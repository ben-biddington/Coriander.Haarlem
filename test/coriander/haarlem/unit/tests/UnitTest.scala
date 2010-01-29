package coriander.haarlem.unit.tests

import org.mockito.Mockito._
import org.mockito.Matchers._
import jetbrains.buildServer.serverSide.SRunningBuild
import java.util.Calendar._
import java.util.{SimpleTimeZone, Date}

class UnitTest {
    protected def given_any_running_build {
		mockRunningBuild = mock(classOf[SRunningBuild])
		when(mockRunningBuild.getCurrentPath()).
		thenReturn("xxx");
	}

	protected def toDate(
		year : Int,
		month : Int,
		day : Int
	) : Date = {
		val BLANK = 0
		toDateAndTimeGmt(year, month, day, BLANK, BLANK, BLANK )
	}

	protected def toDateAndTimeGmt(
		year : Int, month : Int, 	day : Int,
		hour : Int, minute : Int,	second : Int
	) : Date = {
		val calendar = getInstance(GMT)
		calendar.set(year, month, day, hour, minute, second)
		calendar.set(MILLISECOND, 0)

		calendar.getTime
	}

	private val GMT = new SimpleTimeZone(0, "GMT")
	protected var mockRunningBuild : SRunningBuild = null
}