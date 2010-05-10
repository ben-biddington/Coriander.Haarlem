package coriander.haarlem.unit.tests.core.calendar

import org.scalatest.{BeforeAndAfterEach, Spec}
import org.joda.time.{Duration, Instant, Interval}
import org.scalatest.matchers.{MustMatchers, ShouldMatchers}
import coriander.haarlem.matchers.IntervalMatchers

class LastTests extends Spec
	with ShouldMatchers
	with MustMatchers
	with BeforeAndAfterEach
	with IntervalMatchers
{
	describe("Last") {
		it("two weeks means something") {
			val lastTwoWeeks : Interval = Last.days(7)
			val twoDaysAgo = now.minus(Duration.standardDays(2))

			lastTwoWeeks must contain(twoDaysAgo)
		}
    }

	private val now : Instant = new Instant()
}

object Last {
	def days(howMany : Long) = {
		val now = new Instant()
		val start = now.minus(Duration.standardDays(howMany))

		new Interval(start, now)
	}
}