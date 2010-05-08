package coriander.haarlem.unit.tests.core.calendar

import collection.mutable.ListBuffer
import coriander.haarlem.core.Convert
import jetbrains.buildServer.serverSide.{SBuildType, SFinishedBuild, ProjectManager}
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

class BuildFinderTests extends Spec with ShouldMatchers with BeforeAndAfterEach {
	override def beforeEach() {
		result = null
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
			val tenMinutesAgo = new Instant().minus(Duration.standardMinutes(20))
			val theInterval = new Interval(tenMinutesAgo, new Instant())

			given_a_build_type_with_a(aBuildThatFinished(tenMinutesAgo))

			given_a_finder

			when_it_is_asked_to_find_sommit_in(theInterval)

			result.length should equal(1)
		}

		it("returns all finished builds within range, NOT just the last sucessful") (pending)

		it("ignores failed builds") (pending)

		it("ignores builds that are currently running") (pending)

		it("ignores all completed builds when no interval supplied") {
			pending
		}

		it("May be better to implement a fake build") {
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

	private def given_a_build_type_with_a(finishedBuild : SFinishedBuild) {
		var buildHistory = new ArrayList[SFinishedBuild]()
		buildHistory.add(finishedBuild)

		val newBuildType = newFakeBuildType

		when(newBuildType.getHistoryFull(true)).
		thenReturn(buildHistory)

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

	private def aBuildThatFinished(when : Instant) = {
		var result = mock(classOf[SFinishedBuild])

		var toDate = when.toDate

		stub(result.getFinishDate).toReturn(toDate)
		stub(result.getBuildStatus).toReturn(Status.NORMAL)

		result
	}

	private def newFakeBuildType =  mock(classOf[SBuildType])

	private var projectManager 	: ProjectManager = null
	private var finder 			: BuildFinder = null
	private var result			: List[SFinishedBuild] = null
	private var buildTypes		: ListBuffer[SBuildType] = new ListBuffer[SBuildType]
}
