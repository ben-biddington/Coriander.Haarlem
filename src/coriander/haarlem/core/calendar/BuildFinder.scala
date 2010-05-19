package coriander.haarlem.core.calendar

import coriander.haarlem.core.Convert._
import jetbrains.buildServer.serverSide.{SBuildType, SFinishedBuild, ProjectManager}
import org.joda.time.{DateTimeZone, DateTime, Interval}
import org.joda.time.Days._

class BuildFinder(val projectManager : ProjectManager) extends IBuildFinder {
	
	def last(howMany : Int, options : FilterOptions) : List[SFinishedBuild] = {
		println("xxx")
		allBuilds(options.filter).sort(byNewestFirst).slice(0, howMany)
	}
	
	def find(options : FilterOptions) = {
		val all = allBuilds(options.filter)
		
		if (options.interval != null) filterByInterval(all, options.interval) else all
	}

	private def filterByInterval(
		builds: List[SFinishedBuild],
		interval : Interval
	) : List[SFinishedBuild] = {

		validate(interval)

		builds.
			filter((build : SFinishedBuild) => {
				val finishedAtUTC = new DateTime(build.getFinishDate, DateTimeZone.UTC)
			
				interval contains(finishedAtUTC)
			}).
			sort(byNewestFirst)
	}

	private def allBuilds(by : SFinishedBuild => Boolean) : List[SFinishedBuild] = {
		
		toScalaList(projectManager.getAllBuildTypes).
			map(buildType => fullHistory(buildType)).
			flatten[SFinishedBuild].
			filter(by)
	}

	private def fullHistory(_of : SBuildType) =
		toScalaList(_of.getHistoryFull(true))

	private def validate(interval : Interval) = {
		var howMany = 31

		if (interval.toDuration.isLongerThan(days(howMany).toStandardDuration))
			throw new IllegalArgumentException(
				"Duration must be smaller than about <" + howMany + " days>"
			)
	}
	
	private val byNewestFirst = (left : SFinishedBuild, right: SFinishedBuild) =>
		new DateTime(left.getFinishDate).isAfter(new DateTime(right.getFinishDate))
}