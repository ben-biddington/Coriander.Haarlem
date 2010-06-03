package coriander.haarlem.models

import jetbrains.buildServer.serverSide.SFinishedBuild
import org.joda.time.Days._
import org.joda.time._

class ReleasesModel(
	val builds : java.util.List[SFinishedBuild],
	val interval : Interval,
	val now : Instant
) {
	def getBuilds 			= builds 	
	def getInterval 		= interval
	def getNow 				= now
	def getToday 			= now.toDateTime.toLocalDateTime.toString("dd MMM yyyy")
	def getIntervalStart 	= interval.getStart.toLocalDateTime.toString("dd MMM yyyy")
	def getIntervalEnd 		= selectIntervalEnd
	def getIntervalInDays 	= Math.max(daysIn(interval).getDays, 1)
	def getCount			= builds.size
	def getDayOfMonth		= now.toDateTime.toLocalDateTime.getDayOfMonth
	def getMonthOfYear		= now.toDateTime.toLocalDateTime.toString("MMM")
	def getError			= now.toDateTime.toLocalDateTime.toString("MMM")

	private def selectIntervalEnd : String = {
		if (
			interval.getEnd.getDayOfYear == now.get(DateTimeFieldType.dayOfYear) &&
			interval.getEnd.getYear == now.get(DateTimeFieldType.year)
		) "Today"
		else interval.getEnd.toLocalDateTime.toString("dd MMM yyyy")
	}

	def addError(what : String) {
		error.append(what)
	}

	private lazy val error : StringBuffer = new StringBuffer()
}