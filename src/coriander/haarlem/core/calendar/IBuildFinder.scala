package coriander.haarlem.core.calendar

import jetbrains.buildServer.serverSide.SFinishedBuild

trait IBuildFinder {
	def find(options : FilterOptions) : List[SFinishedBuild]
}