package coriander.haarlem.core.calendar

import util.matching.Regex
import java.lang.Integer._
import org.joda.time.Days._
import org.joda.time.{DateMidnight, Instant}
import java.util.regex.PatternSyntaxException

class InstantParser(val now : Instant) {
	def parse(what : String) : Instant = {

		if (todayPattern.findFirstIn(what).isDefined)
			return new DateMidnight(now).toInstant
		
		if (yesterdayPattern.findFirstIn(what).isDefined)
			return new DateMidnight(now).minus(days(1)).toInstant

		parseDaysOrWeeks(what)
	}

	private def parseDaysOrWeeks(what : String) = {
		val theMatch = daysOrWeeksPattern.findFirstMatchIn(what)

		if (theMatch == None)
			throw new PatternSyntaxException(
				"Parse failed. " +
				"The value <" + what + "> did not match expected pattern.",
				daysOrWeeksPattern.toString,
				0
			)

		val count = parseInt(theMatch.get.group("count"))

		val unit = theMatch.get.group("unit").toString.toLowerCase

		if (unit == "day") daysAgo(count) else weeksAgo(count)
	}

	private def weeksAgo(howMany : Int) = daysAgo(howMany * 7)
	private def daysAgo(howMany : Int) = new DateMidnight(now).minus(days(howMany)).toInstant
	private val todayPattern = new Regex("(?i)^today$")
	private val yesterdayPattern = new Regex("(?i)^yesterday")
	private val daysOrWeeksPattern = new Regex("(?i)([\\d]+)-(day|week)s?-ago$", "count", "unit")
}