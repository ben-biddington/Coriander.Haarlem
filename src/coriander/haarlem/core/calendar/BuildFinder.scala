package coriander.haarlem.core.calendar

import jetbrains.buildServer.serverSide.{SFinishedBuild, SBuildType, ProjectManager}
import collection.mutable.ListBuffer
import coriander.haarlem.core.Convert

class BuildFinder(val projectManager : ProjectManager) {
	def find() : List[SFinishedBuild] = allSuccessfulBuilds;

	private def allSuccessfulBuilds : List[SFinishedBuild] = {
		val allBuildTypes = Convert.toScalaList(projectManager.getAllBuildTypes);

		allBuildTypes.
			map(_.getLastSuccessfullyFinished)
	}
}