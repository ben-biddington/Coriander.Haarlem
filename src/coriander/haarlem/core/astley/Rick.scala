package coriander.haarlem.core.astley

import jetbrains.buildServer.users.SUser
import org.joda.time.{Instant, DateTimeZone}

trait RickrollEligibility {
	def rollable(user : SUser, now : Instant) : Boolean
}

class Rick(plonkers : List[String]) extends RickrollEligibility {
	def rollable(user : SUser, now : Instant) : Boolean = {
		theTimeIsOkay(now) && isPlonker(user)
	}

	private def isPlonker(user: SUser) : Boolean = {
		plonkers.contains(user.getEmail.toLowerCase) ||
		plonkers.contains(user.getUsername.toLowerCase)
	}

	private def theTimeIsOkay(now : Instant) = {
		var dateTime = now.toDateTime(DateTimeZone.UTC)

		dateTime.getHourOfDay == 13 && (37 until 59).contains(dateTime.getMinuteOfHour)
	}
}