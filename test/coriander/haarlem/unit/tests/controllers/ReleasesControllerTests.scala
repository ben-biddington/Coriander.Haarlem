package coriander.haarlem.unit.tests.controllers

import org.mockito.Mockito._
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import org.mockito.Matchers._
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import jetbrains.buildServer.web.openapi.PluginDescriptor
import coriander.haarlem.controllers.ReleasesController
import coriander.haarlem.core.calendar.{FilterOptions, IBuildFinder}
import org.joda.time.{Instant, Interval}
import org.joda.time.Days._
import coriander.haarlem.matchers.FilterOptionsMatcher._
import org.junit.{Ignore, Before, Test}
import coriander.haarlem.models.ReleasesModel
import jetbrains.buildServer.serverSide.{SFinishedBuild, ProjectManager}
import coriander.haarlem.core.Convert._
import org.springframework.web.servlet.ModelAndView

class ReleasesControllerTests extends ControllerUnitTest {
	@Before
	def before {
		request 			= mock(classOf[HttpServletRequest])
		response 			= mock(classOf[HttpServletResponse])
		pluginDescriptor 	= mock(classOf[PluginDescriptor])
		buildFinder 		= mock(classOf[IBuildFinder])
		result				= null
		
		given_a_build_finder
		given_a_controller
	}

	@Test
	def accepts_since_parameter {
		when_since_supplied_as("2-weeks-ago")
		then_builds_are_searched_in(theLastTwoWeeks)
	}

	@Test
	def since_can_be_in_days {
		when_since_supplied_as("1-day-ago")
		then_builds_are_searched_in(theLastDay)
	}

	@Test @Ignore
	def since_can_be_today {
		throw new Exception("PENDING")
		when_since_supplied_as("today")
	}

	@Test
	def accepts_last_parameter {
		when_last_supplied_as(10)
		then_we_search_for_the_last_with_no_match(10)
	}

	@Test
	def since_defaults_to_seven_days {
		when_since_not_supplied
		then_builds_are_searched_in(theLastSevenDays)
	}

	@Test
	def accepts_matching_parameter {
		when_matching_supplied_with_required_count_as(10, "live")
		then_we_search_for_the_last_with_an_non_null_filter(10)
	}

	@Test
	def the_matching_parameter_is_a_regex_pattern {
		given_a_build_finder_that_returns(List(
			newFakeBuildWithDescription("[live] release"),
			newFakeBuildWithDescription("[uat] release")
		))

		when_matching_supplied_with_required_count_as(10, "live")
		val result : ModelAndView = controller.go(request, response)
		val model : ReleasesModel = result.getModel.get("results").asInstanceOf[ReleasesModel]
		println(model.getBuilds.size)

		assertThat(model.getBuilds.size, is(equalTo(2)))
	}

	@Test @Ignore
	def you_can_only_ask_for_up_to_the_last_100_for_performance_reasons {
		 throw new Exception("PENDING")
	}

	@Test @Ignore
	def you_can_only_ask_for_builds_since_a_maximum_31_days_ago_for_performance_reasons {
		throw new Exception("PENDING")
	}

	@Test @Ignore
	def if_you_do_not_supply_since_or_last_then_you_get_the_last_25_results {
		throw new Exception("PENDING")
	}

	private def given_a_build_finder {
	   given_a_build_finder_that_returns(List())
	}

	private def given_a_build_finder_that_returns(what : List[SFinishedBuild]) {
		stub(buildFinder.find(any(classOf[FilterOptions]))).toReturn(what)
		stub(buildFinder.last(any(classOf[Int]), any(classOf[FilterOptions]))).toReturn(what)
	}

	private def given_a_controller {
		controller = new ReleasesController(
			pluginDescriptor,
			buildFinder
		);
	}

	private def when_since_not_supplied {
		when_since_supplied_as("")	
	}

	private def when_since_supplied_as(what : String) {
		stub(request.getQueryString).toReturn("since=" + what)

		controller.go(request, response)
	}

	private def when_last_supplied_as(howMany : Int) {
		stub(request.getQueryString).toReturn("last=" + howMany.toString)

		controller.go(request, response)
	}

	private def when_matching_supplied_with_required_count_as(howMany : Int, what : String) {
		matching = what

		stub(request.getQueryString).toReturn("last=" + howMany + "&matching=" + what)

		result = controller.go(request, response).
			getModel.get("results").asInstanceOf[ReleasesModel]
	}

	private def then_builds_are_searched_in(interval : Interval) {
		verify(buildFinder).find(argThat(hasMatching(interval)))
	}

	private def then_we_search_for_the_last_with_no_match(howMany : Int) {
		verify(buildFinder).last(argThat(is(howMany)), argThat(is(FilterOptions.NONE)))
	}

	private def then_we_search_for_the_last_with_an_non_null_filter(howMany : Int) {
		verify(buildFinder).last(argThat(is(howMany)), argThat(is(not(FilterOptions.NONE))))
	}

	private def newFakeBuildWithDescription(what : String) = {
		val result = mock(classOf[SFinishedBuild])
		stub(result.getBuildDescription).toReturn(what)

		result
	}

	private var projectManager 	: ProjectManager = null
	private var controller 		: ReleasesController = null
	private var buildFinder 	: IBuildFinder = null
	private var result 			: ReleasesModel = null
	private var matching 		: String = null

	private lazy val now 				= new Instant
	private lazy val yesterday 			= new Instant().minus(days(1).toStandardDuration)
	private lazy val fourteenDaysAgo 	= new Instant().minus(days(14).toStandardDuration)
	private lazy val sevenDaysAgo 		= new Instant().minus(days(7).toStandardDuration)
	private lazy val theLastTwoWeeks 	= new Interval(fourteenDaysAgo, now)
	private lazy val theLastSevenDays 	= new Interval(sevenDaysAgo, now)
	private lazy val theLastDay 		= new Interval(yesterday, now)
}