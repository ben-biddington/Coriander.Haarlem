package coriander.haarlem.core.scheduling

import java.util.{GregorianCalendar, Calendar}
import org.joda.time.Instant

class SystemClock extends Clock {
	def getHour = new GregorianCalendar().get(Calendar.HOUR_OF_DAY)
	def now = new Instant
}