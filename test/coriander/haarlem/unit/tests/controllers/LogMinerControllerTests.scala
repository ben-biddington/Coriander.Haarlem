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

class LogMinerControllerTests extends UnitTest {
    @Test { val expected = classOf[Exception] }
	def doHandle_throws_exception {
		val mockRequest = mock(classOf[HttpServletRequest])
		val mockResponse = mock(classOf[HttpServletResponse])

		new LogMinerController().doHandle(mockRequest, mockResponse)    
    }
}