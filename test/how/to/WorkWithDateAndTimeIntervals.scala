package how.to

import org.scalatest.{Spec, BeforeAndAfterEach}
import org.scalatest.matchers.ShouldMatchers
import org.joda.time._
import java.util.Date

class WorkWithDateAndTimeIntervals extends Spec with ShouldMatchers with BeforeAndAfterEach  {
    override def beforeEach() {
		
	}

	describe("Interval") {
		it("start and end can be used to determine whether a date is in interval or not") {
			given(theLastTenMinutes)

			then_this_instant_is_out(fifteenMinutesAgo)
			then_this_instant_is_in(fiveMinutesAgo)
			then_this_instant_is_out(tomorrow)
		}

		it("start and end are always UTC") {
			theLastTenMinutes.getStart.getZone 	should equal(DateTimeZone.UTC)
			theLastTenMinutes.getEnd.getZone 	should equal(DateTimeZone.UTC)
		}
	}

	describe("Instant") {
		it("is always local DateTime") {
			now.toDateTime.getZone should be(myLocalTimeZone)
		}

		it("is always local Date") {
			getZone(now.toDate) should be(myLocalTimeZone)
		}

		it("can be converted to UTC") {
			now.toDateTime(DateTimeZone.UTC).getZone should equal(DateTimeZone.UTC)
		}
	}

	describe("java.util.Date") {
		it("yields its timezone by using a Calendar") {
			(pending)
		}
	}

	private def given(interval : Interval) = this.interval = interval
	
	private def then_this_instant_is_in(instant : Instant) = {
		isIn(instant, interval) should be(true)
	}

	private def then_this_instant_is_out(instant : Instant) = {
		isIn(instant, interval) should be(false)
	}

	private def isIn(instant : Instant, interval : Interval) : Boolean = {
		instant.isAfter(interval.getStart) && instant.isBefore(interval.getEnd)
	}

	private def getZone(date : Date) = new DateTime(date).getZone

	private lazy val now = new Instant()
	private var interval : Interval = null
	private val tenMinutesAgoToNow = now.plus(Duration.standardMinutes(-10))
	private val theLastTenMinutes = new Interval(tenMinutesAgoToNow, now)
	private val fiveMinutesAgo = now.minus(Duration.standardMinutes(5))
	private val fifteenMinutesAgo = now.minus(Duration.standardMinutes(15))
	private val tomorrow = now.plus(Duration.standardDays(1))
	private lazy val myLocalTimeZone = DateTimeZone.getDefault

}