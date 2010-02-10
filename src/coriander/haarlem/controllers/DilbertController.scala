package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import jetbrains.buildServer.web.openapi.{PluginDescriptor, WebControllerManager}
import coriander.haarlem.rss.{DilbertRssFeed}

class DilbertController(pluginDescriptor : PluginDescriptor) extends BaseController {	
	def register() {
		webControllerManager.registerController("/dilbert.html", this)
	}
	
	override protected def doHandle(
		request : HttpServletRequest,
		response : HttpServletResponse
	) : ModelAndView = {
		val rssFeedItem = getLatestDilbert

		new ModelAndView(
			pluginResourcePath + "default.jsp",
			"results",
			rssFeedItem
		)
	}

	private lazy val webControllerManager = getApplicationContext.
		getBean("webControllerManager", classOf[WebControllerManager])

	private def pluginResourcePath = pluginDescriptor.getPluginResourcesPath + "/server/dilbert/"

	private def getLatestDilbert = new DilbertRssFeed().find
}