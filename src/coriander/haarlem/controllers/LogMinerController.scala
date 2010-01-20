package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import jetbrains.buildServer.serverSide.SBuildServer
import javax.management.Query

class LogMinerController(buildServer : SBuildServer) extends BaseController(buildServer) {
	def this() = this(null)
	
	def doHandle(
		request : HttpServletRequest,
		response : HttpServletResponse
	) : ModelAndView = {
		require(buildServer != null, "Not much can be done without a build server.")

		val query = request.getQueryString()

		if (false == query.contains("build")) {
			throw new Exception("Not implemented yet")
		}

		val buildNumber = buildServer.getBuildNumber
		
		response.setStatus(200)

		return null
	}
}