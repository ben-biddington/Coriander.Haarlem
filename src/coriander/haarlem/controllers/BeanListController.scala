package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import org.springframework.web.servlet.ModelAndView
import coriander.haarlem.core.Convert._
import jetbrains.buildServer.web.openapi.{PluginDescriptor, WebControllerManager}
import jetbrains.buildServer.serverSide.SBuildServer
import coriander.haarlem.models.BeanListModel
import javax.servlet.http.{HttpSession, HttpServletResponse, HttpServletRequest}

class BeanListController(
	buildServer : SBuildServer,
	pluginDescriptor : PluginDescriptor
) extends BaseController {
	override protected def doHandle(
		request : HttpServletRequest,
		response : HttpServletResponse
	) : ModelAndView = {
		val ctx = getApplicationContext
		
		val allBeans = ctx.getBeanDefinitionNames.toList.sort(_<_).map(name =>
			name + " (" + ctx.getBean(name) + ") "
		)

		val session : HttpSession = request.getSession
		val sessionInfo = session.getValueNames.map(name =>
			name + " = \"" + session.getValue(name) + "\" (" + session.getValue(name).getClass + ")"
		)

		val viewPath = pluginDescriptor.getPluginResourcesPath + "server/beans/default.jsp"

		val result = new BeanListModel(
			toJavaList(allBeans),
			ctx.getBeanDefinitionCount,
			toJavaList(sessionInfo)
		)

		new ModelAndView(viewPath, "results", result)
	}

	def register() {
		val mgr = getApplicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])

		mgr.registerController("/beans.html", this)
	}
}