package coriander.haarlem.core.calendar

import java.lang.Integer._
import util.matching.Regex
import org.joda.time.Days._
import org.joda.time.{DateMidnight, Instant}

class InstantParser(val now : Instant) {
	def parse(what : String) : Instant = {
		val pattern : Regex = new Regex("([\\d]+)-(day|week)s?-ago$", "count", "unit")

		val theMatch = pattern.findFirstMatchIn(what)

		if (theMatch == None)
			throw new Exception(
				"Parse failed. " +
				"The value <" + what + "> did not match expected pattern."
			)

		val count = parseInt(theMatch.get.group("count"))

		if (count == 0)
			throw new IllegalArgumentException("The count must be greater than zero")

		val unit = theMatch.get.group("unit").toString.toLowerCase

		if (unit == "day") daysAgo(count) else weeksAgo(count)
	}

	private def weeksAgo(howMany : Int) =
		daysAgo(howMany * 7)

	private def daysAgo(howMany : Int) : Instant = 
		new DateMidnight(now).minus(days(howMany)).toInstant
}