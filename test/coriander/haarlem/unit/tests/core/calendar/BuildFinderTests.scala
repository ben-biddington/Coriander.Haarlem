package coriander.haarlem.unit.tests.core.calendar

import collection.mutable.ListBuffer
import coriander.haarlem.core.Convert
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{BeforeAndAfterEach, Spec}
import org.mockito.Mockito._
import org.mockito.Matchers._
import jetbrains.buildServer.users.User
import coriander.haarlem.core.calendar.{FilterOptions, BuildFinder}
import coriander.haarlem.core.calendar.FilterOptions._
import java.util.{ArrayList, Date}
import jetbrains.buildServer.messages.Status
import org.joda.time._
import jetbrains.buildServer.serverSide.{SRunningBuild, SBuildType, SFinishedBuild, ProjectManager}

class BuildFinderTests extends Spec with ShouldMatchers with BeforeAndAfterEach {
	override def beforeEach() {
		result = null
		buildTypes.clear
	}

	describe("BuildFinder") {
		it("queries the project manager for all build types") {
			given_no_finished_builds

			given_a_finder

			when_it_is_asked_to_find_sommit

			then_it_queries_the_project_manager_for_all_build_types
			then_the_result_is_empty
		}

		it("collects all successfully finished build instances from all build types") {
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

		it("includes failed builds") {
			given_a_build_type_with_history(aBuildThatFailed(fiveMinutesAgo))

			given_a_finder

			when_it_is_asked_to_find_sommit_in(theLastTenMinutes)

			result.length should equal(1)
		}

		it("includes builds that are currently running") {
			(pending)
			
			given_a_build_type_with_a_running_build

			given_a_finder

			when_it_is_asked_to_find_sommit

			result.length should equal(1)
		}

		it("ignores all completed builds when no interval supplied") {
			pending
		}

		it("returns builds sorted by date ascending, i.e., newest first") {
			pending
		}
	}

	private def given_a_finder {
		given_a_project_manager
		finder = new BuildFinder(projectManager)
	}
	
	private def given_a_project_manager {
		projectManager = mock(classOf[ProjectManager])

		when(projectManager.getAllBuildTypes).
		thenReturn(Convert.toJavaList(buildTypes.toList))
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

	private def then_it_queries_the_project_manager_for_all_build_types {
		verify(projectManager).getAllBuildTypes
	}

	private def then_the_result_is_empty {
		result.length should be(0)
	}

	private def newFakeFinishedBuild = {
		var oneHourAgo = new Instant().minus(Duration.standardHours(1))
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

	private def newFakeBuildType =  mock(classOf[SBuildType])

	private var projectManager 	: ProjectManager = null
	private var finder 			: BuildFinder = null
	private var result			: List[SFinishedBuild] = null
	private var buildTypes		: ListBuffer[SBuildType] = new ListBuffer[SBuildType]

	private lazy val now 			= new Instant
	private val tenMinutesAgo 		= new Instant().minus(Duration.standardMinutes(10))
	private val fiveMinutesAgo 		= new Instant().minus(Duration.standardMinutes(5))
	private val theLastTenMinutes 	= new Interval(tenMinutesAgo, now)
}
