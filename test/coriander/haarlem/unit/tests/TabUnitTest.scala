package coriander.haarlem.unit.tests

import org.junit.Before
import org.mockito.Mockito._
import org.mockito.Matchers._
import javax.servlet.http.HttpServletRequest
import jetbrains.buildServer.messages.Status
import jetbrains.buildServer.serverSide.{SBuildServer, SBuild}

class TabUnitTest {
	@Before
	def initialiseFakes {
		mockRequest = mock(classOf[HttpServletRequest])
		mockBuildServer = mock(classOf[SBuildServer])
	}

	protected def given_any_build_with_status(status : Status) {
		given_query_string("buildId=" + ANY_BUILD_ID.toString)
		given_build_has_status(ANY_BUILD_ID, status)
	}

	protected def given_query_string(queryString : String) {
		when(mockRequest.getQueryString).
		thenReturn(queryString)
	}

	protected def given_build_has_status(buildId : Long, status : Status) {
		val aFailedBuild = aFakeBuildWithStatus(status)

		when(mockBuildServer.findBuildInstanceById(buildId)).
		thenReturn(aFailedBuild)
	}

	protected def aFakeBuildWithStatus(status : Status) : SBuild = {
		val result : SBuild = mock(classOf[SBuild])
		when(result.getBuildStatus).thenReturn(status)

		result
	}

	protected var mockRequest : HttpServletRequest = null
	protected var mockBuildServer : SBuildServer = null
	val ANY_BUILD_ID : Long = 1L
}