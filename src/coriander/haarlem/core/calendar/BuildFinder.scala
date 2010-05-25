package coriander.haarlem.core.calendar

import coriander.haarlem.core.Convert._
import org.joda.time.{DateTimeZone, DateTime, Interval}
import org.joda.time.Days._
import java.util.Date
import jetbrains.buildServer.serverSide.{BuildHistory, SFinishedBuild}

class BuildFinder(buildHistory : BuildHistory) extends IBuildFinder {
	
	def last(howMany : Int, options : FilterOptions) = {
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
			filter(build => interval.contains(utc(build.getFinishDate))).
			sort(byNewestFirst)
	}

	private def allBuilds(by : SFinishedBuild => Boolean) : List[SFinishedBuild] = {
		val filter = if (null == by) passThrough else by

		val includeCancelled = true
		
		toScalaList(buildHistory.getEntries(includeCancelled)).filter(filter)
	}

	private def validate(interval : Interval) = {
		var howMany = 31

		if (interval.toDuration.isLongerThan(days(howMany).toStandardDuration))
			throw new IllegalArgumentException(
				"Duration must be smaller than about <" + howMany + " days>"
			)
	}
	
	private val byNewestFirst = (left : SFinishedBuild, right: SFinishedBuild) =>
		new DateTime(left.getFinishDate).isAfter(new DateTime(right.getFinishDate))

	private val passThrough : SFinishedBuild => Boolean = b => true

	private def utc(when : Date) = new DateTime(when, DateTimeZone.UTC)	
}