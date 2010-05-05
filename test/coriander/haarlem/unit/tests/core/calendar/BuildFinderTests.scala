package coriander.haarlem.unit.tests.core.calendar

import org.scalatest.matchers.ShouldMatchers
import coriander.haarlem.core.calendar.BuildFinder
import org.mockito.Mockito._
import org.mockito.Matchers._
import coriander.haarlem.core.Convert
import jetbrains.buildServer.serverSide.{SBuildType, SFinishedBuild, ProjectManager}
import org.scalatest.{BeforeAndAfterEach, Spec}

class BuildFinderTests extends Spec with ShouldMatchers with BeforeAndAfterEach {
	override def beforeEach() {
		given_a_single_build_type
		given_a_finder
		result = null
	}

	describe("BuildFinder") {
		it("Queries the build server") {
			given_no_finished_builds

			when_asked_to_find_sommit

			then_it_queries_the_project_manager_for_all_build_types

			then_the_result_is_empty
		}

		it("Collects all successful builds") {
			given_one_finished_build
			when_asked_to_find_sommit

			result.length should equal(1)
		}

		it("Returns only builds completed within date range") (pending)

		it("Ignores failed builds") (pending)

		it("Ignores builds that are currently running") (pending)
	}

	private def given_a_single_build_type {
		buildType = mock(classOf[SBuildType])
		projectManager = mock(classOf[ProjectManager])

		when(projectManager.getAllBuildTypes).thenReturn(Convert.toJavaList(List(buildType)))
	}

	private def given_a_finder {
		finder = new BuildFinder(projectManager)	
	}

	private def given_no_finished_builds {
		require(buildType != null, "No build type")
		when(buildType.getLastSuccessfullyFinished).thenReturn(null)
	}

	private def given_one_finished_build {
		val fakeFinishedBuild : SFinishedBuild = mock(classOf[SFinishedBuild])
		when(fakeFinishedBuild.getBuildDescription).thenReturn("A fake example build")
		
		when(buildType.getLastSuccessfullyFinished).thenReturn(fakeFinishedBuild)
	}

	private def when_asked_to_find_sommit {
		require(finder != null, "Can't proceed without the CUT (there is no finder).")

		result = finder.find()
	}

	private def then_it_queries_the_project_manager_for_all_build_types {
		verify(projectManager).getAllBuildTypes
	}

	private def then_the_result_is_empty {
		result.length should be(0)
	}

	private var projectManager 	: ProjectManager = null
	private var finder 			: BuildFinder = null
	private var result			: List[SFinishedBuild] = null
	private var buildType		: SBuildType = null
}