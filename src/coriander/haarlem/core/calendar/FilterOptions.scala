package coriander.haarlem.core.calendar

import org.joda.time.Interval

class FilterOptions(val interval : Interval)

object FilterOptions {
	def ALL = new FilterOptions(null)
	def in(interval : Interval) = new FilterOptions(interval)
}