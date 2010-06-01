package coriander.haarlem.core.calendar

import coriander.haarlem.core.Convert._
import org.joda.time.{DateTimeZone, DateTime, Interval}
import java.util.Date
import jetbrains.buildServer.serverSide.{BuildHistory, SFinishedBuild}

class BuildFinder(buildHistory : BuildHistory) extends IBuildFinder {
	
	def last(howMany : Int, options : FilterOptions) = {
		allBuilds(options.filter).sort(byNewestFirst).slice(0, howMany)
	}
	
	def find(options : FilterOptions) = {
		val all = allBuilds(options.filter)
		
		if (options.interval != null)
			filterByInterval(all, options.interval)
		else
			all
	}

	private def filterByInterval(builds : List[SFinishedBuild], interval : Interval) :
		List[SFinishedBuild] = {

		builds.
			filter(build => interval.contains(utc(build.getFinishDate))).
			sort(byNewestFirst)
	}

	private def allBuilds(by : SFinishedBuild => Boolean) : List[SFinishedBuild] = {
		val filter = if (null == by) passThrough else by

		val includeCancelled = true
		
		toScalaList(buildHistory.getEntries(includeCancelled)).filter(filter)
	}
	
	private val byNewestFirst = (left : SFinishedBuild, right: SFinishedBuild) =>
		new DateTime(left.getFinishDate).isAfter(new DateTime(right.getFinishDate))

	private val passThrough : SFinishedBuild => Boolean = b => true

	private def utc(when : Date) = new DateTime(when, DateTimeZone.UTC)	
}

class HistoryFilterOptionMatcher extends jetbrains.buildServer.util.ItemProcessor[SFinishedBuild] {
	def processItem(build : SFinishedBuild) : Boolean = {
		// See: http://www.jetbrains.net/devnet/message/5263906#5263906
		//
		// Method processEntries works as follows:
		//	- retrieves all data from the history table
		//	- for each entry creates build and passes it into the item processor
		//	- then if item processor returns false, stops processing
		//
		// the only problem is the first part -- fetching everything.
		true
	}
}