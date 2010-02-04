package coriander.haarlem.controllers

import java.lang.Long._
import java.io.File
import java.util.ArrayList
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.web.servlet.ModelAndView
import collection.mutable.ListBuffer
import jetbrains.buildServer.controllers.BaseController
import jetbrains.buildServer.web.openapi.{PluginDescriptor, WebControllerManager}
import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.serverSide._
import coriander.haarlem.http.query.Query
import coriander.haarlem.core.{Dashboard, Convert}
import coriander.haarlem.models.{DashboardInfo, MetricsModel}

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

		var user : SUser = null

		if (userId != null) {
			user = buildServer.getUserModel.findUserById(parseLong(userId))

			if (user != null) {
				return run(user)
			}
		}

		response.setContentType("text/html")

		if (null == userId) {
			response.setStatus(400)
			response.getWriter.write("Missing the &lt;user&gt; parameter.")
		}  else if (null == user) {
			response.setStatus(404)
			response.getWriter.write(
				"Not found. " +
				"The user with id &lt;" + userId + "&gt; could not be found."
			)			
		}

		null
	}

	private def run(user: SUser) = {
		val allBuildsWithDashboards = findAllBuildsWithDashboards(user)

		var temp = new ListBuffer[DashboardInfo]
		val dashboardRenderer = new Dashboard()
		val xsl = new File(
			buildServer.getServerRootPath +
			pluginDescriptor.getPluginResourcesPath +
			"/metrics/dashboard.xsl"
		)

		allBuildsWithDashboards.foreach(build => {
			temp += new DashboardInfo(	
				build,
				dashboardRenderer.run(build, xsl)
			)
		})
		
		val result = new MetricsModel(user, Convert.toJavaList(temp.toList))
		
		new ModelAndView(
			pluginDescriptor.getPluginResourcesPath + "/metrics/default.jsp",
			"results",
			result
		)
	}

	def register() {
		val mgr = getApplicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])

		mgr.registerController("/metrics.html", this)
	}

	private def findAllBuildsWithDashboards(user : SUser) : List[SBuildType] = {
		val result = new ListBuffer[SBuildType]()
		
		val projects = Convert.toScalaList(getAllProjects(user))

		projects.foreach((project : SProject) => {
			val builds = Convert.toScalaList(project.getBuildTypes)

			builds.foreach(build => {
				if (hasDashboard(build)) {
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

