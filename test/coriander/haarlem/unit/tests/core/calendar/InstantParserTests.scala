package coriander.haarlem.unit.tests.core.calendar

import org.scalatest.{Spec, BeforeAndAfterEach}
import org.joda.time.Days._
import coriander.haarlem.core.calendar.InstantParser
import org.joda.time.DateTimeFieldType._
import org.scalatest.matchers.{ShouldMatchers, MustMatchers}
import org.joda.time.{DateMidnight, Interval, Instant}

class InstantParserTests extends Spec
	with ShouldMatchers
	with MustMatchers
	with BeforeAndAfterEach {

	override def beforeEach() {
		result = null
	}

	describe("Parsing days ago") {
		it("requires pattern dd-day(s)-ago") {
			when_parsing("1-day-ago")
			then_result_is_more_or_less(yesterday)

			when_parsing("7-days-ago")
			then_result_is_more_or_less(sevenDaysAgo)
		}

		it("you can use \"day\" or \"days\", it's up to you") {
			when_parsing("1-day-ago")
			then_result_is_more_or_less(yesterday)

			when_parsing("1-days-ago")
			then_result_is_more_or_less(yesterday)
		}

		it("uses absolute value of negative numbers") {
			when_parsing("-1-day-ago")
			then_result_is_more_or_less(yesterday)
		}

		it("fails when number equal to zero") {
			intercept[IllegalArgumentException] {
				when_parsing("0-day-ago")
			}
		}

		it("rounds down the to nearest prior midnight") {
			when_parsing("1-day-ago")
			then_result_is_more_or_less(yesterday)
			then_the_time_is_midnight()
		}
	}

	describe("Parsing weeks ago") {
		it("requires pattern dd-week(s)-ago") {
			when_parsing("1-week-ago")
			then_result_is_more_or_less(sevenDaysAgo)

			when_parsing("2-weeks-ago")
			then_result_is_more_or_less(fourteenDaysAgo)
		}

		it("you can use \"week\" or \"weeks\", it's up to you") {
			when_parsing("1-week-ago")
			then_result_is_more_or_less(sevenDaysAgo)

			when_parsing("1-weeks-ago")
			then_result_is_more_or_less(sevenDaysAgo)
		}

		it("rounds down the to nearest prior midnight") {
			when_parsing("1-week-ago")
			then_result_is_more_or_less(sevenDaysAgo)
			then_the_time_is_midnight()
		}
	}

	describe("Parsing a string that does not match expected pattern") {
		it("throws an exception") {
			intercept[Exception] {
				when_parsing("xxx_clearly_not_right_pattern_xxx")
			}
		}
	}

	describe("Supports some sugar") {
		it("things like \"yesterday\" or \"last week\"") {
			(pending)
		}
	}

	private def when_parsing(what : String) {
		result = parser.parse(what)
	}

	private def then_result_is_more_or_less(when : Instant) {
		// @todo: consider using plusOrMinus
		// result should be (when plusOrMinus 2)

		result.get(year) should equal(when.get(year))
		result.get(monthOfYear) should equal(when.get(monthOfYear))
		result.get(dayOfMonth) should equal(when.get(dayOfMonth))

		then_the_time_is_midnight
	}

	private def then_the_time_is_midnight() {
		result.get(hourOfDay) should equal(0)
		result.get(minuteOfHour) should equal(0)
		result.get(secondOfMinute) should equal(0)
		result.get(millisOfSecond) should equal(0)

		//DateMidnight.
	}

	private val now = new Instant
	private val parser = new InstantParser(now)
	private var result : Instant = null

	private val yesterday 			= now minus(days(1).toStandardDuration)
	private val sevenDaysAgo 		= now minus(days(7).toStandardDuration)
	private val fourteenDaysAgo 	= now minus(days(14).toStandardDuration)
	private val sinceYesterday 		= new Interval(yesterday, now)
}