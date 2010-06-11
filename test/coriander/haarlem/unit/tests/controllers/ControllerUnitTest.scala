package coriander.haarlem.unit.tests.controllers

import coriander.haarlem.unit.tests.UnitTest
import jetbrains.buildServer.notification.{NotificationRulesManager, NotificatorRegistry}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import jetbrains.buildServer.serverSide.SBuildServer
import jetbrains.buildServer.web.openapi.PluginDescriptor

class ControllerUnitTest extends UnitTest {
	protected var notificatorRegistry : NotificatorRegistry = null
	protected var notificationRulesManager : NotificationRulesManager = null
	protected var request : HttpServletRequest = null
	protected var response : HttpServletResponse  = null
	protected var buildServer : SBuildServer = null
	protected var pluginDescriptor : PluginDescriptor = null
}