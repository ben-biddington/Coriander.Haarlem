package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import jetbrains.buildServer.web.openapi.WebControllerManager
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}

abstract class Controller extends BaseController {
	def register() {
		val mgr = getApplicationContext.getBean(
			"webControllerManager",
			classOf[WebControllerManager]
		)

		mgr.registerController(route, this)
	}

	def setRoute(route : String) {
		this.route = route
	}

	def go(request : HttpServletRequest, response : HttpServletResponse) = {
		doHandle(request, response)
	}

	protected var route : String = null
}