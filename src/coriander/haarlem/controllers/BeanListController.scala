package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import java.io.{PrintWriter}
import jetbrains.buildServer.web.openapi.WebControllerManager

class BeanListController extends BaseController {
	override protected def doHandle(
		request : HttpServletRequest,
		response : HttpServletResponse
	) : ModelAndView = {
		response.setContentType("text/html")

		response.getWriter.write("<html><head></head><body>")

		printBeans(response.getWriter)

		response.getWriter.write("</body></html>")
		
		null
	}

	private def printBeans(out : PrintWriter) {
		val allBeans = getApplicationContext().getBeanDefinitionNames.
			toList.sort((left, right) => left < right)

		out write("<ul>")

		allBeans.foreach(name => {
			val bean = getApplicationContext().getBean(name)
			out write(
				"<li>Bean: " + name + "</li>" +
				printInterfaces(bean, out)
			)	
		});

		out write("</ul>")
	}

	private def printInterfaces(c : java.lang.Object, out : PrintWriter) {
    	val interfaces = c.getClass.getInterfaces

		if (false == interfaces.isEmpty) {
			out.write("<ul>")
			interfaces.foreach(interface => out.write("<li>[" + c.getClass.toString + "]" + interface + "</li>"));
			out.write("</ul>")
		}
	}
	
	def register() {
		val mgr : WebControllerManager = getApplicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])

		mgr.registerController("/beans.html", this)
	}
}