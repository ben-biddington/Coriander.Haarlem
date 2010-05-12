package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import jetbrains.buildServer.web.openapi.{WebControllerManager, PluginDescriptor}
import coriander.haarlem.models.ReleasesModel
import jetbrains.buildServer.serverSide.{SFinishedBuild}
import coriander.haarlem.core.Convert
import coriander.haarlem.core.calendar.{FilterOptions, IBuildFinder}
import org.joda.time._
import org.joda.time.Days._

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
		val sevenDaysAgo = new Instant().minus(days(7).toStandardDuration)
		val thePastWeek = new Interval(sevenDaysAgo, now)
		
		new ModelAndView(
			view,
			"results",
			new ReleasesModel(
				findAllOfTheBuildsIAmSupposedToShow(thePastWeek),
				thePastWeek,
				now
			) 
		)
	}

	private def findAllOfTheBuildsIAmSupposedToShow(interval : Interval) = {
		var list = buildFinder.find(new FilterOptions(interval))
		
		Convert.toJavaList(list)
	}

	private lazy val view 	= pluginDescriptor.getPluginResourcesPath + "/server/releases/default.jsp"
	private lazy val now 	= new Instant
	private var route 		= "/releases.html"
}
