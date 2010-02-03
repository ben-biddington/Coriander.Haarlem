package coriander.haarlem.controllers

import java.lang.Long._
import jetbrains.buildServer.controllers.BaseController
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import coriander.haarlem.http.query.Query
import jetbrains.buildServer.web.openapi.{PluginDescriptor, WebControllerManager}
import jetbrains.buildServer.users.SUser
import java.util.ArrayList
import collection.mutable.ListBuffer
import jetbrains.buildServer.serverSide._
import coriander.haarlem.core.Convert

class MetricsController(
	buildServer : SBuildServer,
	pluginDescriptor : PluginDescriptor
) extends BaseController {
	override protected def doHandle(
		request : HttpServletRequest,
		response : HttpServletResponse
	) : ModelAndView = {

		val query = Query(request.getQueryString)

		val userId = query.value("user")

		var result : MetricsModel = null

		if (userId != null) {
			val user : SUser = buildServer.getUserModel.findUserById(parseLong(userId))
			val allBuildsWithDashboards = findAllBuildsWithDashboards(user) 

			result = new MetricsModel(user, Convert.toJavaList(allBuildsWithDashboards))

			return new ModelAndView(
				pluginDescriptor.getPluginResourcesPath + "/metrics/default.jsp",
				"results",
				result
			)
		}

		response.setStatus(400)
		response.getWriter.write("Missing the &lt;user&gt; parameter.")

		null
	}

	def register() {
		val mgr = getApplicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])

		mgr.registerController("/metrics.html", this)
	}

	private def findAllBuildsWithDashboards(user : SUser) : List[SBuildType] = {
		val result = new ListBuffer[SBuildType]()
		
		val projects = Convert.toScalaList(getAllProjects(user))

		projects.foreach(project => {
			val builds = Convert.toScalaList(project.getBuildTypes)

			builds.foreach(build => {
				if (hasDashboard(build)) {
					println("Searching build: " + build.toString)
					result += build
				}
			})
		})

		result toList
	}

	private def hasDashboard(buildType : SBuildType) : Boolean = {
		val dashboardFolderName = "dashboard"

		val lastSuccessful = buildType.getLastChangesSuccessfullyFinished

		if (null == lastSuccessful)
			return false

		val rootDir = lastSuccessful.getArtifactsDirectory.getCanonicalFile

		return if (rootDir.list != null)
			rootDir.list.contains(dashboardFolderName)
		else false
	}

	// TODO: Make sure they're projects I am following
	private def getAllProjects(user : SUser) = {
		// TODO: Spring it
		val projectManager = buildServer.getProjectManager

		val result = new ArrayList[SProject]()

		val projectIdIterator = user.getAllProjects.iterator

		var currentId : String = null

		while (projectIdIterator.hasNext) {
			currentId = projectIdIterator.next

			val project = projectManager.findProjectById(currentId)

			if (project != null) {
				result.add(project)
			}
		}

		result
	}
}

class MetricsModel(
	val user : SUser,
	builds : java.util.List[SBuildType]
) {
	def this(user : SUser) { this(user, null) }

	def getUser = user
	def getBuilds = builds

	def getError = error
	def setError(err : String) = this.error = err
	
	private var error : String = null
}