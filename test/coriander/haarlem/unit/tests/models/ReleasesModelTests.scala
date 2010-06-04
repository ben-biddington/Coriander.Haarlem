package coriander.haarlem.unit.tests.models

import org.scalatest.{Spec, BeforeAndAfterEach}
import org.scalatest.matchers.{MustMatchers}
import coriander.haarlem.models.ReleasesModel
import org.joda.time.Days._
import org.joda.time.Hours._
import org.joda.time.DateTimeConstants._
import org.joda.time.DateTimeZone._
import jetbrains.buildServer.serverSide.SFinishedBuild
import coriander.haarlem.core.Convert._
import collection.mutable.ListBuffer
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.joda.time.{DateTime, Period, DateMidnight, Interval}

class ReleasesModelTests extends Spec
	with MustMatchers
	with BeforeAndAfterEach {

	override def beforeEach() {
		model = null
		today = DEFAULT_TODAY
	}

	describe("getIntervalInDays") {
		it("returns the same number of days as the interval supplied") { 
			given_a_one_day_interval_from_the_day_before_the_first_bikini_to_the_day_of_the_first_bikini

			val numberOfDays = model.getIntervalInDays

			numberOfDays must equal(1)
		}

		it("returns 1 when the interval start and end are less than 24 hours apart") {
			given_a_three_hour_interval_on_the_day_of_the_first_bikini

			val numberOfDays = model.getIntervalInDays

			numberOfDays must equal(1)
		}
	}

	describe("getIntervalStart") {
		it("is a string representing the start of the interval") {
			given_a_one_day_interval_from_the_day_before_the_first_bikini_to_the_day_of_the_first_bikini

			model.getIntervalStart must equal("02 Jun 1946")
		}
	}

	describe("getIntervalEnd") {
		it("is a string representing the end of the interval") {
			given_today_is(firstBikiniDisplayedInParis.plusDays(1))
			given_a_one_day_interval_from_the_day_before_the_first_bikini_to_the_day_of_the_first_bikini
			
			model.getIntervalEnd must equal("03 Jun 1946")
		}

		it("is \"Today\" if the interval ends on today") {
			given_today_is(firstBikiniDisplayedInParis)
			
			given_a_one_day_interval_from_the_day_before_the_first_bikini_to_the_day_of_the_first_bikini

			model.getIntervalEnd must equal("Today")
		}
	}

	describe("getCount") {
		it("returns the number of builds, so is zero when there are none") {
			given_zero_builds
			model.getCount must equal(0)
		}

		it("is 1 for single build") {
			given_builds(1)
			model.getCount must equal(1)
		}

		it("is 2 for two builds") {
			given_builds(2)
			model.getCount must equal(2)
		}
	}

	describe("getDayOfMonth") {
		it("returns the day of the month") {
			given_today_is(firstBikiniDisplayedInParis)
			given_a_new_model
			
			model.getDayOfMonth must equal(3)
		}
	}

	describe("getMonthOfYear") {
		it("returns the day of the month") {
			given_today_is(firstBikiniDisplayedInParis)
			given_a_new_model

			model.getMonthOfYear must equal("Jun")
		}
	}

	describe("getErrors") {
		it("returns the list of errors as one string") {
			model = new ReleasesModel(null, null, null)

			model.addError("1. Phil Murphy's hair.")
			model.addError("2. The pedestrianization of Norwich city centre.")

			model.getErrors must equal(
				"1. Phil Murphy's hair." + NEWLINE +
				"2. The pedestrianization of Norwich city centre."
			)
		}
	}

	private def given_today_is(when : DateMidnight) {
		given_today_is(when.toDateTime)
	}

	private def given_today_is(when : DateTime) {
		today = when
	}

	private def given_a_new_model {
		given_zero_builds
	}

	private def given_zero_builds {
		given_builds(0)
	}

	private def given_builds(howMany : Int) {
		val theList = (0 until howMany).map(count => mock(classOf[SFinishedBuild]))
		
		val interval = new Interval(oneDay, firstBikiniDisplayedInParis.toInstant)
		model = new ReleasesModel(toJavaList(theList), interval, today.toInstant)
	}

	private def given_a_one_day_interval_from_the_day_before_the_first_bikini_to_the_day_of_the_first_bikini() {
		val interval = new Interval(oneDay, firstBikiniDisplayedInParis.toInstant)

		model = new ReleasesModel(null, interval, today.toInstant)
	}

	private def given_a_three_hour_interval_on_the_day_of_the_first_bikini() {
		val interval = new Interval(threeHours, firstBikiniDisplayedInParis.toInstant)

		model = new ReleasesModel(null, interval, today.toInstant)
	}

	private var model : ReleasesModel = null
	private lazy val firstBikiniDisplayedInParis = new DateMidnight(1946, JUNE, 3, UTC)
	private var today : DateTime = null
	private val DEFAULT_TODAY = new DateTime
	private lazy val threeHours = new Period(hours(3))
	private lazy val oneDay = new Period(days(1))
	private lazy val twoDays = new Period(days(2))
	private val NEWLINE = System.getProperty("line.separator")
}