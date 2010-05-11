package coriander.haarlem.models

import jetbrains.buildServer.serverSide.SFinishedBuild
import org.joda.time.{Instant, Interval}

class ReleasesModel(
	val builds : java.util.List[SFinishedBuild],
	val interval : Interval,
	val now : Instant
) {
	def getBuilds = builds 	
	def getInterval = interval
	def getNow = now
}