package coriander.haarlem.unit.tests.controllers

import org.junit.Assert._
import org.hamcrest.core.IsEqual._
import org.mockito.Mockito._
import org.junit.{Before, Test}
import java.util.ArrayList
import jetbrains.buildServer.users.{SUser, UserModel}
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import jetbrains.buildServer.serverSide.{ProjectManager, SBuildServer}
import jetbrains.buildServer.web.openapi.PluginDescriptor

import coriander.haarlem.controllers.ReleasesController

class ReleasesControllerTests extends ControllerUnitTest {
	@Before
	def before {
		buildServer 		= mock(classOf[SBuildServer])
		projectManager 		= mock(classOf[ProjectManager])
		pluginDescriptor 	= mock(classOf[PluginDescriptor])
		request 			= mock(classOf[HttpServletRequest])
		response 			= mock(classOf[HttpServletResponse])
	}

	@Test
	def can_create_instance {
		given_a_controller
	}

	private def given_a_controller {
		controller = new ReleasesController(
			buildServer,
			projectManager,
			pluginDescriptor
		);
	}

	private var projectManager : ProjectManager = null
	private var controller : ReleasesController = null
}