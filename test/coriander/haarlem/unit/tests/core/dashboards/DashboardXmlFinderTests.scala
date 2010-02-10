package coriander.haarlem.unit.tests.core.dashboards

import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.hamcrest.core.IsEqual._
import coriander.haarlem.unit.tests.UnitTest
import jetbrains.buildServer.serverSide.SBuildType
import org.junit.{Before, Test}
import coriander.haarlem.core.dashboards.DashboardXmlFinder

class DashboardXmlFinderTests extends UnitTest {
	@Before
	def before {
		build = mock(classOf[SBuildType])
	}
	
    @Test
	def given_build_has_never_completed_successfully_then_returns_false {
    	given_a_build_that_has_never_completed_successfully
    }

	private def given_a_build_that_has_never_completed_successfully {
		when(build.getLastChangesSuccessfullyFinished).thenReturn(null)

		val result = new DashboardXmlFinder().hasDashboard(build)

		assertThat(result, is(equalTo(false)))
	}

	private var build : SBuildType = null
}