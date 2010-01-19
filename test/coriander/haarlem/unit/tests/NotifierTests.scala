package coriander.haarlem.unit.tests

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.mockito.Mockito._
import org.mockito.Matchers._
import coriander.haarlem.Notifier
import jetbrains.buildServer.serverSide.SRunningBuild
import jetbrains.buildServer.users.SUser
import java.util.HashSet

class NotifierTests {
    @Test
	def notifyBuildStarted_reads_the_current_build_path {
		given_any_running_build
		
		val exampleUsers = new HashSet[SUser]

 		new Notifier().notifyBuildStarted(mockRunningBuild, exampleUsers)

		verify(mockRunningBuild, times(1)).getCurrentPath()
    }

	private def given_any_running_build {
		mockRunningBuild = mock(classOf[SRunningBuild])
		when(mockRunningBuild.getCurrentPath()).
		thenReturn("xxx");
	}

	var mockRunningBuild : SRunningBuild = null
}