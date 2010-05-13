package coriander.haarlem.matchers

import org.joda.time.Interval
import org.junit.internal.matchers.TypeSafeMatcher
import coriander.haarlem.core.calendar.FilterOptions
import org.hamcrest.Description

class FilterOptionsMatcher(expected : Interval)
	extends TypeSafeMatcher[FilterOptions] {

	var actual : FilterOptions = null

	override def matchesSafely(what : FilterOptions) = {
		actual = what

		// @todo: introduce tolerance
		actual.interval.equals(expected) ||
			expected.contains(actual.interval)
	}

	def describeTo(description : Description) {
    	description.appendText(
			"Expected <" + expected + "> but got <" + actual.interval + ">"
		)
  	}
}

object FilterOptionsMatcher {
	def hasMatching(expected : Interval) = new FilterOptionsMatcher(expected)
}