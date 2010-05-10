package coriander.haarlem.unit.tests.controllers

import org.mockito.Mockito._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import jetbrains.buildServer.serverSide.{ProjectManager, SBuildServer}
import jetbrains.buildServer.web.openapi.PluginDescriptor

import coriander.haarlem.controllers.ReleasesController
import coriander.haarlem.core.calendar.IBuildFinder
import org.junit.{Ignore, Before, Test}

class ReleasesControllerTests extends ControllerUnitTest {
	@Before
	def before {
		buildServer 		= mock(classOf[SBuildServer])
		projectManager 		= mock(classOf[ProjectManager])
		pluginDescriptor 	= mock(classOf[PluginDescriptor])
		request 			= mock(classOf[HttpServletRequest])
		response 			= mock(classOf[HttpServletResponse])
		buildFinder 		= mock(classOf[IBuildFinder])
	}

	@Test
	def can_create_instance {
		given_a_controller
	}

	@Test @Ignore
	def finds_builds_for_the_last_week {
		//pending
	}

	private def given_a_controller {
		controller = new ReleasesController(
			pluginDescriptor,
			buildFinder
		);
	}

	private var projectManager : ProjectManager = null
	private var controller : ReleasesController = null
	private var buildFinder : IBuildFinder = null
}