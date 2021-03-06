package coriander.haarlem.unit.tests.controllers

import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import coriander.haarlem.unit.tests.UnitTest
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.junit.{Before, Test}
import java.util.ArrayList
import jetbrains.buildServer.users.{SUser, UserModel}
import org.springframework.web.servlet.ModelAndView
import coriander.haarlem.controllers.MetricsController
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import coriander.haarlem.models.MetricsModel
import jetbrains.buildServer.serverSide.{ProjectManager, SBuildServer}
import jetbrains.buildServer.web.openapi.PluginDescriptor
import jetbrains.buildServer.notification.{NotificationRulesManager, NotificationRule, NotificatorRegistry, Notificator}
import java.io.{PrintWriter, StringWriter}

class MetricsControllerTests extends ControllerUnitTest {
	@Before
	def before {
		buildServer = mock(classOf[SBuildServer])
		projectManager = mock(classOf[ProjectManager])
		pluginDescriptor = mock(classOf[PluginDescriptor])
		notificatorRegistry = mock(classOf[NotificatorRegistry])
		notificationRulesManager = mock(classOf[NotificationRulesManager])
		request = mock(classOf[HttpServletRequest])
		response = mock(classOf[HttpServletResponse])
		result = null

		given_writable_response
	}

    @Test
	def given_I_am_watching_no_builds_then_empty_model_is_returned { 
        given_I_am_watching_no_builds_at_all
		given_user_exists
		given_valid_request

		when_I_make_request

		val modelMap = result.getModel
		val model : MetricsModel = modelMap.get("results").asInstanceOf[MetricsModel]
		
		assertThat(model.getDashboardCount, is(equalTo(0)))
    }

	@Test
	def when_the_user_parameter_is_missing_then_400_error_is_returned { 
		when(request.getQueryString).thenReturn("")

		when_I_make_request

		verify(response).setStatus(400)

		then_content_type_is_html
    }

	@Test
	def when_the_user_parameter_is_supplied_but_does_not_exist_then_404_returned { 
		given_user_does_not_exist

		given_valid_request
		
		when_I_make_request

		verify(response).setStatus(404)

		then_content_type_is_html
    }

	private def given_I_am_watching_no_builds_at_all {
		when(
			notificationRulesManager.getAllUserNotificationRules(
				USERID ,
				NOTIFICATOR_TYPE_NAME
			)
		).thenReturn(new ArrayList[NotificationRule])
	}

	private def given_valid_request {
		when(request.getQueryString).thenReturn("user=" + USERID)
	}

	private def given_user_exists {
		val userModel : UserModel = mock(classOf[UserModel])
		var fakeUser : SUser = mock(classOf[SUser])
		when(fakeUser.getId).thenReturn(USERID)
		when(userModel.findUserById(USERID)).thenReturn(fakeUser)
		when(buildServer.getUserModel).thenReturn(userModel)
	}

	private def given_user_does_not_exist {
		val userModel = mock(classOf[UserModel])
		when(userModel.findUserById(USERID)).thenReturn(null)
		when(buildServer.getUserModel).thenReturn(userModel)
	}

	private def given_a_single_notificator {
		var list : java.util.List[Notificator] = new java.util.ArrayList[Notificator]

		val fakeNotificator = mock(classOf[Notificator])
		when(fakeNotificator.getNotificatorType).thenReturn(NOTIFICATOR_TYPE_NAME)
		when(fakeNotificator.getDisplayName).thenReturn("Fake Notificator")

		list.add(fakeNotificator)
		
		when(notificatorRegistry.getNotificators).thenReturn(list)
	}

	private def given_writable_response {
		val printWriter = new PrintWriter(new StringWriter())
		
		when(response.getWriter).thenReturn(printWriter)
	}

	private def when_I_make_request {
		result = newController.go(request, response)
	}

	private def then_content_type_is_html {
		verify(response).setContentType("text/html")
	}

	private def newController = {
		new MetricsController(
			buildServer,
			projectManager,
			pluginDescriptor,
			notificationRulesManager,
			notificatorRegistry
		)
	}

	private val USERID = 1
	private val NOTIFICATOR_TYPE_NAME = "FAKE"
	private var result : ModelAndView = null
	private var projectManager : ProjectManager = null
}