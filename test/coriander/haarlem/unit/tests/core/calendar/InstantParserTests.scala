package coriander.haarlem.unit.tests.core.calendar

import org.scalatest.{Spec, BeforeAndAfterEach}
import org.joda.time.Days._
import coriander.haarlem.core.calendar.InstantParser
import org.joda.time.DateTimeFieldType._
import org.scalatest.matchers.MustMatchers
import org.joda.time.{DateMidnight, Interval, Instant}
import java.util.regex.PatternSyntaxException

class InstantParserTests extends Spec
	with MustMatchers
	with BeforeAndAfterEach {

	override def beforeEach() {
		result = null
	}

	describe("Parsing days ago") {
		it("requires pattern dd-day(s)-ago") {
			when_parsing("1-day-ago")
			then_result_is(midnightYesterday)

			when_parsing("7-days-ago")
			then_result_is(midnightSevenDaysAgo)
		}

		it("you can use \"day\" or \"days\", it's up to you") {
			when_parsing("1-day-ago")
			then_result_is(midnightYesterday)

			when_parsing("1-days-ago")
			then_result_is(midnightYesterday)
		}

		it("uses absolute value of negative numbers") {
			when_parsing("-1-day-ago")
			then_result_is(midnightYesterday)
		}

		it("just returns midnight today when number equal to zero") {
			when_parsing("0-day-ago")
			then_result_is(midnightToday)
		}

		it("rounds down the to nearest prior midnight") {
			when_parsing("1-day-ago")
			then_result_is(midnightYesterday)
			then_the_time_is_midnight()
		}

		it("does case-insensitive matching") {
			when_parsing("1-day-ago")
			then_result_is(midnightYesterday)

			when_parsing("1-DAY-ago")
			then_result_is(midnightYesterday)
		}
	}

	describe("Parsing weeks ago") {
		it("requires pattern dd-week(s)-ago") {
			when_parsing("1-week-ago")
			then_result_is(midnightSevenDaysAgo)

			when_parsing("2-weeks-ago")
			then_result_is(fourteenDaysAgo)
		}

		it("you can use \"week\" or \"weeks\", it's up to you") {
			when_parsing("1-week-ago")
			then_result_is(midnightSevenDaysAgo)

			when_parsing("1-weeks-ago")
			then_result_is(midnightSevenDaysAgo)
		}

		it("rounds down the to nearest prior midnight") {
			when_parsing("1-week-ago")
			then_result_is(midnightSevenDaysAgo)
			then_the_time_is_midnight()
		}

		it("does case-insensitive matching") {
			when_parsing("1-week-ago")
			then_result_is(midnightSevenDaysAgo)

			when_parsing("1-Week-AGO")
			then_result_is(midnightSevenDaysAgo)
		}
	}

	describe("Parsing a string that does not match expected pattern") {
		it("throws a pattern syntax exception") {
			intercept[PatternSyntaxException] {
				when_parsing("xxx_clearly_not_right_pattern_xxx")
			}
		}
	}

	describe("Supports some sugar") {
		it("like \"today\", which is also case-insensitive") {
			when_parsing("today")
			then_result_is(midnight.toInstant)

			when_parsing("ToDay")
			then_result_is(midnight.toInstant)
		}

		it("like \"yesterday\", which is also case-insensitive") {
			when_parsing("yesterday")
			then_result_is(midnightYesterday.toInstant)

			when_parsing("YESTERDay")
			then_result_is(midnightYesterday.toInstant)
		}
	}

	describe("DateMidnight") {
		it(
			"is local when no argument supplied, but UTC when an instant supplied " +
			"because \"An Instant should be used to represent a point in time irrespective " +
			"of any other factor, such as chronology or time zone.\" " +
			"see: http://joda-time.sourceforge.net/api-release/org/joda/time/Instant.html"
		) {
			val localMidnight 	= new DateMidnight()
			val utcMidnight 	= new DateMidnight(new Instant)

			localMidnight must not equal(utcMidnight)
		}
	}

	private def when_parsing(what : String) {
		result = parser.parse(what)
	}

	private def then_result_is(when : Instant) {
		result must equal(when)
	}

	private def then_the_time_is_midnight() {
		result.get(hourOfDay) must equal(0)
		result.get(minuteOfHour) must equal(0)
		result.get(secondOfMinute) must equal(0)
		result.get(millisOfSecond) must equal(0)
	}

	private val now = new Instant
	private val parser = new InstantParser(now)
	private var result : Instant = null

	private val midnight 				= new DateMidnight(now)
	private val midnightToday 			= midnight.toInstant
	private val midnightYesterday 		= midnight.minus(days(1)).toInstant
	private val midnightSevenDaysAgo 	= midnight.minus(days(7)).toInstant
	private val fourteenDaysAgo 		= midnight.minus(days(14)).toInstant
	private val sinceYesterday 			= new Interval(midnightYesterday, now)
}