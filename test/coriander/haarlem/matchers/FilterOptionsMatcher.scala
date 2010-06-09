package coriander.haarlem.matchers

import org.junit.internal.matchers.TypeSafeMatcher
import coriander.haarlem.core.calendar.FilterOptions
import org.hamcrest.Description
import org.joda.time.Hours._
import org.joda.time.{DateTime, Interval}
import org.joda.time.format.DateTimeFormat._

class FilterOptionsMatcher(expected : Interval)
	extends TypeSafeMatcher[FilterOptions] {

	var actual : FilterOptions = null

	override def matchesSafely(what : FilterOptions) = {
		actual = what

		startRoughlyEquals(expected.getStart) &&
		endRoughlyEquals(expected.getEnd)
	}

	def describeTo(description : Description) {
		description.appendText(
			"Expected: interval between \n\t<" + format(expected.getStart) + "> and <" + format(expected.getEnd) + "> \n"
		)

		if (actual != null) {
			description.appendText(
				"Actual: interval between \n\t<" + format(actual.interval.getStart) + "> and <" + format(actual.interval.getEnd) + "> "
			)
		} else {
			description.appendText(
				"Actual: <null>"
			)
		}
  	}

	private def format(what : DateTime) =
		what.toString(fullDate) + " at " + what.toString(fullTime)

	private def startRoughlyEquals(when : DateTime) = {
		val theDifference = Math.abs(actual.interval.getStart.getMillis - when.getMillis)

		theDifference <= gracePeriod.getMillis
	}

	private def endRoughlyEquals(when : DateTime) = {
		val theDifference = Math.abs(actual.interval.getEnd.getMillis - when.getMillis)

		theDifference <= gracePeriod.getMillis
	}

	private val gracePeriod = hours(1).toStandardDuration
}

object FilterOptionsMatcher {
	def hasMatching(expected : Interval) = new FilterOptionsMatcher(expected)
}