package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import jetbrains.buildServer.web.openapi.{WebControllerManager, PluginDescriptor}
import coriander.haarlem.models.ReleasesModel
import jetbrains.buildServer.serverSide.{SFinishedBuild, ProjectManager, SBuildServer}
import coriander.haarlem.core.Convert
import coriander.haarlem.core.calendar.{FilterOptions, IBuildFinder}
import org.joda.time.{Interval, Duration, Instant}

class ReleasesController(
	pluginDescriptor 	: PluginDescriptor,
	buildFinder 		: IBuildFinder
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
		val sevenDaysAgo = new Instant().minus(Duration.standardDays(7))
		val thePastWeek = new Interval(sevenDaysAgo, now)
		Convert.toJavaList(buildFinder.find(new FilterOptions(thePastWeek)))
	}

	private lazy val now 	= new Instant
	private var route 		= "/releases.html"
}
