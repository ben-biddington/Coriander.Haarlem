package coriander.haarlem.controllers

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import jetbrains.buildServer.controllers.BaseController
import jetbrains.buildServer.web.openapi.{WebControllerManager, PluginDescriptor}
import jetbrains.buildServer.serverSide.{SBuildType, SBuildServer, ProjectManager}
import coriander.haarlem.models.GreatestHitsModel
import coriander.haarlem.core.Convert
import coriander.haarlem.http.query.Query

class GreatestHitsController(
	pluginDescriptor : PluginDescriptor,
	projectManager : ProjectManager,
	buildServer : SBuildServer
) extends BaseController {
	def register() {
		val mgr = getApplicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])

		mgr.registerController(route, this)
	}

	def setRoute(route : String) = this.route = route

	def go(request : HttpServletRequest, response : HttpServletResponse) =
		doHandle(request, response)

	var key = "buildTypeId"

	override protected def doHandle(
		request : HttpServletRequest,
		response : HttpServletResponse
	) : ModelAndView = {
		var query = new Query(request.getQueryString)

		val buildTypeIds = if (query.contains(key)) query.value(key) else "none"
		
		val firstBuildType : SBuildType = Convert.toScalaList(projectManager.getAllBuildTypes).first

		new ModelAndView(
			view,
			"results",
			new GreatestHitsModel("Ben rules! And here are the builds you asked for: " + buildTypeIds)
		)
	}

	private lazy val view 	= pluginDescriptor.getPluginResourcesPath + "/server/greatesthits/default.jsp"
	private var route 		= "/phil.html"
}