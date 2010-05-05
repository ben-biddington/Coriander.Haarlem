package coriander.haarlem.core.calendar

import jetbrains.buildServer.serverSide.{SFinishedBuild, ProjectManager}
import coriander.haarlem.core.Convert

class BuildFinder(val projectManager : ProjectManager) {
	def find() : List[SFinishedBuild] = allSuccessfulBuilds;

	private def allSuccessfulBuilds : List[SFinishedBuild] = {
		val allBuildTypes = Convert.toScalaList(projectManager.getAllBuildTypes);

		val result = allBuildTypes.
			map(_.getLastSuccessfullyFinished).
			filter(_ != null)

		result
	}
}