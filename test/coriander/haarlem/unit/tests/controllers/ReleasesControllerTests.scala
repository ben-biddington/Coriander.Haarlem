package coriander.haarlem.unit.tests.controllers

import org.mockito.Mockito._
import org.mockito.Matchers._
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import jetbrains.buildServer.web.openapi.PluginDescriptor
import coriander.haarlem.controllers.ReleasesController
import coriander.haarlem.core.calendar.{FilterOptions, IBuildFinder}
import org.joda.time.Days._
import coriander.haarlem.matchers.FilterOptionsMatcher._
import org.junit.{Ignore, Before, Test}
import coriander.haarlem.models.ReleasesModel
import jetbrains.buildServer.serverSide.{SFinishedBuild, ProjectManager}
import coriander.haarlem.core.Convert._
import org.springframework.web.servlet.ModelAndView
import jetbrains.buildServer.users.SUser
import javax.servlet.http.{HttpSession, HttpServletResponse, HttpServletRequest}
import coriander.haarlem.core.scheduling.{SystemClock, Clock}
import org.joda.time._
import org.joda.time.DateTimeConstants._
import org.joda.time.DateTimeZone._

class ReleasesControllerTests extends ControllerUnitTest {
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
	def accepts_since_parameter {
		when_since_supplied_as("2-weeks-ago")
		then_builds_are_searched_in(theLastTwoWeeks)
	}

	@Test
	def since_can_be_in_days {
		when_since_supplied_as("1-day-ago")
		then_builds_are_searched_in(theLastDay)
	}

	@Test
	def since_can_be_today {
		when_since_supplied_as("today")
		then_builds_are_searched_in(today)
	}

	@Test
	def since_can_be_yesterday {
		when_since_supplied_as("yesterday")
		then_builds_are_searched_in(theLastDay)
	}

	@Test
	def since_can_have_count_zero_which_returns_builds_since_midnight_today {
		when_since_supplied_as("0-days-ago")
		then_builds_are_searched_in(today)
	}
	
	@Test
	def when_since_is_missing_then_you_get_the_last_twenty_five_builds {
		when_since_not_supplied
		then_we_search_for_the_last_with_no_filter_options(25)
	}

	@Test
	def you_can_only_ask_for_builds_since_a_maximum_90_days_ago_for_performance_reasons {
		when_since_supplied_as("91-days-ago")
		then_builds_are_searched_in(theLastNinetyDays)
		then_there_is_an_error(
			"The requested number of days exceeds the limit of <90>, " +
			"results have been truncated"
		)
	}

	@Test
	def accepts_last_parameter {
		when_last_supplied_as(10)
		then_we_search_for_the_last_with_no_filter_options(10)
	}

	@Test
	def you_can_only_ask_for_up_to_the_last_200_builds_for_performance_reasons {
		when_last_supplied_as(201)
		then_we_search_for_the_last_with_no_filter_options(200)
	}
	
	@Test
	def accepts_matching_parameter {
		when_matching_supplied_with_required_count_as(10, "live")
		then_we_search_for_the_last_with_a_non_null_filter(10)
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

		assertThat(model.getBuilds.size, is(equalTo(2)))
	}

	@Test
	def the_matching_parameter_may_be_omitted {
		when_matching_supplied_with_required_count_as(10, null)
		then_we_search_for_the_last_with_a_non_null_filter(10)
	}

	@Test @Ignore
	def the_matching_parameter_must_be_a_valid_regex_otherwise_an_error_is_returned_and_no_results() {
		fail("PENDING")
		
		given_a_build_finder_that_returns(List(
			newFakeBuildWithDescription("[live] release"),
			newFakeBuildWithDescription("[uat] release")
		))

		when_matching_supplied_with_required_count_as(10, "[this is meant to be invalid[")

		then_there_is_an_error("Invalid regex")
		assertThat(result.getBuilds.size, is(equalTo(0)))
	}

