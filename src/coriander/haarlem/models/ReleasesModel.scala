package coriander.haarlem.models

import jetbrains.buildServer.serverSide.SFinishedBuild
import org.joda.time.{Instant, Interval}
import org.joda.time.Days._

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
	def getIntervalEnd 		= interval.getEnd.toLocalDateTime.toString("dd MMM yyyy")
	def getIntervalInDays 	= daysIn(interval).getDays
	def getCount			= builds.size
}