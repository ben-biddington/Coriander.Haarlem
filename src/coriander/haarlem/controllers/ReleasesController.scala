package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import jetbrains.buildServer.web.openapi.{WebControllerManager, PluginDescriptor}
import coriander.haarlem.models.ReleasesModel
import coriander.haarlem.core.Convert._
import org.joda.time._
import org.joda.time.Days._
import coriander.haarlem.http.query.Query
import coriander.haarlem.core.calendar.{InstantParser, FilterOptions, IBuildFinder}
import jetbrains.buildServer.serverSide.SFinishedBuild
import java.util.ArrayList
import java.lang.Integer._

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
	
	def go(request : HttpServletRequest, response : HttpServletResponse) =
		doHandle(request, response)
	
	override protected def doHandle(
		request : HttpServletRequest,
		response : HttpServletResponse
	) : ModelAndView = {
		val now = new Instant

		val query = Query(request.getQueryString)

		var searchResult : java.util.List[SFinishedBuild] = new ArrayList[SFinishedBuild]()

		var interval : Interval = new Interval(0L, 0L)
		
		if (query.contains("since")) {
			val sinceWhen = fromWhen(now, query)
			interval = new Interval(sinceWhen, now)
			searchResult = findBuildsIn(interval)
		} else if (query.contains("last")) {
			searchResult = findLast(parseInt(query.value("last")))
		}

		new ModelAndView(
			view,
			"results",
			new ReleasesModel(
				searchResult,
				interval,
				now
			) 
		)
	}

	private def fromWhen(now : Instant, query : Query) : Instant = {
		var value = query.value("since")

		if (value != null) parse(now, value) else DEFAULT
	}

	private def parse(now : Instant, what : String) =
		new InstantParser(now).parse(what) 

	private def findBuildsIn(interval : Interval) =
		toJavaList(buildFinder.find(new FilterOptions(interval)))

	private def findLast(howMany : Int) =
		toJavaList(buildFinder.last(howMany))

	private lazy val view 	= pluginDescriptor.getPluginResourcesPath + "/server/releases/default.jsp"
	private var route 		= "/releases.html"

	private lazy val sevenDaysAgo 	= new Instant().minus(days(7).toStandardDuration)
	private lazy val DEFAULT 		= sevenDaysAgo
}