	@Test @Ignore
	def if_you_do_not_supply_since_or_last_then_you_get_the_last_25_results {
		fail("PENDING")
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

	private def given_a_build_finder {
	   given_a_build_finder_that_returns(List())
	}

	private def given_a_build_finder_that_returns(what : List[SFinishedBuild]) {
		stub(buildFinder.find(any(classOf[FilterOptions]))).toReturn(what)
		stub(buildFinder.last(any(classOf[Int]), any(classOf[FilterOptions]))).toReturn(what)
	}

	private def when_since_not_supplied {
		when_since_supplied_as("")	
	}

	private def when_since_supplied_as(what : String) {
		stub(request.getQueryString).toReturn("since=" + what)

		doIt
	}

	private def when_last_supplied_as(howMany : Int) {
		stub(request.getQueryString).toReturn("last=" + howMany.toString)

		doIt
	}

	private def when_matching_supplied_with_required_count_as(howMany : Int, what : String) {
		matching = what

		stub(request.getQueryString).toReturn("last=" + howMany + "&matching=" + what)

		doIt
	}

	private def then_builds_are_searched_in(interval : Interval) {
		verify(buildFinder).find(argThat(hasMatching(interval)))

		// TODO: verify the interval returned is similar to expected
	}

	private def then_we_search_for_the_last_with_no_filter_options(howMany : Int) {
		verify(buildFinder).last(argThat(is(howMany)), argThat(is(FilterOptions.NONE)))
	}

	private def then_we_search_for_the_last_with_a_non_null_filter(howMany : Int) {
		verify(buildFinder).last(argThat(is(howMany)), argThat(is(not(FilterOptions.NONE))))
	}

	private def then_there_is_an_error(what : String) {
		require(result != null)
		assertTrue(
			"Expected <" + result.getErrors + "> " +
			"to contain <" + what + ">", result.getErrors.contains(what)
		)
	}

	private def newFakeBuildWithDescription(what : String) = {
		val result = mock(classOf[SFinishedBuild])
		stub(result.getBuildDescription).toReturn(what)

		result
	}

	private def newFakeUser(email : String) = {
		val result : SUser = mock(classOf[SUser])
		stub(result.getEmail).toReturn(email)
		stub(result.getUsername).toReturn(email)

		result
	}

	private def doIt {
		result = controller.go(request, response).
			getModel.get("results").asInstanceOf[ReleasesModel]
	}

	private def controller = {
		val result = new ReleasesController(
			pluginDescriptor,
			buildFinder,
			clock
		)

		result.setPlonkers(plonkers)

		result
	}

	private var projectManager 	: ProjectManager = null
	private var _controller 		: ReleasesController = null
	private var buildFinder 	: IBuildFinder = null
	private var clock 			: Clock = null
	private var result 			: ReleasesModel = null
	private var matching 		: String = null
	private var plonkers 		: String = null

	private lazy val now 				= new Instant
	private lazy val yesterday 			= new DateMidnight(now).minus(days(1)).toInstant
	private lazy val fourteenDaysAgo 	= new DateMidnight(now).minus(days(14)).toInstant
	private lazy val sevenDaysAgo 		= new DateMidnight(now).minus(days(7)).toInstant
	private lazy val ninetyDaysAgo 		= new DateMidnight(now).minus(days(90)).toInstant
	private lazy val today 				= new Interval(new DateMidnight(now), now)
	private lazy val theLastTwoWeeks 	= new Interval(fourteenDaysAgo, now)
	private lazy val theLastSevenDays 	= new Interval(sevenDaysAgo, now)
	private lazy val theLastNinetyDays 	= new Interval(ninetyDaysAgo, now)
	private lazy val theLastDay 		= new Interval(yesterday, now)
	private lazy val leetOClock 		= new TimeOfDay(13, 37)
	private lazy val justBeforeTwo		= new TimeOfDay(13, 59)
}