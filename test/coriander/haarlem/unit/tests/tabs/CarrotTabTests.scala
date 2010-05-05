package coriander.haarlem.unit.tests.tabs

import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.junit.{Before, Test}
import jetbrains.buildServer.messages.Status
import coriander.haarlem.tabs.CarrotTab
import coriander.haarlem.unit.tests.TabUnitTest

class CarrotTabTests extends TabUnitTest {
	@Before
	def before {
		tab = new CarrotTab(mockBuildServer)
	}

    @Test
	def is_not_available_when_no_build_id_supplied { 
    	given_query_string("")

		val actual = tab.isAvailable(mockRequest)

		assertThat(actual, is(false))
    }

	@Test
	def is_not_available_when_build_finished_but_failed {
		given_any_finished_build_with_status(Status.FAILURE)

    	then_tab_is_not_available
    }

	@Test
	def is_available_when_build_finished_normally {
		given_any_finished_build_with_status(Status.NORMAL)

    	then_tab_is_available
    }

	@Test
	def is_not_available_when_build_finished_with_errors {
		given_any_finished_build_with_status(Status.ERROR)

    	then_tab_is_not_available
    }

	@Test
	def is_not_available_until_build_has_completed {
		given_any_running_build_with_status(Status.NORMAL)

    	then_tab_is_not_available
	}

	// @see: http://youtrack.jetbrains.net/issue/TW-11465
	@Test
	def the_build_identifier_does_not_have_to_be_a_number_provided_there_is_build_key {
		given_the_current_build_is_an_alias
		
		given_a_finished_build_with_id("any_build_alias_for_example_last_pinned")

		then_tab_is_available
	}

	private def given_a_finished_build_with_id(what : String) {
		given_query_string("buildId=" + what)
		given_build_has_status(ANY_BUILD_ID, Status.NORMAL, true)
	}
}