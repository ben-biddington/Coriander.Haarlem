package coriander.haarlem.unit.tests.controllers

import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.mockito.Mockito._
import org.mockito.Matchers._
import coriander.haarlem.controllers.DilbertController
import org.springframework.context.ApplicationContext
import org.junit.{Before, Test}
import jetbrains.buildServer.web.openapi.{WebControllerManager, PluginDescriptor}

class DilbertControllerTests {

	@Before
	def before {
		context = null
		pluginDescriptor = null
	}

    @Test { val expected = classOf[RuntimeException]}
	def register_throws_exception_if_no_route_has_been_set {
		given_an_application_context

		given_a_plugin_descriptor
		
		val controller = new DilbertController(pluginDescriptor)
		controller.setApplicationContext(context)

		controller.register
    }

	@Test
	def register_uses_the_route_supplied_as_the_route_property {
		given_an_application_context
		given_a_plugin_descriptor

		val expectedRoute = "/chubby_bat.html"

		val controller = new DilbertController(pluginDescriptor)
		controller.setApplicationContext(context)
		controller.setRoute(expectedRoute)
		controller.register

		verify(webControllerManager).registerController(expectedRoute, controller)
	}

	private def given_an_application_context {
		context = mock(classOf[ApplicationContext])
		webControllerManager = mock(classOf[WebControllerManager])
		
		when(context.getBean("webControllerManager", classOf[WebControllerManager])).
			thenReturn(webControllerManager)
	}

	private def given_a_plugin_descriptor {
		pluginDescriptor = mock(classOf[PluginDescriptor])
	}

	private var context : ApplicationContext = null
	private var pluginDescriptor : PluginDescriptor = null
	private var webControllerManager : WebControllerManager = null
}