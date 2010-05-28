package coriander.haarlem.unit.tests.core.calendar

import collection.mutable.ListBuffer
import coriander.haarlem.core.Convert._
import org.scalatest.{BeforeAndAfterEach, Spec}
import org.mockito.Mockito._
import coriander.haarlem.core.calendar.FilterOptions._
import java.util.{Date}
import jetbrains.buildServer.messages.Status
import org.joda.time._
import org.joda.time.Minutes._
import org.joda.time.Days._
import org.joda.time.Hours._
import org.scalatest.matchers.{MustMatchers, ShouldMatchers}
import coriander.haarlem.core.calendar.{FilterOptions, BuildFinder}
import jetbrains.buildServer.serverSide._

class BuildFinderTests
	extends Spec
	with ShouldMatchers
	with MustMatchers
	with BeforeAndAfterEach {
	override def beforeEach() {
		result = null
		finishedBuilds.clear
	}

	describe("BuildFinder.find") {
		it("queries the project manager for all build types") {
			given_no_finished_builds

			given_a_finder

			when_it_is_asked_to_find_sommit

			then_it_queries_the_build_history_for_all_build_including_cancelled
			then_the_result_is_empty
		}

		it("only returns builds completed within specified date range") {
			given(aBuildThatFinished(fiveMinutesAgo))

			given_a_finder

			when_it_is_asked_to_find_sommit_in(theLastTenMinutes)

			result.length should equal(1)
		}

		it("collects failed builds too") {
			given(aBuildThatFailed(fiveMinutesAgo))

			given_a_finder

			when_it_is_asked_to_find_sommit_in(theLastTenMinutes)

			result.length should equal(1)
		}

		it("returns builds sorted by date descending, i.e., newest first") {
			given_a_build_history(
				aBuildThatFinished(tenMinutesAgo),
				aBuildThatFinished(yesterday),
				aBuildThatFinished(fiveMinutesAgo)
			)

			given_a_finder

			when_it_is_asked_to_find_sommit_in(theLastWeek)

			toUtc(result.first.getFinishDate) should equal(fiveMinutesAgo)
			toUtc(result.last.getFinishDate) should equal(yesterday)
		}

		it("returns and empty list (not null) when nothing matching found") {
			given_a_build_history(aBuildThatFinished(yesterday))

			given_a_finder

			when_it_is_asked_to_find_sommit_in(theLastTenMinutes)

			result.length should equal(0)
		}
	}

	describe("BuildFinder.last") {
		it("returns empty list when given zero") {
			given_a_build_history(
				aBuildThatFinished(tenMinutesAgo),
				aBuildThatFinished(fiveMinutesAgo)
			)

			given_a_finder

			when_it_is_asked_to_find_the_last(0)

			result.length should equal(0)
		}

		it("returns at most the supplied count") {
			given_a_build_history(
				aBuildThatFinished(tenMinutesAgo),
				aBuildThatFinished(fiveMinutesAgo)
			)

			given_a_finder

			when_it_is_asked_to_find_the_last(1)

			result.length should equal(1)
		}

		it("returns the builds sorted desc (newest first)") {
			given_a_build_history(
				aBuildThatFinished(tenMinutesAgo),
				aBuildThatFinished(yesterday),
				aBuildThatFinished(fiveMinutesAgo)
			)

			given_a_finder

			when_it_is_asked_to_find_the_last(5)

			toUtc(result.first.getFinishDate) should equal(fiveMinutesAgo)
			toUtc(result.last.getFinishDate) should equal(yesterday)
		}
	}

	describe("The optional filter") {
		it("invokes my filter") (pending)
	}

	private def given_a_finder {                                                                  
		given_a_build_history
		finder = new BuildFinder(buildHistory)
	}
	
	private def given_a_build_history {
		buildHistory = mock(classOf[BuildHistory])
		stub(buildHistory.getEntries(true)).toReturn(toJavaList(finishedBuilds.toList))
	}

	private def given_no_finished_builds {
		finishedBuilds.clear
	}

	private def given(build : SFinishedBuild) {
		finishedBuilds += build
	}

	private def given_a_build_history(buildHistory : SFinishedBuild*) {
		buildHistory.foreach(b => finishedBuilds += b)
	}

	private def when_it_is_asked_to_find_sommit {
		require(finder != null, "Can't proceed without the CUT (there is no finder).")

		result = finder.find(FilterOptions.NONE)
	}

	private def when_it_is_asked_to_find_sommit_in(interval : Interval) {
		require(finder != null, "Can't proceed without the CUT (there is no finder).")

		result = finder.find(in(interval))
	}

	private def when_it_is_asked_to_find_the_last(howMany : Int) {
		require(finder != null, "Can't proceed without the CUT (there is no finder).")

		result = finder.last(howMany, FilterOptions.NONE)
	}

	private def then_it_queries_the_build_history_for_all_build_including_cancelled {
		verify(buildHistory).getEntries(true)
	}

	private def then_the_result_is_empty {
		result.length should be(0)
	}

	private def then_only_builds_with_description_or_name_matching_are_returned() {
		throw new Exception("Not implemented properly -- moved straightt from release controller test")
		
		require(result != null, "Unable to work with no result")

//		toScalaList(result.builds).foreach(build =>
//			assertTrue(
//				"The build description <" + build.getBuildDescription + "> " +
//				"does not contain <" + matching + ">",
//				build.getBuildDescription.contains(matching)
//			)
//		)
	}

	private def newFakeFinishedBuild = {
		var oneHourAgo = new Instant().minus(hours(1).toStandardDuration)
		aBuildThatFinished(oneHourAgo)
	}

	private def newFakerRunningBuild = mock(classOf[SRunningBuild])
	private def aBuildThatFinished(when : Instant) = newBuild(when, Status.NORMAL)
	private def aBuildThatFailed(when : Instant) = newBuild(when, Status.FAILURE)

	private def newBuild(thatFinishedAt : Instant, withStatus : Status) = {
		var result = mock(classOf[SFinishedBuild])

		var toDate = thatFinishedAt.toDate

		stub(result.getFinishDate).toReturn(toDate)
		stub(result.getBuildStatus).toReturn(withStatus)

		result
	}

	private def toUtc(date : Date) : DateTime = new DateTime(date, DateTimeZone.UTC)

	private def newFakeBuildType =  mock(classOf[SBuildType])

	private var buildHistory 	: BuildHistory = null
	private var finder 			: BuildFinder = null
	private var result			: List[SFinishedBuild] = null
	private var finishedBuilds	: ListBuffer[SFinishedBuild] = new ListBuffer[SFinishedBuild]

	private lazy val now 			= new Instant
	private val tenMinutesAgo 		= new Instant().minus(minutes(10).toStandardDuration)
	private val fiveMinutesAgo 		= new Instant().minus(minutes(5).toStandardDuration)
	private val yesterday 			= new Instant().minus(days(1).toStandardDuration)
	private val sevenDaysAgo 		= new Instant().minus(days(7).toStandardDuration)
	private val theLastTenMinutes 	= new Interval(tenMinutesAgo, now)
	private val theLastWeek 		= new Interval(sevenDaysAgo, now)
}
