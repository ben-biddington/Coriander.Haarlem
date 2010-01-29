package coriander.haarlem.unit.tests.tabs

import org.junit.Assert._
import org.hamcrest.core.Is._
import org.junit.{Before, Test}
import jetbrains.buildServer.messages.Status
import coriander.haarlem.unit.tests.TabUnitTest
import coriander.haarlem.tabs.{StickTab}

class StickTabTests extends TabUnitTest {
	@Before
	def before {
		tab = new StickTab(mockBuildServer)
	}

    @Test
	def is_not_available_when_no_build_id_supplied {
    	given_query_string("")

		val actual = tab.isAvailable(mockRequest)

		assertThat(actual, is(false))
    }

	@Test
	def is_available_when_build_failed {
		given_any_build_with_status(Status.FAILURE)

    	val actual = tab.isAvailable(mockRequest)

		assertThat(
			"Expected that the tab should be available for a failed build.",
			actual, is(true)
		)
    }

	@Test
	def is_not_available_when_build_normal {
		given_any_build_with_status(Status.NORMAL)

    	val actual = tab.isAvailable(mockRequest)

		assertThat(
			"Expected that the tab should not be available for a normal build.",
			actual, is(false)
		)
    }

	@Test
	def is_available_when_build_errors {
		given_any_build_with_status(Status.ERROR)

    	val actual = tab.isAvailable(mockRequest)

		assertThat(
			"Expected that the tab should be available for a error build.",
			actual, is(true)
		)
    }

	private var tab : StickTab = null
}