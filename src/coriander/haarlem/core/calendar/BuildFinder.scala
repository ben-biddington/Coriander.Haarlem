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
			val finishedAt = new DateTime(build.getFinishDate, DateTimeZone.UTC)

			println("Finish date as recorded: <" + build.getFinishDate + ">")
			println("Finish date UTC: <" + finishedAt + ">")

			val from = interval.getStart//.toDateTime(DateTimeZone.UTC)
			var to = interval.getEnd//.toDateTime(DateTimeZone.UTC)

			println("Interval: <" + from + "> to <" + to + ">")
			println("Interval: <" + from.toDateTime(DateTimeZone.UTC) + "> to <" + to.toDateTime(DateTimeZone.UTC) + ">")

			finishedAt.isAfter(interval.getStart.toDateTime(DateTimeZone.UTC)) &&
			finishedAt.isBefore(interval.getEnd.toDateTime(DateTimeZone.UTC))
		})
	}

	private def allSuccessfulBuilds : List[SFinishedBuild] = {
		val allBuildTypes = Convert.toScalaList(projectManager.getAllBuildTypes);
		
		val result = allBuildTypes.
			map((buildType : SBuildType) => {
				fullHistory(buildType).filter((finishedBuild : SFinishedBuild) => {
					finishedBuild.getBuildStatus == Status.NORMAL
				})
			}).
			flatten[SFinishedBuild]

		println("(" + result.length + ") successful builds in total")

		result
	}

	private def fullHistory(_of : SBuildType) =
		Convert.toScalaList(_of.getHistoryFull(true))
}

class FilterOptions(val interval : Interval)

object FilterOptions {
	def ALL = new FilterOptions(null)
	def in(interval : Interval) = new FilterOptions(interval)
}