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
import java.util.{Date, ArrayList}
import jetbrains.buildServer.messages.Status
import org.junit.{Ignore, Before, Test}

class BuildLogSearchTests extends UnitTest {
	@Before
	def before {
		given_log_messages()
	}

    @Test
	def Searching_in_empty_logs_returns_empty_result {

		val search = new BuildLogSearch(mockBuildLog)

		val actual = search.searchForPattern("anything");

		assertThat(actual.size, is(equalTo(0)))
    }

	@Test
	def Searching_one_log_file_returns_match {
		given_log_messages("CHUBBY RAIN")
		
		val search = new BuildLogSearch(mockBuildLog)

		val actual = search.searchForPattern("CHUBB");

		assertThat(actual.size, is(equalTo(1)))
    }

	@Test
	def non_matching_lines_are_ignored {
		given_log_messages("CHUBBY RAIN", "BOOTS", "APPLE")

		val search = new BuildLogSearch(mockBuildLog)

		val actual = search.searchForPattern("CHUBB");

		assertThat(actual.size, is(equalTo(1)))
    }

	@Test
	def multiple_matching_lines_are_returned {
		given_log_messages("CHUBBY RAIN", "BOOTS", "APPLE", "CHUBBY BAT")

		val search = new BuildLogSearch(mockBuildLog)

		val actual = search.searchForPattern("CHUBB");

		assertThat(actual.size, is(equalTo(2)))
    }

	@Test
	def match_is_case_sensitive {
		given_log_messages("CHUBBY RAIN")

		val search = new BuildLogSearch(mockBuildLog)

		val actual = search.searchForPattern("chubby");

		assertThat(actual.size, is(equalTo(0)))
    }

	@Test
	def searching_for_regex_special_chars_is_okay {
		given_log_messages("C:\\Documents\\xxx")

		val search = new BuildLogSearch(mockBuildLog)

		val actual = search.searchForPattern("\\xxx");

		assertThat(actual.size, is(equalTo(1)))
    }

	@Test
	def searching_for_regex_pattern_works {
		given_log_messages("C:\\Documents\\1234567890")

		val search = new BuildLogSearch(mockBuildLog)

		val actual = search.searchForRegex("[0-9]+");

		assertThat(actual.size, is(equalTo(1)))
    }
	
	private def given_log_messages(messages : String*) {
		var buffer = new ArrayList[LogMessage]

		messages.foreach(msg => buffer.add(newLogMessage(msg)))

		logMessages = buffer.iterator

		mockBuildLog = mock(classOf[BuildLog])

		when(mockBuildLog.getMessagesIterator).thenReturn(logMessages)
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