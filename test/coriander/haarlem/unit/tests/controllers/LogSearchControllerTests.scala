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
import coriander.haarlem.controllers.LogSearchController
import jetbrains.buildServer.web.openapi.PluginDescriptor
import java.io.File
import org.junit.{Ignore, Before, Test}

class LogSearchControllerTests extends UnitTest {

	@Before
	def before {
		mockBuildServer = mock(classOf[SBuildServer])
		mockRequest = null
		mockPluginDescriptor = mock(classOf[PluginDescriptor]) 
	}

    @Test { val expected = classOf[Exception] } @Ignore
	def doHandle_throws_exception {
		val mockRequest = mock(classOf[HttpServletRequest])
		val mockResponse = mock(classOf[HttpServletResponse])

		new LogSearchController(mockBuildServer).doHandle(mockRequest, mockResponse)
    }

	@Test @Ignore
	def accepts_build_id_as_query_parameter {
		given_a_request_for_build("1337")
		
		val mockResponse = mock(classOf[HttpServletResponse])

		new LogSearchController(mockBuildServer).doHandle(mockRequest, mockResponse)
	}

	@Test @Ignore
	def requires_search_term_as_query_parameter {
		given_a_request_for_build("1337")

		val mockResponse = mock(classOf[HttpServletResponse])

		when(mockPluginDescriptor.getPluginResourcesPath).
			thenReturn("")
		
		when(mockBuildServer.getArtifactsDirectory).
			thenReturn(new File("/"))

		when(mockBuildServer.getServerRootPath).thenReturn("")

		val result = new LogSearchController(mockBuildServer, mockPluginDescriptor).
			doHandle(mockRequest, mockResponse);

		val expectedViewName = "xxx"

		val actualViewName = result.getViewName()

		assertThat(actualViewName, is(equalTo(expectedViewName)))
	
	}

	private def given_build_exists_with_id(expected : String) {
		when(mockBuildServer.getBuildNumber).thenReturn(expected)
	}

	private def given_a_request_for_build(buildId : String) {
		mockRequest = mock(classOf[HttpServletRequest])
		when(mockRequest.getQueryString).
		thenReturn("buildId=" + buildId.toString + "&q=")
	}

	var mockBuildServer : SBuildServer = null
	var mockRequest : HttpServletRequest = null
	var mockPluginDescriptor : PluginDescriptor = null
}