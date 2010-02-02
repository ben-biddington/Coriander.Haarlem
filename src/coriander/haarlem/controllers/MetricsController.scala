package coriander.haarlem.controllers

import java.lang.Long._
import jetbrains.buildServer.controllers.BaseController
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import coriander.haarlem.http.query.Query
import jetbrains.buildServer.web.openapi.{PluginDescriptor, WebControllerManager}
import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.serverSide.{SProject, SBuildServer}
import java.util.ArrayList

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
			val allProjects = getAllProjects(user)

			buildServer.find

			// Now get the artifacts directory, and search for dashboard.xml

			result = new MetricsModel(user, allProjects)
		}

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

class MetricsModel(val user : SUser, projects : java.util.List[SProject]) {
	def this(user : SUser) { this(user, null) }

	def getUser = user
	def getProjects = projects

	def getError = error
	def setError(err : String) = this.error = err
	
	private var error : String = null
}