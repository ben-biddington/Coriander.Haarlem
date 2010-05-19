package coriander.haarlem.core.calendar

import org.joda.time.Interval
import jetbrains.buildServer.serverSide.SFinishedBuild

class FilterOptions(val interval : Interval, val filter : SFinishedBuild => Boolean) {
	def this(interval : Interval) = this(interval, build => true)
	def this() = this(null, null)
}

object FilterOptions {
	val NONE = new FilterOptions(null, passThrough)
	def in(interval : Interval) = new FilterOptions(interval)
	private val passThrough : SFinishedBuild => Boolean = build => true
}