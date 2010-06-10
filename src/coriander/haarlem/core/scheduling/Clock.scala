package coriander.haarlem.core.scheduling

import org.joda.time.Instant

abstract class Clock {
	def getHour : Int
	def now : Instant
}