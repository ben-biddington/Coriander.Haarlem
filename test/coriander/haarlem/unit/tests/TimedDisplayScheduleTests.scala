package coriander.haarlem.unit.tests

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.mockito.Mockito._
import org.mockito.Matchers._
import coriander.haarlem.core.scheduling.{Clock, TimedDisplaySchedule}

class TimedDisplayScheduleTests {
    @Test
	def isVisible_returns_true_in_business_hours {
		val mockClock = mock(classOf[Clock])

		when(mockClock.getHour).
		thenReturn(8)
		
        val schedule = new TimedDisplaySchedule(mockClock)

		assertThat("Should be invisible at 08:00", schedule.isVisible, is(false))

		when(mockClock.getHour).
		thenReturn(9)

		assertThat("Should be invisible at 09:00", schedule.isVisible, is(true))

		when(mockClock.getHour).
		thenReturn(19)

		assertThat("Should be invisible at 19:00", schedule.isVisible, is(false))
    }
}