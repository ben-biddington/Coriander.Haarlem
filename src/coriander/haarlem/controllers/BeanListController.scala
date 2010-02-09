package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import java.io.{PrintWriter}
import coriander.haarlem.core.Convert
import jetbrains.buildServer.web.openapi.{PluginDescriptor, WebControllerManager}
import jetbrains.buildServer.serverSide.SBuildServer
import coriander.haarlem.models.BeanListModel

class BeanListController(
	buildServer : SBuildServer,
	pluginDescriptor : PluginDescriptor
) extends BaseController {
	override protected def doHandle(
		request : HttpServletRequest,
		response : HttpServletResponse
	) : ModelAndView = {

		val test = getApplicationContext().getBean("buildGraphController")
		
		val allBeans = getApplicationContext().getBeanDefinitionNames.
			toList.sort((left, right) => left < right)

		val viewPath = pluginDescriptor.getPluginResourcesPath + "server/beans/default.jsp"

		println("View path: " + viewPath)

		val result = new BeanListModel(Convert.toJavaList(allBeans))

		new ModelAndView(
			viewPath,
			"results",
		    result
		)
	}

	def register() {
		val mgr = getApplicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])

		mgr.registerController("/beans.html", this)
	}
	
	private def printInterfaces(c : java.lang.Object, out : PrintWriter) {
    	val interfaces = c.getClass.getInterfaces

		if (false == interfaces.isEmpty) {
			out.write("<ul>")
			interfaces.foreach(interface => out.write("<li>[" + c.getClass.toString + "]" + interface + "</li>"));
			out.write("</ul>")
		}
	}

	private val BEAN_NAME = "beanListController"
}