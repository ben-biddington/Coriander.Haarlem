package coriander.haarlem.models

import jetbrains.buildServer.serverSide.SFinishedBuild

class ReleasesModel(val builds : List[SFinishedBuild]) {
	def getBuilds = builds 	
}