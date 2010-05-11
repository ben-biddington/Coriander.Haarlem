package coriander.haarlem.unit.tests.core.calendar

import org.scalatest.matchers.{ShouldMatchers, MustMatchers}
import org.scalatest.{Spec, BeforeAndAfterEach}
import coriander.haarlem.core.calendar.IntervalParser
import org.joda.time.{Interval, Instant}
import org.joda.time.Days._

class IntervalParserTests extends Spec
	with ShouldMatchers
	with MustMatchers
	with BeforeAndAfterEach {

	override def beforeEach() {
		result = null
	}

	describe("Parsing days") {
		it("requires pattern dd-day(s)-ago") {
			(pending)
			when_parsing("1-day-ago")

			then_result_is_about(sinceYesterday)
		}
	}

	private def when_parsing(what : String) {
		result = parser.parse("1-day-ago")
	}

	private def then_result_is_about(what : Interval) {
		
	}

	private val parser = new IntervalParser
	private var result : Interval = null

	private val now = new Instant

	private val yesterday 			= new Instant().minus(days(1).toStandardDuration)
	private val sinceYesterday 		= new Interval(yesterday, now)
}