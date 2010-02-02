package coriander.haarlem.controllers

import java.lang.Long._
import jetbrains.buildServer.controllers.BaseController
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import coriander.haarlem.http.query.Query
import jetbrains.buildServer.serverSide.SBuildServer
import jetbrains.buildServer.web.openapi.{PluginDescriptor, WebControllerManager}
import jetbrains.buildServer.users.SUser

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
			
			result = new MetricsModel(user)
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
}

class MetricsModel(val user : SUser, val error : String) {
	def this(user : SUser) { this(user, null) }

	def getError = error
	def getUser = user
}