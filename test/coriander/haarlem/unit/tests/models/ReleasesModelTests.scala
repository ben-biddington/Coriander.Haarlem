package coriander.haarlem.unit.tests.models

import org.scalatest.{Spec, BeforeAndAfterEach}
import org.scalatest.matchers.{MustMatchers}
import coriander.haarlem.models.ReleasesModel
import org.joda.time.Days._
import org.joda.time.{DateMidnight, Interval}
import org.joda.time.DateTimeConstants._
import org.joda.time.DateTimeZone._
import jetbrains.buildServer.serverSide.SFinishedBuild
import coriander.haarlem.core.Convert._
import collection.mutable.ListBuffer
import org.mockito.Mockito._
import org.mockito.Matchers._

class ReleasesModelTests extends Spec
	with MustMatchers
	with BeforeAndAfterEach {

	override def beforeEach() {
		model = null
	}

	describe("getIntervalInDays") {
		it("returns the same number of days as the interval supplied") { 
			given_a_two_day_interval_from_two_days_before_the_first_bikini

			val numberOfDays = model.getIntervalInDays

			numberOfDays must equal(2)
		}
	}

	describe("getIntervalStart") {
		it("is a string representing the start of the interval") {
			given_a_two_day_interval_from_two_days_before_the_first_bikini

			model.getIntervalStart must equal("01 Jun 1946")
		}
	}

	describe("getIntervalEnd") {
		it("is a string representing the end of the interval") {
			given_a_two_day_interval_from_two_days_before_the_first_bikini
			
			model.getIntervalEnd must equal("03 Jun 1946")
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

	private def given_zero_builds() {
		given_builds(0)
	}

	private def given_builds(howMany : Int) {
		val theList = (0 until howMany).map(count => mock(classOf[SFinishedBuild]))
		
		model = new ReleasesModel(toJavaList(theList), twoDays, firstBikiniDisplayedInParis)
	}

	private def given_a_two_day_interval_from_two_days_before_the_first_bikini() {
		model = new ReleasesModel(null, twoDays, firstBikiniDisplayedInParis)
	}

	private var model : ReleasesModel = null
	private lazy val firstBikiniDisplayedInParis = new DateMidnight(1946, JUNE, 3, UTC).toInstant
	private lazy val twoDays = new Interval(days(2).toPeriod, firstBikiniDisplayedInParis)
}