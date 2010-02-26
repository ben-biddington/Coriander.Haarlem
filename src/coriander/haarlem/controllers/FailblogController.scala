package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import jetbrains.buildServer.web.openapi.{PluginDescriptor, WebControllerManager}
import coriander.haarlem.rss.FailblogRssFeed

class FailblogController(pluginDescriptor : PluginDescriptor) extends BaseController {
	def register() {
		webControllerManager.registerController(route, this)
	}

	override protected def doHandle(
		request : HttpServletRequest,
		response : HttpServletResponse
	) = {
		new ModelAndView(
			pluginResourcePath + "default.jsp",
			"results",
			getLatestFail
		)
	}

	def setRoute(route : String) = this.route = route

	private lazy val webControllerManager = getApplicationContext.
		getBean("webControllerManager", classOf[WebControllerManager])

	private def pluginResourcePath = pluginDescriptor.getPluginResourcesPath + "/server/failblog/"

	private def getLatestFail = new FailblogRssFeed().find

	private var route : String = null
}