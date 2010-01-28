package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import jetbrains.buildServer.web.openapi.{PluginDescriptor, WebControllerManager}

class DilbertController(pluginDescriptor : PluginDescriptor) extends BaseController {
	
	override protected def doHandle(
		httpServletRequest : HttpServletRequest,
		httpServletResponse : HttpServletResponse
	) : ModelAndView = {
		val latestDil = getLatestDilbert

		new ModelAndView(
			pluginDescriptor.getPluginResourcesPath + "/default.jsp",
			"",
			new DilbertModel(latestDil)
		)
	}

	def register() {
		val mgr = getApplicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])
		
		mgr.registerController("/dilbert.html", this)
	}

	private def getLatestDilbert = {
		new DilbertFinder().find
	}
}

class DilbertModel(url : String) {
	def getUrl = url	
}