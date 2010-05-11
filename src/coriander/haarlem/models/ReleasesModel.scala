package coriander.haarlem.models

import jetbrains.buildServer.serverSide.SFinishedBuild
import org.joda.time.{Interval}

class ReleasesModel(val builds : java.util.List[SFinishedBuild], val interval : Interval) {
	def getBuilds = builds 	
	def getInterval = interval
}