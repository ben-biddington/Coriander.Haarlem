package coriander.haarlem.core.calendar

import java.lang.Integer._
import util.matching.Regex
import util.matching.Regex.Match
import org.joda.time.{Instant}
import org.joda.time.Days._

class InstantParser(val now : Instant) {
	def parse(what : String) : Instant = {
		val pattern : Regex = new Regex("([\\d]+)-(day[s]?)-ago", "count", "unit")

		val theMatch : Option[Match] = pattern.findFirstMatchIn(what)

		if (theMatch == None)
			throw new Exception("The value did not match expected pattern")

		val count = parseInt(theMatch.get.group("count"))
		val unit = theMatch.get.group("unit")

		val result = daysAgo(count)

		println(result)

		result
	}

	private def daysAgo(howMany : Int) =
		now.minus(days(howMany).toStandardDuration)
}