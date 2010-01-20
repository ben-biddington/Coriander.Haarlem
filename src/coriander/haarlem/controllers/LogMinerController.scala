package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import jetbrains.buildServer.serverSide.SBuildServer

class LogMinerController(buildServer : SBuildServer) extends BaseController(buildServer) {
	def this() = this(null)
	
	def doHandle(request : HttpServletRequest, response : HttpServletResponse) : ModelAndView = {		
		throw new Exception("Not implemented yet")
	}
}