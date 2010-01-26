package coriander.haarlem.core.scheduling

import java.util.{GregorianCalendar, Calendar}

class SystemClock extends Clock {
	def getHour = new GregorianCalendar().get(Calendar.HOUR_OF_DAY)
}