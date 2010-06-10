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
import collection.mutable.ListBuffer
import java.util.regex.PatternSyntaxException
import java.util.ArrayList
import jetbrains.buildServer.users.SUser
import coriander.haarlem.core.astley.{RickrollEligibility, Rick}

class ReleasesController(
	pluginDescriptor 	: PluginDescriptor,
	buildFinder 		: IBuildFinder
) extends Controller {
	this.route = DEFAULT_ROUTE

	def setPlonkers(who : String) {
		plonkers = who.split(",").toList
	}

	override protected def doHandle(
		request : HttpServletRequest,
		response : HttpServletResponse
	) : ModelAndView = {
		errors.clear
		
		val model = tryFind(Query(request.getQueryString))
		
		errors.foreach(model.addError(_))

		if (false == plonkers.isEmpty) {
			val rick = new Rick(plonkers)
			val user = request.getSession.getValue("USER_KEY").asInstanceOf[SUser]
			model.setRickrollable(rick.rollable(user, now))	
		}

		new ModelAndView(view, "results", model)
	}

	private def tryFind(query : Query) = {
		var model = new ReleasesModel(
			new ArrayList[SFinishedBuild](),
			null,
			now
		)

		try {
			model = find(query)
		} catch {
			case e : PatternSyntaxException => error("Invalid regexp")
		}

		model
	}

	private def find(query : Query) : ReleasesModel = {
		var result : List[SFinishedBuild] = null
		var interval = new Interval(0L, 0L)
		val matching = if (query.containsWithValue("matching")) query.value("matching") else null
		var lastHowMany = if (query.containsWithValue("last"))
			parseInt(query.value("last"))
			else DEFAULT_BUILD_COUNT
		val since = query.value("since")
		
		if (query.containsWithValue("since")) {
			val sinceWhen = fromWhen(now, since)
			interval = new Interval(sinceWhen, now)
			result = findSince(interval, matching)
		} else {
			result = findLast(lastHowMany, matching)
			interval = calculateSpanningInterval(result)
		}

		new ReleasesModel(toJavaList(result), interval, now)
	}

	private def findSince(in : Interval, matching : String) = {
		val matcher : SFinishedBuild => Boolean =
			if (matching != null) newBuildMatcher(matching) else build => true
		
		buildFinder.find(new FilterOptions(in, matcher))
	}

	private def findLast(howMany : Int, matching : String) = {
		var lastHowMany = howMany
		if (lastHowMany > MAX_BUILD_COUNT) {
			lastHowMany  = MAX_BUILD_COUNT
		}
		
		val options =
			if (matching != null) new FilterOptions(null, newBuildMatcher(matching))
			else FilterOptions.NONE

		val result = buildFinder.last(lastHowMany, options)

		if (howMany > MAX_BUILD_COUNT) {
			val hasProbablyBeenTruncated = result.size == 200
			if (hasProbablyBeenTruncated) {
				error(
					"The requested number of results exceeds the limit of <" + MAX_BUILD_COUNT + ">, " +
					"results have been truncated"
				)
			}
		}

		result
	}

	private def calculateSpanningInterval(builds : List[SFinishedBuild]) : Interval = {
		if (builds.size == 0)
			 return new Interval(0L, 0L)

		val from = new DateTime(builds.first.getFinishDate, DateTimeZone.UTC)

		val to = new DateTime(builds.last.getFinishDate, DateTimeZone.UTC)

		new Interval(to, from)
	}

	private def newBuildMatcher(thatMatches : String) : SFinishedBuild => Boolean = {
		build => {
			matcher.matches(build.getBuildDescription, thatMatches) ||
			matcher.matches(build.getBuildTypeName, thatMatches)
		}
	}

	private def fromWhen(now : Instant, since : String) : Instant = {
		var result = DEFAULT_DAYS_AGO
		
		if (since != null) {
			result = parse(now, since)

			if (result.isBefore(MAX_DAYS_AGO)) {
				result = new DateMidnight(now).minus(days(MAX_DAY_COUNT)).toInstant

				error(
					"The requested number of days exceeds the limit of <" + MAX_DAY_COUNT + ">, " +
					"results have been truncated"
				)
			}
		}

		return result
	}

	private def error(what : String) = errors += what

	private def parse(now : Instant, what : String) = new InstantParser(now).parse(what)

	private lazy val now 					= new Instant
	private lazy val MAX_DAY_COUNT 			= 90
	private lazy val DEFAULT_BUILD_COUNT 	= 25
	private lazy val MAX_BUILD_COUNT 		= 200
	private lazy val view 					= pluginDescriptor.getPluginResourcesPath + "/server/releases/default.jsp"
	private lazy val sevenDaysAgo 			= new DateMidnight(new Instant).minus(days(7)).toInstant
	private lazy val ninetyDaysAgo 			= new DateMidnight(new Instant).minus(days(MAX_DAY_COUNT)).toInstant
	private lazy val DEFAULT_DAYS_AGO 		= sevenDaysAgo
	private lazy val MAX_DAYS_AGO 			= ninetyDaysAgo
	private lazy val DEFAULT_ROUTE 			= "releases.html"
	private lazy val matcher 				= new StringMatcher()
	private var errors						= new ListBuffer[String]()
	private var plonkers : List[String]		= List()
}
