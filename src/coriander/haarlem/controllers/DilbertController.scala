package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import org.springframework.web.servlet.ModelAndView

class DilbertController extends BaseController {
	override protected def doHandle(
		httpServletRequest : HttpServletRequest,
		httpServletResponse : HttpServletResponse
	) : ModelAndView = {
		// TODO: Still crap, shouldn't need to resolve view paths myself
		new ModelAndView("plugins/coriander-haarlem/default.jsp")
	}

	def register() {
		// TODO: Register route here
		// see: http://www.jetbrains.net/confluence/display/TCD5/Web+UI+Extensions#WebUIExtensions-DevelopPageExtention
		println("Registering DilbertController")

		logger.info("Registering DilbertController")
	}
}