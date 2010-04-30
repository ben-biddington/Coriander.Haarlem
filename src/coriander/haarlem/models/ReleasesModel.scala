package coriander.haarlem.models

import jetbrains.buildServer.serverSide.SFinishedBuild

class ReleasesModel(val builds : java.util.List[SFinishedBuild]) {
	def getBuilds = builds 	
}