package coriander.haarlem.unit.tests.controllers

import org.joda.time.{TimeOfDay, Interval, DateMidnight, Instant}
import coriander.haarlem.models.ReleasesModel
import coriander.haarlem.core.scheduling.Clock
import coriander.haarlem.core.calendar.IBuildFinder
import jetbrains.buildServer.serverSide.ProjectManager
import org.joda.time.Days._
import coriander.haarlem.controllers.ReleasesController

class ReleasesControllerUnitTest extends ControllerUnitTest {
	protected def doIt {
		result = controller.go(request, response).
			getModel.get("results").asInstanceOf[ReleasesModel]
	}

	protected def controller = {
		val result = new ReleasesController(
			pluginDescriptor,
			buildFinder,
			clock
		)

		result.setPlonkers(plonkers)

		result
	}

	protected var projectManager 	: ProjectManager = null
	protected var _controller 		: ReleasesController = null
	protected var buildFinder 		: IBuildFinder = null
	protected var clock 			: Clock = null
	protected var result 			: ReleasesModel = null
	protected var matching 			: String = null
	protected var plonkers 			: String = null

	protected lazy val now 					= new Instant
	protected lazy val yesterday 			= new DateMidnight(now).minus(days(1)).toInstant
	protected lazy val fourteenDaysAgo 		= new DateMidnight(now).minus(days(14)).toInstant
	protected lazy val sevenDaysAgo 		= new DateMidnight(now).minus(days(7)).toInstant
	protected lazy val ninetyDaysAgo 		= new DateMidnight(now).minus(days(90)).toInstant
	protected lazy val today 				= new Interval(new DateMidnight(now), now)
	protected lazy val theLastTwoWeeks 		= new Interval(fourteenDaysAgo, now)
	protected lazy val theLastSevenDays 	= new Interval(sevenDaysAgo, now)
	protected lazy val theLastNinetyDays 	= new Interval(ninetyDaysAgo, now)
	protected lazy val theLastDay 			= new Interval(yesterday, now)
	protected lazy val leetOClock 			= new TimeOfDay(13, 37)
	protected lazy val justBeforeTwo		= new TimeOfDay(13, 59)
}