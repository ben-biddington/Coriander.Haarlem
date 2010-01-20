package coriander.haarlem.unit.tests.controllers

import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.mockito.Mockito._
import org.mockito.Matchers._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import coriander.haarlem.unit.tests.UnitTest
import jetbrains.buildServer.serverSide.SBuildServer
import org.junit.{Before, Test}
import coriander.haarlem.controllers.LogSearchController

class LogSearchControllerTests extends UnitTest {

	@Before
	def before {
		mockBuildServer = mock(classOf[SBuildServer])
		mockRequest = null
	}

    @Test { val expected = classOf[Exception] }
	def doHandle_throws_exception {
		val mockRequest = mock(classOf[HttpServletRequest])
		val mockResponse = mock(classOf[HttpServletResponse])

		new LogSearchController(mockBuildServer).doHandle(mockRequest, mockResponse)
    }

	@Test
	def accepts_build_id_as_query_parameter {
		given_a_request_for_build("1337")
		
		val mockResponse = mock(classOf[HttpServletResponse])

		new LogSearchController(mockBuildServer).doHandle(mockRequest, mockResponse)
	}

	@Test
	def requires_search_term_as_query_parameter {
		given_a_request_for_build("1337")

		val mockResponse = mock(classOf[HttpServletResponse])

		new LogSearchController(mockBuildServer).doHandle(mockRequest, mockResponse)
	}

	@Test
	def given_the_build_exists_then_responds_with_200_OK {
		given_a_request_for_build("1337")
		given_build_exists_with_id("1337")
		
		val mockResponse = mock(classOf[HttpServletResponse])

		new LogSearchController(mockBuildServer).doHandle(mockRequest, mockResponse)

		verify(mockResponse, times(1)).setStatus(200)
	}

	private def given_build_exists_with_id(expected : String) {
		when(mockBuildServer.getBuildNumber).thenReturn(expected)
	}

	private def given_a_request_for_build(buildId : String) {
		mockRequest = mock(classOf[HttpServletRequest])
		when(mockRequest.getQueryString).
		thenReturn("build=" + buildId.toString + "&q=")
	}

	var mockBuildServer : SBuildServer = null
	var mockRequest : HttpServletRequest = null
}