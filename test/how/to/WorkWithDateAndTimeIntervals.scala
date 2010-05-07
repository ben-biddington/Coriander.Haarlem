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

		it("start and end is always UTC") {
			val start : DateTime = theLastTenMinutes.getStart
			val end : DateTime = theLastTenMinutes.getEnd

			val startUTC = start.toDateTime(DateTimeZone.UTC)
			val endUTC = end.toDateTime(DateTimeZone.UTC)

			startUTC should equal(start)
			endUTC should equal(end)
		}

		it("when DateTime is converted to Date it becomes local, unless DateTimeZone is supplied") {
			val date : Date = now.toDate

			new DateTime(date).getZone should not equal(DateTimeZone.UTC)
			new DateTime(date, DateTimeZone.UTC).getZone should equal(DateTimeZone.UTC)
		}
	}

	describe("Instant") {
		it("is always local DateTime") {
			(new Instant).toDateTime.getZone should not be(DateTimeZone.UTC)
		}

		it("toDate returns local") {
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

	private lazy val now = new Instant()
	private var interval : Interval = null
	private val tenMinutesAgoToNow = now.plus(Duration.standardMinutes(-10))
	private val theLastTenMinutes = new Interval(tenMinutesAgoToNow, now)
	private val fiveMinutesAgo = now.minus(Duration.standardMinutes(5))
	private val fifteenMinutesAgo = now.minus(Duration.standardMinutes(15))
	private val tomorrow = now.plus(Duration.standardDays(1))

}