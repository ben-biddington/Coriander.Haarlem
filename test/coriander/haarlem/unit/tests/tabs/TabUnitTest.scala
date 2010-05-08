package coriander.haarlem.unit.tests

import org.junit.Before
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.mockito.Mockito._
import org.mockito.Matchers._
import javax.servlet.http.HttpServletRequest
import jetbrains.buildServer.messages.Status
import jetbrains.buildServer.serverSide.{SBuildServer, SBuild}
import jetbrains.buildServer.web.openapi.CustomTab

class TabUnitTest {
	@Before
	def initialiseFakes {
		mockRequest = mock(classOf[HttpServletRequest])
		mockBuildServer = mock(classOf[SBuildServer])
		buildStatus = null
		isAliased = false
	}

	protected def given_any_finished_build_with_status(status : Status) {
		given_query_string("buildId=" + ANY_BUILD_ID.toString)
		given_build_has_status(ANY_BUILD_ID, status, true)
	}

	protected def given_any_running_build_with_status(status : Status) {
		given_query_string("buildId=" + ANY_BUILD_ID.toString)
		given_build_has_status(ANY_BUILD_ID, status, false)
	}

	protected def given_query_string(queryString : String) {
		when(mockRequest.getQueryString).
		thenReturn(queryString)
	}

	protected def given_build_has_status(buildId : Long, status : Status, finished : Boolean) {
		val build = aFakeBuildWithStatus(status)
		buildStatus = status

		when(mockBuildServer.findBuildInstanceById(buildId)).
		thenReturn(build)

		when(build.isFinished).
		thenReturn(finished)

		if (isAliased) {
			given_build_key(build)
		}
	}

	protected def given_the_current_build_is_an_alias = isAliased = true

	protected def given_no_build_key = given_build_key(null);

	protected def given_build_key(build : SBuild) {
		when(mockRequest.getAttribute("BUILD_KEY")).
		thenReturn(build, null);
	}

	protected def then_tab_is_available {
		val actual = tab.isAvailable(mockRequest)

		assertThat(
			"Expected that the tab should be available for a " + buildStatus.toString + " build.",
			actual, is(true)
		)
	}

	protected def then_tab_is_not_available {
		val actual = tab.isAvailable(mockRequest)

		assertThat(
			"Expected that the tab should NOT be available for a(n) " + buildStatus.toString + " build.",
			actual, is(false)
		)
	}

	protected def aFakeBuildWithStatus(status : Status) : SBuild = {
		val result : SBuild = mock(classOf[SBuild])
		when(result.getBuildStatus).thenReturn(status)

		result
	}

	protected var tab : CustomTab = null
	protected var mockRequest : HttpServletRequest = null
	protected var mockBuildServer : SBuildServer = null
	protected val ANY_BUILD_ID : Long = 1L
	protected var buildStatus : Status = null
	protected var isAliased : Boolean = false
}