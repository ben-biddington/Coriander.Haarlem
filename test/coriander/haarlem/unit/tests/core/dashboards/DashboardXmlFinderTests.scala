package coriander.haarlem.unit.tests.core.dashboards

import org.junit.Assert._
import org.junit.{Before, Test}
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.mockito.Mockito._
import org.mockito.Matchers._
import java.io.File
import coriander.haarlem.unit.tests.UnitTest
import coriander.haarlem.core.dashboards.DashboardXmlFinder
import jetbrains.buildServer.serverSide.{SFinishedBuild, SBuildType}

class DashboardXmlFinderTests extends UnitTest {
	@Before
	def before {
		build = mock(classOf[SBuildType])
	}
	
    @Test
	def given_build_has_never_completed_successfully_then_it_has_no_dashboards {
    	given_a_build_that_has_never_completed_successfully
		then_has_dashboard_returns(false)
    }

	@Test
	def given_build_with_empty_artifacts_directory_then_it_has_no_dashboards {
		given_a_build_that_has_completed_successfully
    	given_a_build_with_an_empty_artifacts_directory
		
		then_has_dashboard_returns(false)
    }

	@Test
	def given_build_with_artifacts_but_no_dashboard_xml_then_it_has_no_dashboards {
		given_a_build_that_has_completed_successfully
    	given_a_build_with_non_empty_an_empty_artifacts_directory

		then_has_dashboard_returns(false)
    }

	@Test
	def given_build_with_dashboard_xml_artifact_then_it_has_dashboards {
		given_a_build_that_has_completed_successfully
    	given_a_build_with_dashboard_xml

		then_has_dashboard_returns(true)
    }

	private def given_a_build_that_has_never_completed_successfully {
		when(build.getLastChangesSuccessfullyFinished).thenReturn(null)
	}

	private def given_a_build_that_has_completed_successfully {
		lastSuccessfulBuild = mock(classOf[SFinishedBuild])
		when(lastSuccessfulBuild.getArtifactsDirectory).thenReturn(anyArtifactDirectory)
		when(build.getLastChangesSuccessfullyFinished).thenReturn(lastSuccessfulBuild)
	}

	private def given_a_build_with_an_empty_artifacts_directory {
		val fakeArtifactsDir : File = newFakeEmptyDirectory
		when(lastSuccessfulBuild.getArtifactsDirectory).thenReturn(fakeArtifactsDir)
	}

	private def given_a_build_with_non_empty_an_empty_artifacts_directory {
		var theFiles = new Array[String](1)
		theFiles.update(0, "CHUBBY_BAT.txt") 
	
		val fakeArtifactsDir : File = newFakeDirectory(theFiles)
		
		when(lastSuccessfulBuild.getArtifactsDirectory).thenReturn(fakeArtifactsDir)
	}

	private def given_a_build_with_dashboard_xml {
		var theFiles = new Array[String](1)
		theFiles.update(0, "dashboard") 

		val fakeArtifactsDir : File = newFakeDirectory(theFiles)

		when(lastSuccessfulBuild.getArtifactsDirectory).thenReturn(fakeArtifactsDir)
	}

	private def then_has_dashboard_returns(what : Boolean) {
	   	val result = new DashboardXmlFinder().hasDashboard(build)

		assertThat(result, is(equalTo(what)))
	}

	private def newFakeEmptyDirectory = {
		newFakeDirectory(new Array[String](0))
	}

	private def newFakeDirectory(files : Array[String]) = {
		val result = mock(classOf[File])
		val canonicalFile = mock(classOf[File])

		when(result.getCanonicalFile).thenReturn(canonicalFile)
		when(canonicalFile.list).thenReturn(files)

		result
	}
	
	private var build : SBuildType = null
	private var lastSuccessfulBuild : SFinishedBuild = null
	private var anyArtifactDirectory : File = new File("")
}