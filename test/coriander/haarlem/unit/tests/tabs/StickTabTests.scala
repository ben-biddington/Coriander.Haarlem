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
	def
	is_available_when_build_finishes_and_fails {
		given_any_finished_build_with_status(Status.FAILURE)

    	then_tab_is_available
    }

	@Test
	def is_not_available_when_build_finishes_normally {
		given_any_finished_build_with_status(Status.NORMAL)

    	then_tab_is_not_available
    }

	@Test
	def is_available_when_build_finishes_with_errors {
		given_any_finished_build_with_status(Status.ERROR)

    	then_tab_is_available
    }

	@Test
	def is_not_available_until_failed_build_has_completed {
		given_any_running_build_with_status(Status.ERROR)

    	then_tab_is_not_available
	}
}