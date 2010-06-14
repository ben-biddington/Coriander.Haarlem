package coriander.haarlem.unit.tests.controllers.releases

import org.mockito.Mockito._
import org.mockito.Matchers._
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import coriander.haarlem.core.calendar.IBuildFinder
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import jetbrains.buildServer.web.openapi.PluginDescriptor
import org.junit.{Test, Ignore, Before}
import coriander.haarlem.core.scheduling.{Clock, SystemClock}

class Since extends ReleasesControllerUnitTest {
	@Before
	def before {
		request 			= mock(classOf[HttpServletRequest])
		response 			= mock(classOf[HttpServletResponse])
		pluginDescriptor 	= mock(classOf[PluginDescriptor])
		buildFinder 		= mock(classOf[IBuildFinder])
		clock				= new SystemClock
		result				= null
		plonkers 			= null

		given_a_build_finder
	}

	@Test
	def accepts_since_parameter {
		when_since_supplied_as("2-weeks-ago")
		then_builds_are_searched_in(theLastTwoWeeks)
	}

	@Test
	def since_can_be_in_days {
		when_since_supplied_as("1-day-ago")
		then_builds_are_searched_in(theLastDay)
	}

	@Test
	def since_can_be_today {
		when_since_supplied_as("today")
		then_builds_are_searched_in(today)
	}

	@Test
	def since_can_be_yesterday {
		when_since_supplied_as("yesterday")
		then_builds_are_searched_in(theLastDay)
	}

	@Test
	def since_can_have_count_zero_which_returns_builds_since_midnight_today {
		when_since_supplied_as("0-days-ago")
		then_builds_are_searched_in(today)
	}

	@Test
	def when_since_is_missing_then_you_get_the_last_twenty_five_builds {
		when_since_not_supplied
		then_we_search_for_the_last_with_no_filter_options(25)
	}

	@Test
	def you_can_only_ask_for_builds_since_a_maximum_90_days_ago_for_performance_reasons {
		when_since_supplied_as("91-days-ago")
		then_builds_are_searched_in(theLastNinetyDays)
		then_there_is_an_error(
			"The requested number of days exceeds the limit of <90>, " +
			"results have been truncated"
		)
	}

	@Test
	def controller_refreshes_its_time_on_each_call_because_instance_may_have_been_reused {
		clock = mock(classOf[Clock])
		
		when_since_supplied_as("1-days-ago")

		controller.go(request, response)

		verify(clock, times(2)).now
	}

	private def when_since_not_supplied {
		when_since_supplied_as("")
	}

	private def when_since_supplied_as(what : String) {
		stub(request.getQueryString).toReturn("since=" + what)

		doIt
	}
}