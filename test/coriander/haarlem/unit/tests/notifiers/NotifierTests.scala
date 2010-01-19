package coriander.haarlem.unit.tests.notifiers

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.mockito.Mockito._
import org.mockito.Matchers._
import jetbrains.buildServer.serverSide.SRunningBuild
import jetbrains.buildServer.users.SUser
import coriander.haarlem.notifiers.{Notifier}
import java.util.HashSet
import coriander.haarlem.unit.tests.UnitTest

class NotifierTests extends UnitTest {
    @Test
	def notifyBuildStarted_reads_the_current_build_path {
		given_any_running_build
		
		val exampleUsers = new HashSet[SUser]

 		new Notifier().notifyBuildStarted(mockRunningBuild, exampleUsers)

		verify(mockRunningBuild, times(1)).getCurrentPath()
    }
}