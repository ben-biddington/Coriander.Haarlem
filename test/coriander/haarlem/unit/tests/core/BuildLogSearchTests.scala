package coriander.haarlem.unit.tests.core

import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.mockito.Mockito._
import org.mockito.Matchers._
import coriander.haarlem.unit.tests.UnitTest
import coriander.haarlem.core.BuildLogSearch
import jetbrains.buildServer.serverSide.buildLog.{BuildLog, LogMessage}
import collection.mutable.ListBuffer
import java.util.{Date, Calendar, ArrayList}
import jetbrains.buildServer.messages.Status
import org.junit.{Before, Test}

class BuildLogSearchTests extends UnitTest {
	@Before
	def before {
		given_log_messages()

		mockBuildLog = mock(classOf[BuildLog])

		when(mockBuildLog.getMessagesIterator).thenReturn(logMessages)
	}

    @Test
	def Searching_in_empty_logs_returns_empty_result {

		val search = new BuildLogSearch(mockBuildLog)

		val actual = search.searchFor("anything");

		assertThat(actual.size, is(equalTo(0)))
    }

	private def given_log_messages(messages : String*) {
		var buffer = new ArrayList[LogMessage]

		messages.foreach(msg => buffer.add(newLogMessage(msg)))

		logMessages = buffer.iterator
	}

	private def newLogMessage(message : String) ={
		new LogMessage(
			message,
			Status.FAILURE,
			new Date(),
			"",
			false,
			0
		)
	}
	private var mockBuildLog : BuildLog = null
	private var logMessages : java.util.Iterator[LogMessage] = null
}