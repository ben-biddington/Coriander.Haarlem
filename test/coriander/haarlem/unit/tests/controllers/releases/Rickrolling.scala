package coriander.haarlem.unit.tests.controllers.releases

import org.junit.{Test, Before}
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.mockito.Mockito._
import org.mockito.Matchers._
import javax.servlet.http.{HttpSession, HttpServletResponse, HttpServletRequest}
import jetbrains.buildServer.web.openapi.PluginDescriptor
import org.joda.time.{TimeOfDay, DateTime}
import org.joda.time.DateTimeConstants._
import org.joda.time.DateTimeZone._
import coriander.haarlem.core.calendar.IBuildFinder
import coriander.haarlem.core.scheduling.{Clock, SystemClock}
import jetbrains.buildServer.users.SUser

class Rickrolling extends ReleasesControllerUnitTest {
	@Before
	def before {
		request 			= mock(classOf[HttpServletRequest])
		response 			= mock(classOf[HttpServletResponse])
		pluginDescriptor 	= mock(classOf[PluginDescriptor])
		buildFinder 		= mock(classOf[IBuildFinder])
		clock				= new SystemClock
		result				= null
		plonkers 			= null

		given_a_build_finder
	}

	@Test
	def anyone_in_the_plonker_list_can_be_rickrolled_at_leet_o_clock_gmt {
		given_the_greenwich_mean_time_is(leetOClock)
		given_the_plonkers("anyone.else@xxx.com")
		given_the_current_user_has_email("anyone.else@xxx.com")

		doIt

		assertThat(result.getRickrollable, is(true))
	}

	@Test
	def no_one_can_be_rickrolled_before_leet_o_clock_gmt {
		given_the_greenwich_mean_time_is(leetOClock.minusMinutes(1))
		given_the_plonkers("anyone.else@xxx.com")
		given_the_current_user_has_email("anyone.else@xxx.com")

		doIt

		assertThat(result.getRickrollable, is(false))
	}

	@Test
	def no_one_can_be_rickrolled_after_13_59_gmt {
		given_the_greenwich_mean_time_is(justBeforeTwo.plusMinutes(1))
		given_the_plonkers("anyone.else@xxx.com")
		given_the_current_user_has_email("anyone.else@xxx.com")

		doIt

		assertThat(result.getRickrollable, is(false))
	}

	private def given_the_greenwich_mean_time_is(when : TimeOfDay) {
		clock = mock(classOf[Clock])

		val millenniumBridgeClosed = new DateTime(
			2001,
			JUNE,
			10,
			when.getHourOfDay,
			when.getMinuteOfHour,
			when.getSecondOfMinute,
			when.getMillisOfSecond,
			UTC
		)

		stub(clock.now).toReturn(millenniumBridgeClosed.toInstant)
	}

	private def given_the_plonkers(what : String) {
		plonkers = what
	}

	private def given_the_current_user_has_email(what : String) {
		val fakeSession : HttpSession = mock(classOf[HttpSession])

		val user = newFakeUser(what)
		stub(fakeSession.getValue(any(classOf[String]))).toReturn(user)
		stub(request.getSession()).toReturn(fakeSession)
	}

	private def newFakeUser(email : String) = {
		val result : SUser = mock(classOf[SUser])
		stub(result.getEmail).toReturn(email)
		stub(result.getUsername).toReturn(email)

		result
	}
}