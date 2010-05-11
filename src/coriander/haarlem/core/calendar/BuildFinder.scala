package coriander.haarlem.core.calendar

import coriander.haarlem.core.Convert
import jetbrains.buildServer.serverSide.{SBuildType, SFinishedBuild, ProjectManager}
import org.joda.time.{DateTimeZone, DateTime, Interval}
import org.joda.time.Days._

class BuildFinder(val projectManager : ProjectManager) extends IBuildFinder {
	def find() : List[SFinishedBuild] = find(FilterOptions.ALL);
	
	def find(options : FilterOptions) =
		if (options.interval != null) allBuildsIn(options.interval) else allBuilds

	private def allBuildsIn(interval : Interval) : List[SFinishedBuild] = {
		validate(interval)

		allBuilds.
			filter((build : SFinishedBuild) => {
				val finishedAtUTC = new DateTime(build.getFinishDate, DateTimeZone.UTC)
			
				interval contains(finishedAtUTC)
			}).
			sort((left, right) =>
				new DateTime(left.getFinishDate).isAfter(new DateTime(right.getFinishDate))
			)
	}

	private def allBuilds : List[SFinishedBuild] = {
		val allBuildTypes = Convert.toScalaList(projectManager.getAllBuildTypes);
		
		allBuildTypes.map(buildType => 
			fullHistory(buildType)).
			flatten[SFinishedBuild]
	}

	private def fullHistory(_of : SBuildType) =
		Convert.toScalaList(_of.getHistoryFull(true))

	private def validate(interval : Interval) = {
		var howMany = 31
		if (interval.toDuration.isLongerThan(days(howMany).toStandardDuration))
			throw new IllegalArgumentException(
				"Duration must be smaller than about <" + howMany + " days>"
			)
	}
}