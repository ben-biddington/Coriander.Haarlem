package coriander.haarlem.controllers

import java.lang.Long._
import java.io.File
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import org.springframework.web.servlet.ModelAndView
import collection.mutable.ListBuffer
import jetbrains.buildServer.controllers.BaseController
import jetbrains.buildServer.web.openapi.{PluginDescriptor, WebControllerManager}
import jetbrains.buildServer.users.SUser
import jetbrains.buildServer.serverSide._
import coriander.haarlem.http.query.Query
import coriander.haarlem.core.{Dashboard, Convert}
import coriander.haarlem.models.{DashboardInfo, MetricsModel}
import jetbrains.buildServer.notification.{Notificator, NotificatorRegistry, NotificationRule, NotificationRulesManager}
import coriander.haarlem.core.dashboards.DashboardXmlFinder

class MetricsController(
	buildServer : SBuildServer,
	projectManager : ProjectManager,
	pluginDescriptor : PluginDescriptor,
	notificationRulesManager : NotificationRulesManager,
	notificatorRegistry : NotificatorRegistry
) extends BaseController {
	def register() {
		val mgr = getApplicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])

		mgr.registerController("/metrics.html", this)
	}

	def go(request : HttpServletRequest, response : HttpServletResponse) = {
		doHandle(request, response)
	}

	override protected def doHandle(
		request : HttpServletRequest,
		response : HttpServletResponse
	) : ModelAndView = {

		val query = Query(request.getQueryString)

		val userId = query.value("user")

		var user : SUser = null

		if (userId != null) {
			user = buildServer.getUserModel.findUserById(parseLong(userId))

			if (user != null) {
				return run(user)
			}
		}

		response.setContentType("text/html")

		if (null == userId) {
			response.setStatus(400)
			response.getWriter.write("Missing the &lt;user&gt; parameter.")
		}  else if (null == user) {
			response.setStatus(404)
			response.getWriter.write(
				"Not found. " +
				"The user with id &lt;" + userId + "&gt; could not be found."
			)			
		}

		null
	}

	private def run(user: SUser) = {
		val allBuildsWithDashboards = findAllBuildsWithDashboards(user)

		var temp = new ListBuffer[DashboardInfo]
		val dashboardRenderer = new Dashboard()

		allBuildsWithDashboards.foreach(build => {
			temp += new DashboardInfo(	
				build,
				dashboardRenderer.run(build, xsl)
			)
		})
		
		val result = new MetricsModel(user, Convert.toJavaList(temp.toList))

		new ModelAndView(
			pluginDescriptor.getPluginResourcesPath + "/server/metrics/default.jsp",
			"results",
			result
		)
	}

	private def findAllBuildsWithDashboards(user : SUser) : List[SBuildType] = {
		val buildIds = getWatchedBuildIds(user.getId)

		buildIds.
			map(projectManager.findBuildTypeById(_)).
			filter(hasDashboard(_))
	}
	
	private def getWatchedBuildIds(userId : Long) : List[String] = {
		val result = new ListBuffer[String]
		val rules = getAllNotificationRules(userId)

		Convert.toScalaList(rules).foreach((rule : NotificationRule) => {
			val temp = rule.getWatchedBuilds.getBuildTypeIds;
			temp.toArray.foreach(buildTypeID => {
				if (false == result.contains(buildTypeID.toString)) {
					result.append(buildTypeID.toString)
				}
			})
		})

		result toList
	}

	private def getAllNotificationRules(userId : Long) = {
		val allNotificators = Convert.toScalaList(notificatorRegistry.getNotificators.iterator)

		var rules = new java.util.ArrayList[NotificationRule]()

		allNotificators.foreach((notificator : Notificator) => {
			rules.addAll(
				notificationRulesManager.getAllUserNotificationRules(
					userId,
					notificator.getNotificatorType
				)
			)
		})

		rules
	}
	
	private def hasDashboard(buildType : SBuildType) = dashboardFinder.hasDashboard(buildType)

	lazy val xsl = new File(
		buildServer.getServerRootPath +
		pluginDescriptor.getPluginResourcesPath +
		"/server/metrics/dashboard.xsl"
	)

	lazy val dashboardFinder = new DashboardXmlFinder()
}

