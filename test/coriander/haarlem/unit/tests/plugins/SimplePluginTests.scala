package coriander.haarlem.unit.tests.plugins

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.mockito.Mockito._
import org.mockito.Matchers._
import jetbrains.buildServer.messages.BuildMessage1
import coriander.haarlem.plugins.SimplePlugin
import coriander.haarlem.unit.tests.UnitTest

class SimplePluginTests extends UnitTest {
    @Test
	def can_initialize_one { 
    	val plugin = new SimplePlugin;
    }

	@Test
	def before_build_finish_adds_a_custom_message {
		given_any_running_build
    	val plugin = new SimplePlugin;

		plugin.beforeBuildFinish(mockRunningBuild)

		verify(mockRunningBuild, times(1)).addBuildMessage(any(classOf[BuildMessage1]));
    }
}