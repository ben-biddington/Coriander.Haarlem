package coriander.haarlem.unit.tests.controllers

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.mockito.Mockito._
import org.mockito.Matchers._
import coriander.haarlem.controllers.LogMinerController
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import coriander.haarlem.unit.tests.UnitTest
import jetbrains.buildServer.serverSide.SBuildServer

class LogMinerControllerTests extends UnitTest {
	val mockBuildServer = mock(classOf[SBuildServer])
	when(mockBuildServer.getBuildNumber).thenReturn("1337")

    @Test { val expected = classOf[Exception] }
	def doHandle_throws_exception {
		val mockRequest = mock(classOf[HttpServletRequest])
		val mockResponse = mock(classOf[HttpServletResponse])

		new LogMinerController(mockBuildServer).doHandle(mockRequest, mockResponse)    
    }

	@Test
	def accepts_build_id_as_query_parameter {
		val mockRequest = mock(classOf[HttpServletRequest])
		val mockResponse = mock(classOf[HttpServletResponse])

		when(mockRequest.getQueryString).thenReturn("?build=xxx")

		new LogMinerController(mockBuildServer).doHandle(mockRequest, mockResponse)

		verify(mockResponse, times(1)).setStatus(200)
	}

	@Test
	def given_the_build_exists_then_responds_with_200_OK {
		val mockRequest = mock(classOf[HttpServletRequest])
		val mockResponse = mock(classOf[HttpServletResponse])

		when(mockRequest.getQueryString).thenReturn("?build=1337")

		new LogMinerController(mockBuildServer).doHandle(mockRequest, mockResponse)

		verify(mockResponse, times(1)).setStatus(200)
	}
}