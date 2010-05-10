package coriander.haarlem.matchers

import org.joda.time.{Interval, Instant}
import org.scalatest.matchers.{MatchResult, Matcher}

trait IntervalMatchers {
	class IntervalMatcher (val right : Instant) extends Matcher[Interval] {
		def apply(interval : Interval) = {
			new MatchResult(
				interval.contains(right),
				"The interval <" + interval + "> " +
				"does not contain the instant <" + right + ">", ""
			)
		}
   	}

	def contain(right : Instant) = new IntervalMatcher(right)
}