package coriander.haarlem.models

import jetbrains.buildServer.serverSide.SFinishedBuild
import org.joda.time.Days._
import org.joda.time._
import collection.mutable.ListBuffer
import coriander.haarlem.core.Convert._

class ReleasesModel(
	val builds : java.util.List[SFinishedBuild],
	val interval : Interval,
	val now : Instant
) {
	def getBuilds 				= builds
	def getInterval 			= interval
	def getIntervalString 		= if (interval != null) interval.getStart + " -> " + interval.getEnd else ""
	def getNow 					= now
	def getToday 				= now.toDateTime.toLocalDateTime.toString("EEE, dd MMM yyyy")
	def getIntervalStart 		= interval.getStart.toLocalDateTime.toString("EEE, dd MMM yyyy")
	def getIntervalStartDay 	= interval.getStart.toLocalDateTime.getDayOfMonth
	def getIntervalStartMonth 	= interval.getStart.toLocalDateTime.toString("MMM")
	def getIntervalEndDay 		= interval.getEnd.toLocalDateTime.getDayOfMonth
	def getIntervalEndMonth 	= interval.getEnd.toLocalDateTime.toString("MMM")
	def getIntervalEnd 			= selectIntervalEnd
	def getIntervalInDays 		= if (interval != null) Math.max(getTotalDays(interval), 1) else 0
	def getCount				= builds.size
	def getDayOfMonth			= now.toDateTime.toLocalDateTime.getDayOfMonth
	def getMonthOfYear			= now.toDateTime.toLocalDateTime.toString("MMM")
	def getErrors 				= toJavaList(errors)
	def addError(what : String) = errors += what
	def clearErrors 			= errors.clear
	def getRickrollable 		= rickroll
	def setRickrollable(on : Boolean) = rickroll = on
	def getTitle 				= title
	def getDescription 			= description
	
	private def getTotalDays(interval : Interval) = {
		val days = daysIn(interval).getDays
		val hours = interval.toPeriod.getHours

		val totalHours = days*24 + hours

		Math.ceil(totalHours.toFloat/24).intValue
	}

	private def selectIntervalEnd : String = {
		if (
			interval.getEnd.getDayOfYear == now.get(DateTimeFieldType.dayOfYear) &&
			interval.getEnd.getYear == now.get(DateTimeFieldType.year)
		) "Today"
		else interval.getEnd.toLocalDateTime.toString("EEE, dd MMM yyyy")
	}

	private var errors 		= new ListBuffer[String]()
	private var rickroll 	= false
	private val NEWLINE 	= System.getProperty("line.separator")
	var title 				= "Recent releases search"
	var description 		= "Search for builds across all projects"
}