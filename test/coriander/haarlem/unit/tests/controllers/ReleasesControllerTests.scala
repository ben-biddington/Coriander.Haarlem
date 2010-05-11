package coriander.haarlem.unit.tests.controllers

import org.mockito.Mockito._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import jetbrains.buildServer.serverSide.{ProjectManager, SBuildServer}
import jetbrains.buildServer.web.openapi.PluginDescriptor

import coriander.haarlem.controllers.ReleasesController
import org.junit.{Ignore, Before, Test}
import coriander.haarlem.core.calendar.{FilterOptions, IBuildFinder}
import org.joda.time.{Instant, Interval}
import org.joda.time.Days._

class ReleasesControllerTests extends ControllerUnitTest {
	@Before
	def before {
		request 			= mock(classOf[HttpServletRequest])
		response 			= mock(classOf[HttpServletResponse])
		pluginDescriptor 	= mock(classOf[PluginDescriptor])
		buildFinder 		= mock(classOf[IBuildFinder])
	}

	@Test
	def can_create_instance {
		given_a_controller
	}

	@Test @Ignore
	def accepts_since_parameter {
		given_a_controller
		when_since_supplied_as("2-weeks-ago")
		then_builds_are_searched_in(theLastTwoWeeks)
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
	}

	private def then_builds_are_searched_in(interval : Interval) {
		verify(buildFinder).find(new FilterOptions(interval))
	}

	private var projectManager : ProjectManager = null
	private var controller : ReleasesController = null
	private var buildFinder : IBuildFinder = null

	private lazy val now 				= new Instant
	private lazy val fourteenDaysAgo 	= new Instant().minus(days(14).toStandardDuration)
	private lazy val theLastTwoWeeks 	= new Interval(fourteenDaysAgo, now)
}