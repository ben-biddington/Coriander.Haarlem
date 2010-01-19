package coriander.haarlem.unit.tests.plugins

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import coriander.haarlem.plugins.SimplePlugin

class SimplePluginTests {
    @Test
	def can_initialize_one { 
    	val plugin = new SimplePlugin;
    }
}