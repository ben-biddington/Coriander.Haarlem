package coriander.haarlem.unit.tests.controllers.releases

import org.junit.{Ignore, Before, Test}
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import jetbrains.buildServer.web.openapi.PluginDescriptor
import coriander.haarlem.core.calendar.{IBuildFinder}
import coriander.haarlem.models.ReleasesModel
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import coriander.haarlem.core.scheduling.{SystemClock}

class Matching extends ReleasesControllerUnitTest {
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
	def accepts_matching_parameter {
		when_matching_supplied_with_required_count_as(10, "live")
		then_we_search_for_the_last_with_a_non_null_filter(10)
	}
	
	@Test
	def the_matching_parameter_is_a_regex_pattern {
		given_a_build_finder_that_returns(List(
			newFakeBuildWithDescription("[live] release"),
			newFakeBuildWithDescription("[uat] release")
		))

		when_matching_supplied_with_required_count_as(10, "live")
		
		val result : ModelAndView = controller.go(request, response)
		val model : ReleasesModel = result.getModel.get("results").asInstanceOf[ReleasesModel]

		assertThat(model.getBuilds.size, is(equalTo(2)))
	}

	@Test
	def the_matching_parameter_may_be_omitted {
		when_matching_supplied_with_required_count_as(10, null)
		then_we_search_for_the_last_with_a_non_null_filter(10)
	}

	@Test @Ignore
	def the_matching_parameter_must_be_a_valid_regex_otherwise_an_error_is_returned_and_no_results() {
		fail("PENDING")
		
		given_a_build_finder_that_returns(List(
			newFakeBuildWithDescription("[live] release"),
			newFakeBuildWithDescription("[uat] release")
		))

		when_matching_supplied_with_required_count_as(10, "[this is meant to be invalid[")

		then_there_is_an_error("Invalid regex")
		assertThat(result.getBuilds.size, is(equalTo(0)))
	}

	@Test
	def if_you_do_not_supply_since_or_last_then_you_get_the_last_25_results {
		doIt

		then_we_search_for_the_last_with_no_filter_options(25)
	}

	@Test
	def if_you_supply_matching_but_not_since_or_last_then_you_get_at_most_250_results {
		doIt

		then_we_search_for_the_last_with_no_filter_options(25)
	}

	private def when_matching_supplied_with_required_count_as(howMany : Int, what : String) {
		matching = what

		stub(request.getQueryString).toReturn("last=" + howMany + "&matching=" + what)

		doIt
	}
}