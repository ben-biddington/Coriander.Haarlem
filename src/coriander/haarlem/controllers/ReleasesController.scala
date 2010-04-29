package coriander.haarlem.controllers

import jetbrains.buildServer.serverSide.{ProjectManager, SBuildServer}
import jetbrains.buildServer.controllers.BaseController
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import jetbrains.buildServer.web.openapi.{WebControllerManager, PluginDescriptor}
import org.joda.time
import time.DateTime

class ReleasesController(
	buildServer 		: SBuildServer,
	projectManager 		: ProjectManager,
	pluginDescriptor 	: PluginDescriptor
) extends BaseController {
	def register() {
		val mgr = getApplicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])

		mgr.registerController(route, this)
	}

	def setRoute(route : String) = this.route = route
	
	def go(request : HttpServletRequest, response : HttpServletResponse) = {
		doHandle(request, response)
	}
	
	override protected def doHandle(
		request : HttpServletRequest,
		response : HttpServletResponse
	) : ModelAndView = {
		response.setStatus(200)
		val today = new DateTime()
		response.getOutputStream.print(today.toString("dd:MM:yyyy"))

		null
	}

	private var route : String = "/releases.html"
}
