package coriander.haarlem.core.calendar

import coriander.haarlem.core.Convert
import jetbrains.buildServer.serverSide.{SBuildType, SFinishedBuild, ProjectManager}
import jetbrains.buildServer.messages.Status
import org.joda.time.{DateTimeZone, DateTime, Interval}

class BuildFinder(val projectManager : ProjectManager) {
	def find() : List[SFinishedBuild] = find(FilterOptions.ALL);
	
	def find(options : FilterOptions) : List[SFinishedBuild] = {
		if (options.interval != null) allBuildsIn(options.interval) else allSuccessfulBuilds
	}

	private def allBuildsIn(interval : Interval) : List[SFinishedBuild] = {
		allSuccessfulBuilds.filter((build : SFinishedBuild) => {
			val finishedAtUTC = new DateTime(build.getFinishDate, DateTimeZone.UTC)
			
			interval contains(finishedAtUTC)
		})
	}

	private def allSuccessfulBuilds : List[SFinishedBuild] = {
		val allBuildTypes = Convert.toScalaList(projectManager.getAllBuildTypes);
		
		allBuildTypes.map(buildType => 
			fullHistory(buildType).filter(_.getBuildStatus == Status.NORMAL)).
			flatten[SFinishedBuild]
	}

	private def fullHistory(_of : SBuildType) =
		Convert.toScalaList(_of.getHistoryFull(true))
}

class FilterOptions(val interval : Interval)

object FilterOptions {
	def ALL = new FilterOptions(null)
	def in(interval : Interval) = new FilterOptions(interval)
}