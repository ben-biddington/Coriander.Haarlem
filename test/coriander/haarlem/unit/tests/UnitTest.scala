package coriander.haarlem.unit.tests

import org.mockito.Mockito._
import org.mockito.Matchers._
import jetbrains.buildServer.serverSide.SRunningBuild

class UnitTest {
    protected def given_any_running_build {
		mockRunningBuild = mock(classOf[SRunningBuild])
		when(mockRunningBuild.getCurrentPath()).
		thenReturn("xxx");
	}

	protected var mockRunningBuild : SRunningBuild = null
}