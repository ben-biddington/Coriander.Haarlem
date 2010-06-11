package coriander.haarlem.unit.tests.controllers.releases

import org.junit.{Test, Before}
import org.mockito.Mockito._
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import jetbrains.buildServer.web.openapi.PluginDescriptor
import coriander.haarlem.core.scheduling.SystemClock
import coriander.haarlem.core.calendar.IBuildFinder

class Last extends ReleasesControllerUnitTest {
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
	def accepts_last_parameter {
		when_last_supplied_as(10)
		then_we_search_for_the_last_with_no_filter_options(10)
	}

	@Test
	def you_can_only_ask_for_up_to_the_last_200_builds_for_performance_reasons {
		when_last_supplied_as(201)
		then_we_search_for_the_last_with_no_filter_options(200)
	}

	@Test
	def the_last_parameter_ignores_sign {
		when_last_supplied_as(-1)
		then_we_search_for_the_last_with_no_filter_options(1)
	}

	private def when_last_supplied_as(howMany : Int) {
		stub(request.getQueryString).toReturn("last=" + howMany.toString)

		doIt
	}
}