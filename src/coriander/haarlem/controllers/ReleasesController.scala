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
import java.lang.Integer._
import coriander.haarlem.core.StringMatcher

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
		val query = Query(request.getQueryString)

		new ModelAndView(
			view,
			"results",
			find(query) 
		)
	}

	private def find(query : Query) : ReleasesModel = {
	 	val now = new Instant
		var result : List[SFinishedBuild] = null
		var interval = new Interval(0L, 0L)
		val matching = if (query.contains("matching")) query.value("matching") else null

		if (query.contains("since")) {
			interval = new Interval(fromWhen(now, query), now)
			result = buildFinder.find(new FilterOptions(interval, newBuildMatcher(matching)))
		} else {
			val lastHowMany = if (query.containsWithValue("last"))
				parseInt(query.value("last"))
			else DEFAULT_BUILD_COUNT

			result = findLast(lastHowMany, matching)
			interval = calculateInterval(result)
		}

		new ReleasesModel(toJavaList(result), interval, now)
	}

	private def calculateInterval(builds : List[SFinishedBuild]) : Interval = {
		if (builds.size == 0)
			 return new Interval(0L, 0L)

		var from = new DateTime(builds.first.getFinishDate, DateTimeZone.UTC)

		var to = new DateTime(builds.last.getFinishDate, DateTimeZone.UTC)

		new Interval(to, from)
	}

	private def findLast(
		howMany : Int,
		matching : String
	) = {
		val options =
			if(matching != null) new FilterOptions(null, newBuildMatcher(matching))
			else FilterOptions.NONE

		buildFinder.last(howMany, options)
	}

	private def newBuildMatcher(thatMatches : String) : SFinishedBuild => Boolean = {
		 build =>
			matcher.matches(build.getBuildDescription, thatMatches) ||
			matcher.matches(build.getBuildTypeName, thatMatches)
	}

	private def fromWhen(now : Instant, query : Query) : Instant = {
		var value = query.value("since")

		if (value != null) parse(now, value) else DEFAULT
	}

	private def parse(now : Instant, what : String) = new InstantParser(now).parse(what)

	private lazy val view 			= pluginDescriptor.getPluginResourcesPath + "/server/releases/default.jsp"
	private var route 							= "/releases.html"
	private lazy val sevenDaysAgo 				= new Instant().minus(days(7).toStandardDuration)
	private lazy val DEFAULT 					= sevenDaysAgo
	private lazy val DEFAULT_BUILD_COUNT 		= 25
	private lazy val matcher 					= new StringMatcher()
}
