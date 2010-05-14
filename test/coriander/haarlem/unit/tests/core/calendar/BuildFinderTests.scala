package coriander.haarlem.unit.tests.core.calendar

import collection.mutable.ListBuffer
import coriander.haarlem.core.Convert
import org.scalatest.{BeforeAndAfterEach, Spec}
import org.mockito.Mockito._
import coriander.haarlem.core.calendar.{BuildFinder}
import coriander.haarlem.core.calendar.FilterOptions._
import java.util.{Date}
import jetbrains.buildServer.messages.Status
import org.joda.time._
import org.joda.time.Minutes._
import org.joda.time.Days._
import org.joda.time.Hours._
import jetbrains.buildServer.serverSide.{SRunningBuild, SBuildType, SFinishedBuild, ProjectManager}
import org.scalatest.matchers.{MustMatchers, ShouldMatchers}

class BuildFinderTests
	extends Spec
	with ShouldMatchers
	with MustMatchers
	with BeforeAndAfterEach {
	override def beforeEach() {
		result = null
		buildTypes.clear
	}

	describe("BuildFinder.find") {
		it("queries the project manager for all build types") {
			given_no_finished_builds

			given_a_finder

			when_it_is_asked_to_find_sommit

			then_it_queries_the_project_manager_for_all_build_types
			then_the_result_is_empty
		}

		it("collects all build instances from all build types") {
			given_a_build_type_with_a_finished_build
			given_a_build_type_with_a_finished_build
			
			given_a_finder

			when_it_is_asked_to_find_sommit

			result.length should equal(2)
		}

		it("only returns builds completed within specified date range") {
			given_a_build_type_with_a(aBuildThatFinished(fiveMinutesAgo))

			given_a_finder

			when_it_is_asked_to_find_sommit_in(theLastTenMinutes)

			result.length should equal(1)
		}

		it("collects failed builds too") {
			given_a_build_type_with_history(aBuildThatFailed(fiveMinutesAgo))

			given_a_finder

			when_it_is_asked_to_find_sommit_in(theLastTenMinutes)

			result.length should equal(1)
		}

		it("returns builds sorted by date descending, i.e., newest first") {
			given_a_build_type_with_history(
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
			given_a_build_type_with_history(aBuildThatFinished(yesterday))

			given_a_finder

			when_it_is_asked_to_find_sommit_in(theLastTenMinutes)

			result.length should equal(0)
		}

		it("rejects an interval greater than about one month") {
			given_a_finder

			val thirtyTwoDaysAgo 	= new Instant().minus(days(32).toStandardDuration)
			val aBitOverAMonth 		= new Interval(thirtyTwoDaysAgo, now)

			intercept[IllegalArgumentException] {
				when_it_is_asked_to_find_sommit_in(aBitOverAMonth)
			}
		}
	}

	describe("BuildFinder.last") {
			it("returns empty list when given zero") {
				given_a_build_type_with_history(
					aBuildThatFinished(tenMinutesAgo),
					aBuildThatFinished(fiveMinutesAgo)
				)

				given_a_finder

				when_it_is_asked_to_find_the_last(0)

				result.length should equal(0)
			}

			it("returns at most the supplied count") {
				given_a_build_type_with_history(
					aBuildThatFinished(tenMinutesAgo),
					aBuildThatFinished(fiveMinutesAgo)
				)

				given_a_finder

				when_it_is_asked_to_find_the_last(1)

				result.length should equal(1)
			}

			it("returns the builds sorted desc (newest first)") {
				given_a_build_type_with_history(
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

	private def given_a_finder {                                                                  
		given_a_project_manager
		finder = new BuildFinder(projectManager)
	}
	
	private def given_a_project_manager {
		projectManager = mock(classOf[ProjectManager])

		stub(projectManager.getAllBuildTypes).
		toReturn(Convert.toJavaList(buildTypes.toList))
	}

	private def given_no_finished_builds {
		val newBuildType = newFakeBuildType

		stub(newBuildType.getLastSuccessfullyFinished).
		toReturn(null)

		stub(newBuildType.getHistoryFull(true)).
		toReturn(new java.util.ArrayList[SFinishedBuild])

		buildTypes += newBuildType
	}

	private def given_a_build_type_with_a_finished_build {
		given_a_build_type_with_a(newFakeFinishedBuild)
	}

	private def given_a_build_type_with_a_running_build {
		given_a_build_type_with_some(newFakerRunningBuild)
	}

	private def given_a_build_type_with_some(runningBuilds : SRunningBuild*) {
		val newBuildType = newFakeBuildType

		stub(newBuildType.getRunningBuilds).
		toReturn(Convert.toJavaList(runningBuilds.toList))

		buildTypes += newBuildType
	}

	private def given_a_build_type_with_a(finishedBuild : SFinishedBuild) {
		given_a_build_type_with_history(finishedBuild)
	}

	private def given_a_build_type_with_history(buildHistory : SFinishedBuild*) {
		val newBuildType = newFakeBuildType

		stub(newBuildType.getHistoryFull(true)).
		toReturn(Convert.toJavaList(buildHistory.toList))

		buildTypes += newBuildType
	}

	private def when_it_is_asked_to_find_sommit {
		require(finder != null, "Can't proceed without the CUT (there is no finder).")

		result = finder.find()
	}

	private def when_it_is_asked_to_find_sommit_in(interval : Interval) {
		require(finder != null, "Can't proceed without the CUT (there is no finder).")

		result = finder.find(in(interval))
	}

	private def when_it_is_asked_to_find_the_last(howMany : Int) {
		require(finder != null, "Can't proceed without the CUT (there is no finder).")

		result = finder.last(howMany)
	}

	private def then_it_queries_the_project_manager_for_all_build_types {
		verify(projectManager).getAllBuildTypes
	}

	private def then_the_result_is_empty {
		result.length should be(0)
	}

	private def newFakeFinishedBuild = {
		var oneHourAgo = new Instant().minus(hours(1).toStandardDuration)
		aBuildThatFinished(oneHourAgo)
	}

	private def newFakerRunningBuild = {
		var result = mock(classOf[SRunningBuild])
		result
	}

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

	private var projectManager 	: ProjectManager = null
	private var finder 			: BuildFinder = null
	private var result			: List[SFinishedBuild] = null
	private var buildTypes		: ListBuffer[SBuildType] = new ListBuffer[SBuildType]

	private lazy val now 			= new Instant
	private val tenMinutesAgo 		= new Instant().minus(minutes(10).toStandardDuration)
	private val fiveMinutesAgo 		= new Instant().minus(minutes(5).toStandardDuration)
	private val yesterday 			= new Instant().minus(days(1).toStandardDuration)
	private val sevenDaysAgo 		= new Instant().minus(days(7).toStandardDuration)
	private val theLastTenMinutes 	= new Interval(tenMinutesAgo, now)
	private val theLastWeek 		= new Interval(sevenDaysAgo, now)
}
