package coriander.haarlem.unit.tests.core.calendar

import collection.mutable.ListBuffer
import coriander.haarlem.core.calendar.BuildFinder
import coriander.haarlem.core.Convert
import jetbrains.buildServer.serverSide.{SBuildType, SFinishedBuild, ProjectManager}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.{BeforeAndAfterEach, Spec}
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.joda.time.{DateTime, Instant}
import java.util.Date
import jetbrains.buildServer.users.User

class BuildFinderTests extends Spec with ShouldMatchers with BeforeAndAfterEach {
	override def beforeEach() {
		result = null
	}

	describe("BuildFinder") {
		it("Queries the project manager for all build types") {
			given_no_finished_builds

			given_a_finder

			when_it_is_asked_to_find_sommit

			then_it_queries_the_project_manager_for_all_build_types
			then_the_result_is_empty
		}

		it("Collects all successfully finished build instances from all build types") {
			given_a_build_type_with_a_finished_build
			given_a_build_type_with_a_finished_build
			
			given_a_finder

			when_it_is_asked_to_find_sommit

			result.length should equal(2)
		}

		it("Only returns builds completed within specified date range") {
			(pending)
			
			var oneYearAgo = new Instant().toDateTime.plusYears(-1).toDate
			given_a_build_type_with_a(newFakeFinishedBuildThatFinishedAt(oneYearAgo))

			given_a_finder

			when_it_is_asked_to_find_sommit

			result.length should equal(0)
		}

		it("Ignores failed builds") (pending)

		it("Ignores builds that are currently running") (pending)
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

		when(newBuildType.getLastSuccessfullyFinished).
		thenReturn(null)

		buildTypes += newBuildType
	}

	private def given_a_build_type_with_a_finished_build {
		val newBuildType = newFakeBuildType
		val fakeFinishedBuild = newFakeFinishedBuild

		when(newBuildType.getLastSuccessfullyFinished).
		thenReturn(fakeFinishedBuild)

		buildTypes += newBuildType
	}

	private def given_a_build_type_with_a(finishedBuild : SFinishedBuild) {
		when(newFakeBuildType.getLastSuccessfullyFinished).
		thenReturn(finishedBuild)

		buildTypes += newFakeBuildType
	}

	private def when_it_is_asked_to_find_sommit {
		require(finder != null, "Can't proceed without the CUT (there is no finder).")

		result = finder.find()
	}

	private def then_it_queries_the_project_manager_for_all_build_types {
		verify(projectManager).getAllBuildTypes
	}

	private def then_the_result_is_empty {
		result.length should be(0)
	}

	private def newFakeFinishedBuild = {
		var oneHourAgo = new Instant().toDateTime.plusHours(-1)
		newFakeFinishedBuildThatFinishedAt(oneHourAgo.toDate)
	}

	private def newFakeFinishedBuildThatFinishedAt(when : DateTime) : FakeFinishedBuild =
		newFakeFinishedBuildThatFinishedAt(when.toDate)

	private def newFakeFinishedBuildThatFinishedAt(when : Date) = {
		var result : FakeFinishedBuild = mock(classOf[FakeFinishedBuild])
		result.setFinishDate(new Instant().toDate)
		result
	}

	private def newFakeBuildType =  mock(classOf[SBuildType])

	private var projectManager 	: ProjectManager = null
	private var finder 			: BuildFinder = null
	private var result			: List[SFinishedBuild] = null
	private var buildTypes		: ListBuffer[SBuildType] = new ListBuffer[SBuildType]
}

abstract class FakeFinishedBuild extends SFinishedBuild {
	def getFinishDate() : Date = thatFinishedAt
	def setFinishDate(when : Date) = thatFinishedAt = when

	private var thatFinishedAt : Date = null
}
