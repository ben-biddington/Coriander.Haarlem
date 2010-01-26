package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView
import jetbrains.buildServer.web.openapi.WebControllerManager

class DilbertController extends BaseController {
	override protected def doHandle(
		httpServletRequest : HttpServletRequest,
		httpServletResponse : HttpServletResponse
	) : ModelAndView = {
		// TODO: Still crap, shouldn't need to resolve view paths myself
		new ModelAndView("plugins/coriander-haarlem/default.jsp")
	}

	def register() {
		val mgr = getApplicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])

		mgr.registerController("/dilbert.html", this)
	}
}