package coriander.haarlem.unit.tests.controllers.releases

import org.junit.{Ignore, Before, Test}
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.joda.time.{TimeOfDay, Interval, DateMidnight, Instant}
import coriander.haarlem.models.ReleasesModel
import coriander.haarlem.core.scheduling.Clock
import org.joda.time.Days._
import coriander.haarlem.controllers.ReleasesController
import coriander.haarlem.unit.tests.controllers.ControllerUnitTest
import coriander.haarlem.core.calendar.{FilterOptions, IBuildFinder}
import jetbrains.buildServer.serverSide.{SFinishedBuild, ProjectManager}
import coriander.haarlem.matchers.FilterOptionsMatcher._

class ReleasesControllerUnitTest extends ControllerUnitTest {
	protected def given_a_build_finder {
	   given_a_build_finder_that_returns(List())
	}

	protected def given_a_build_finder_that_returns(what : List[SFinishedBuild]) {
		stub(buildFinder.find(any(classOf[FilterOptions]))).toReturn(what)
		stub(buildFinder.last(any(classOf[Int]), any(classOf[FilterOptions]))).toReturn(what)
	}

	protected def then_builds_are_searched_in(interval : Interval) {
		verify(buildFinder).find(argThat(hasMatching(interval)))
	}

	protected def then_we_search_for_the_last_with_no_filter_options(howMany : Int) {
		verify(buildFinder).last(argThat(is(howMany)), argThat(is(FilterOptions.NONE)))
	}

	protected def then_we_search_for_the_last_with_a_non_null_filter(howMany : Int) {
		verify(buildFinder).last(argThat(is(howMany)), argThat(is(not(FilterOptions.NONE))))
	}

	protected def then_there_is_an_error(what : String) {
		require(result != null)
		assertTrue(
			"Expected <" + result.getErrors + "> " +
			"to contain <" + what + ">", result.getErrors.contains(what)
		)
	}

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

	protected def newFakeBuildWithDescription(what : String) = {
		val result = mock(classOf[SFinishedBuild])
		stub(result.getBuildDescription).toReturn(what)

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