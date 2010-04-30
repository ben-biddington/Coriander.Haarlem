package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import jetbrains.buildServer.web.openapi.{WebControllerManager, PluginDescriptor}
import org.joda.time
import time.DateTime
import coriander.haarlem.models.ReleasesModel
import jetbrains.buildServer.serverSide.{SFinishedBuild, ProjectManager, SBuildServer}
import coriander.haarlem.core.Convert

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
		new ModelAndView(
			pluginDescriptor.getPluginResourcesPath + "/server/releases/default.jsp",
			"results",
			new ReleasesModel(findAllOfTheBuildsIAmSupposedToShow)
		)
	}

	private def findAllOfTheBuildsIAmSupposedToShow : java.util.List[SFinishedBuild] = {
		Convert.toJavaList(List())
	}

	private var route : String = "/releases.html"
}
