package coriander.haarlem.unit.tests.core.astley

import org.scalatest.matchers.MustMatchers
import org.mockito.Mockito._
import org.scalatest.{BeforeAndAfterEach, Spec}
import jetbrains.buildServer.users.SUser
import coriander.haarlem.core.astley.Rick
import org.joda.time.{Instant, DateTimeZone, DateTime}
import org.joda.time.Minutes._

class RickTests extends Spec
	with MustMatchers
	with BeforeAndAfterEach {
	
	describe("rolling") {
		it("only happens between 13:37 and 13:59 GMT") {
			val before 	= rollTimeStart.minus(minutes(1).toStandardDuration)
			val during 	= rollTimeStart.plus(minutes(1).toStandardDuration)
			val after 	= rollTimeEnd.plus(minutes(1).toStandardDuration)

			val plonker = newFakeUser("phil.murphy@7digital.com");

			rickroll(plonker, before) must be(false)
			rickroll(plonker, rollTimeStart) must be(true)
			rickroll(plonker, during) must be(true)
			rickroll(plonker, rollTimeEnd) must be(false)
			rickroll(plonker, after) must be(false)
		}

		it("The Crack is okay") {	
			val phil = newFakeUser("phil.murphy@7digital.com")

			rick.rollable(phil, rollTimeStart) must be(true)
		}

		it("DRough is okay") {
			val dan = newFakeUser("dan.rough@7digital.com")

			rick.rollable(dan, rollTimeStart) must be(true)
		}

		it("anyone else is not okay") {
			val anyoneElse = newFakeUser("(_xxx_xxx_xxx_)")

			rick.rollable(anyoneElse, rollTimeStart) must be(false)
		}

		it("returns false with no plonkers") {
			new Rick(List()).rollable(newFakeUser("phil.murphy@7digital.com"), now) must be(false)
		}
	}

	private def rickroll(who : SUser, when : Instant) = {
		rick.rollable(who, when)
	}

	private def newFakeUser(email : String) = {
		val result : SUser = mock(classOf[SUser])
		stub(result.getEmail).toReturn(email)
		stub(result.getUsername).toReturn(email)

		result
	}
	
	private val rick = newRick

	private def newRick = {
		new Rick(List(
			"phil.murphy@7digital.com",
			"dan.rough@7digital.com",
			"ben.biddington@7digital.com"
		))
	}

	private val now = new Instant
	private val rollTimeStart = new DateTime(2010, 6, 9, 13, 37, 0, 1, DateTimeZone.UTC).toInstant
	private val rollTimeEnd = new DateTime(2010, 6, 9, 14, 00, 0, 1, DateTimeZone.UTC).toInstant
}