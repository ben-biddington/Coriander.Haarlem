package coriander.haarlem.controllers

import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import jetbrains.buildServer.web.openapi.{PluginDescriptor}
import coriander.haarlem.models.ReleasesModel
import coriander.haarlem.core.Convert._
import org.joda.time._
import org.joda.time.Days._
import coriander.haarlem.http.query.Query
import coriander.haarlem.core.calendar.{InstantParser, FilterOptions, IBuildFinder}
import java.lang.Integer._
import coriander.haarlem.core.StringMatcher
import jetbrains.buildServer.serverSide.{SFinishedBuild}

class ReleasesController(
	pluginDescriptor 	: PluginDescriptor,
	buildFinder 		: IBuildFinder
) extends Controller {
	this.route = DEFAULT_ROUTE

	override protected def doHandle(
		request : HttpServletRequest,
		response : HttpServletResponse
	) : ModelAndView = {
		new ModelAndView(
			view,
			"results",
			find(Query(request.getQueryString)) 
		)
	}

	private def find(query : Query) : ReleasesModel = {
	 	val now = new Instant
		var result : List[SFinishedBuild] = null
		var interval = new Interval(0L, 0L)
		val matching = if (query.contains("matching")) query.value("matching") else null

		if (query.contains("since")) {
			val sinceWhen = fromWhen(now, query)

			println(sinceWhen)

			interval = new Interval(sinceWhen, now)
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

		val from = new DateTime(builds.first.getFinishDate, DateTimeZone.UTC)

		val to = new DateTime(builds.last.getFinishDate, DateTimeZone.UTC)

		new Interval(to, from)
	}

	private def findLast(howMany : Int, matching : String) = {
		val options =
			if(matching != null) new FilterOptions(null, newBuildMatcher(matching))
			else FilterOptions.NONE

		buildFinder.last(howMany, options)
	}

	private def newBuildMatcher(thatMatches : String) : SFinishedBuild => Boolean = {
		build => {
			matcher.matches(build.getBuildDescription, thatMatches) ||
			matcher.matches(build.getBuildTypeName, thatMatches)
		}
	}

	private def fromWhen(now : Instant, query : Query) : Instant = {
		var value = query.value("since")
		var result = DEFAULT_DAYS_AGO
		
		if (value != null) {
			result = parse(now, value)
		
			if (result.isBefore(MAX_DAYS_AGO)) {
				result = new DateMidnight(now).minus(days(90)).toInstant
			}
		}

		return result
	}

	private def parse(now : Instant, what : String) = new InstantParser(now).parse(what)

	private lazy val view 						= pluginDescriptor.getPluginResourcesPath + "/server/releases/default.jsp"
	private lazy val sevenDaysAgo 				= new DateMidnight(new Instant).minus(days(7)).toInstant
	private lazy val ninetyDaysAgo 				= new DateMidnight(new Instant).minus(days(90)).toInstant
	private lazy val DEFAULT_DAYS_AGO 			= sevenDaysAgo
	private lazy val MAX_DAYS_AGO 				= ninetyDaysAgo
	private lazy val DEFAULT_BUILD_COUNT 		= 25
	private lazy val DEFAULT_ROUTE 				= "releases.html"
	private lazy val matcher 					= new StringMatcher()
}
