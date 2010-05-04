package coriander.haarlem.unit.tests.core.calendar

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import coriander.haarlem.core.calendar.BuildFinder
import org.mockito.Mockito._
import org.mockito.Matchers._
import coriander.haarlem.core.Convert
import jetbrains.buildServer.serverSide.{SBuildType, SFinishedBuild, ProjectManager, SBuildServer}

class BuildFinderTests extends Spec with ShouldMatchers {
	def beforeEach() {
		projectManager = null
	}

	describe("BuildFinder") {
		it("Queries the build server") {
			given_a_project_manager
			given_a_finder
			
			when_finder_is_asked_to_find_sommit
			then_it_queries_the_project_manager_for_all_build_types
		}

		it("Returns only builds completed within date range") {
			
		}

		it("Ignores failed builds") {
			
		}

		it("Ignores builds that are currently running") {
			
		}
	}

	private def given_a_finder {
		finder = new BuildFinder(projectManager)	
	}

	private def given_there_are_no_builds {
		// @todo: implement	
	}

	private def given_a_project_manager {
		val emptyList = Convert.toJavaList[SBuildType](List())
		projectManager = mock(classOf[ProjectManager])
		when(projectManager.getAllBuildTypes).thenReturn(emptyList)
	}

	private def when_finder_is_asked_to_find_sommit {
		require(finder != null, "Can't proceed without the CUT (there is no finder).")

		finder.find()
	}

	private def then_it_queries_the_project_manager_for_all_build_types {
		verify(projectManager).getAllBuildTypes
	}

	private var projectManager 	: ProjectManager = null
	private var finder 			: BuildFinder = null
}