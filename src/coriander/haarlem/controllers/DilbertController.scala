package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import jetbrains.buildServer.web.openapi.{PluginDescriptor, WebControllerManager}
import coriander.haarlem.rss.{DilbertRssFeed}
import coriander.haarlem.models.DilbertModel

class DilbertController(pluginDescriptor : PluginDescriptor) extends BaseController {	
	override protected def doHandle(
		httpServletRequest : HttpServletRequest,
		httpServletResponse : HttpServletResponse
	) : ModelAndView = {
		val rssFeedItem = getLatestDilbert

		new ModelAndView(
			pluginDescriptor.getPluginResourcesPath + "/default.jsp",
			"",
			rssFeedItem
		)
	}

	def register() {
		val mgr = getApplicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])
		
		mgr.registerController("/dilbert.html", this)
	}

	private def getLatestDilbert = {
		new DilbertRssFeed().find
	}
}