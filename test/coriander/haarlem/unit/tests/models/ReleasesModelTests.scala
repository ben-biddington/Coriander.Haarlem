package coriander.haarlem.unit.tests.models

import org.scalatest.{Spec, BeforeAndAfterEach}
import org.scalatest.matchers.{MustMatchers}
import coriander.haarlem.models.ReleasesModel
import org.joda.time.Days._
import org.joda.time.Seconds._
import org.joda.time.Minutes._
import org.joda.time.Hours._
import org.joda.time.DateTimeConstants._
import org.joda.time.DateTimeZone._
import jetbrains.buildServer.serverSide.SFinishedBuild
import coriander.haarlem.core.Convert._
import collection.mutable.ListBuffer
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.joda.time._
import jetbrains.buildServer.users.SUser

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

		it("returns 2 when start is yesterday and end is anytime today, i.e., should treat today as a whole day") {
			val start 	= new DateTime(2010,6,7,0,0,0,0)
			val end 	= new DateTime(2010,6,8,15,6,36,700)

			val interval = new Interval(start, end)

			val model = new ReleasesModel(null, interval, new Instant)

			model.getIntervalInDays must equal(2)
		}

		it("works when interval contains months") {
			val start 	= new DateTime(2010,3,9,8,39,0,0)
			val end 	= new DateTime(2010,5,11,8,3,17,700)

			val interval = new Interval(start, end)

			val model = new ReleasesModel(null, interval, new Instant)

			model.getIntervalInDays must equal(63)
		}

		it("returns zero when interval is null") {
			val model = new ReleasesModel(null, null, null)

			model.getIntervalInDays must equal(0)
		}
	}

	describe("getToday") {
		it("is a string representing the start of the interval") {
			given_today_is(firstBikiniDisplayedInParis.plusDays(1))
			given_a_one_day_interval_from_the_day_before_the_first_bikini_to_the_day_of_the_first_bikini

			model.getToday must equal("Tue, 04 Jun 1946")
		}
	}

	describe("getIntervalStart") {
		it("is a string representing the start of the interval") {
			given_a_one_day_interval_from_the_day_before_the_first_bikini_to_the_day_of_the_first_bikini

			model.getIntervalStart must equal("Sun, 02 Jun 1946")
		}
	}

	describe("getIntervalEnd") {
		it("is a string representing the end of the interval") {
			given_today_is(firstBikiniDisplayedInParis.plusDays(1))
			given_a_one_day_interval_from_the_day_before_the_first_bikini_to_the_day_of_the_first_bikini
			
			model.getIntervalEnd must equal("Mon, 03 Jun 1946")
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
		it("returns a list of errors") {
			model = new ReleasesModel(null, null, null)

			model.addError("1. Phil Murphy's hair.")
			model.addError("2. The pedestrianisation of Norwich city centre.")
			
			model.getErrors.size must equal(2)
		}

		it("returns empty list when there are no errors") {
			model = new ReleasesModel(null, null, null)
			model.getErrors.size must equal(0)
		}
	}

	describe("getIntervalString") {
		it("returns empty when interval is null") {
			model = new ReleasesModel(null, null, null)

			model.getIntervalString must equal("")
		}
	}

	describe("rickrolling") {
		it("The Crack is okay") {
			model = new ReleasesModel(null, null, rollTimeStart)

			val phil = newFakeUser("phil.murphy@7digital.com")

			model.getRickrollable(phil) must be(true)
		}

		it("DRough is okay") {
			model = new ReleasesModel(null, null, rollTimeStart)

			val dan = newFakeUser("dan.rough@7digital.com")

			model.getRickrollable(dan) must be(true)
		}

		it("Anyone else is not okay") {
			model = new ReleasesModel(null, null, rollTimeStart)

			val anyoneElse = newFakeUser("(_xxx_xxx_xxx_)")

			model.getRickrollable(anyoneElse) must be(false)
		}

		it("only happens between 13:37 and 13:59 GMT") {
			val before 	= rollTimeStart.minus(minutes(1).toStandardDuration)
			val during 	= rollTimeStart.plus(minutes(1).toStandardDuration)
			val after 	= rollTimeEnd.plus(minutes(1).toStandardDuration)

			val rickrollableUser = newFakeUser("phil.murphy@7digital.com")

			model = new ReleasesModel(null, null, before)
			model.getRickrollable(rickrollableUser) must be(false)

			model = new ReleasesModel(null, null, rollTimeStart)
			model.getRickrollable(rickrollableUser) must be(true)

			model = new ReleasesModel(null, null, during)
			model.getRickrollable(rickrollableUser) must be(true)

			model = new ReleasesModel(null, null, rollTimeEnd)
			model.getRickrollable(rickrollableUser) must be(false)

			model = new ReleasesModel(null, null, after)
			model.getRickrollable(rickrollableUser) must be(false)
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

	private def newFakeUser(email : String) = {
		val result : SUser = mock(classOf[SUser])
		stub(result.getEmail).toReturn(email)

		result
	}

	private var model : ReleasesModel = null
	private val firstBikiniDisplayedInParis = new DateMidnight(1946, JUNE, 3, UTC)
	private var today : DateTime = null
	private val DEFAULT_TODAY = new DateTime
	private val threeHours = new Period(hours(3))
	private val oneDay = new Period(days(1))
	private val twoDays = new Period(days(2))
	private val NEWLINE = System.getProperty("line.separator")
	private val rollTimeStart = new DateTime(2010, 6, 9, 13, 37, 0, 1, DateTimeZone.UTC).toInstant
	private val rollTimeEnd = new DateTime(2010, 6, 9, 14, 00, 0, 1, DateTimeZone.UTC).toInstant
}