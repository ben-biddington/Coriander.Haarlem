package coriander.haarlem.unit.tests.controllers

import org.mockito.Mockito._
import org.mockito.Matchers._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import jetbrains.buildServer.serverSide.{ProjectManager}
import jetbrains.buildServer.web.openapi.PluginDescriptor

import coriander.haarlem.controllers.ReleasesController
import org.junit.{Before, Test}
import coriander.haarlem.core.calendar.{FilterOptions, IBuildFinder}
import org.joda.time.{Instant, Interval}
import org.joda.time.Days._
import coriander.haarlem.matchers.FilterOptionsMatcher._

class ReleasesControllerTests extends ControllerUnitTest {
	@Before
	def before {
		request 			= mock(classOf[HttpServletRequest])
		response 			= mock(classOf[HttpServletResponse])
		pluginDescriptor 	= mock(classOf[PluginDescriptor])
		buildFinder 		= mock(classOf[IBuildFinder])

		given_a_build_finder
		given_a_controller
	}

	@Test
	def accepts_since_parameter {
		when_since_supplied_as("2-weeks-ago")
		then_builds_are_searched_in(theLastTwoWeeks)
	}

	private def given_a_build_finder {
		stub(buildFinder.find(any(classOf[FilterOptions]))).
		toReturn(List())
	}

	private def given_a_controller {
		controller = new ReleasesController(
			pluginDescriptor,
			buildFinder
		);
	}

	private def when_since_supplied_as(what : String) {
		stub(request.getQueryString).
		toReturn("?since=" + what)

		controller.go(request, response)
	}

	private def then_builds_are_searched_in(interval : Interval) {
		verify(buildFinder).find(argThat(hasMatching(interval)))
	}

	private var projectManager 	: ProjectManager = null
	private var controller 		: ReleasesController = null
	private var buildFinder 	: IBuildFinder = null

	private lazy val now 				= new Instant
	private lazy val fourteenDaysAgo 	= new Instant().minus(days(14).toStandardDuration)
	private lazy val theLastTwoWeeks 	= new Interval(fourteenDaysAgo, now)
}