package coriander.haarlem.unit.tests.tabs

import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.mockito.Mockito._
import org.mockito.Matchers._
import javax.servlet.http.HttpServletRequest
import org.junit.{Before, Test}
import jetbrains.buildServer.serverSide.{SBuild, SBuildServer}
import jetbrains.buildServer.messages.Status
import coriander.haarlem.tabs.CarrotTab

class DilbertTabTests {
	@Before
	def before {
		mockRequest = mock(classOf[HttpServletRequest])
		mockBuildServer = mock(classOf[SBuildServer])
		tab = new CarrotTab(mockBuildServer)
	}

    @Test
	def is_not_available_when_no_build_id_supplied { 
    	given_query_string("")

		val actual = tab.isAvailable(mockRequest)

		assertThat(actual, is(false))
    }

	@Test
	def is_not_available_when_build_failed {
		given_any_build_with_status(Status.FAILURE)

    	val actual = tab.isAvailable(mockRequest)

		assertThat(
			"Expected that the tab should not be available for a failed build.",
			actual, is(false)
		)
    }

	@Test
	def is_available_when_build_normal {
		given_any_build_with_status(Status.NORMAL)

    	val actual = tab.isAvailable(mockRequest)

		assertThat(
			"Expected that the tab should not be available for a normal build.",
			actual, is(true)
		)
    }

	@Test
	def is_not_available_when_build_errors {
		given_any_build_with_status(Status.ERROR)

    	val actual = tab.isAvailable(mockRequest)

		assertThat(
			"Expected that the tab should not be available for a normal build.",
			actual, is(false)
		)
    }

	private def given_any_build_with_status(status : Status) {
		given_query_string("buildId=" + ANY_BUILD_ID.toString)
		given_build_has_status(ANY_BUILD_ID, status)
	}

	private def given_query_string(queryString : String) {
		when(mockRequest.getQueryString).
		thenReturn(queryString)
	}

	private def given_build_has_status(buildId : Long, status : Status) {
		val aFailedBuild = aFakeBuildWithStatus(status)

		when(mockBuildServer.findBuildInstanceById(buildId)).
		thenReturn(aFailedBuild)
	}

	private def aFakeBuildWithStatus(status : Status) : SBuild = {
		val result : SBuild = mock(classOf[SBuild])
		when(result.getBuildStatus).thenReturn(status)

		result
	}

	private var tab : CarrotTab = null
	private var mockRequest : HttpServletRequest = null
	private var mockBuildServer : SBuildServer = null
	val ANY_BUILD_ID : Long = 1L
}