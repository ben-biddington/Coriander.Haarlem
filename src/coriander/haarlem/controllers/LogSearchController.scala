package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import jetbrains.buildServer.serverSide.SBuildServer
import org.coriander.{QueryParser, Query}

class LogSearchController(buildServer : SBuildServer) extends BaseController(buildServer) {
	def this() = this(null)
	
	def doHandle(
		request : HttpServletRequest,
		response : HttpServletResponse
	) : ModelAndView = {
		require(buildServer != null, "Not much can be done without a build server.")

		val query = new QueryParser().parse(request.getQueryString())

		require(query.contains("build"), "Requires build identifier")
		require(query.contains("q"), "Requires search term")

		val buildNumber = buildServer.getBuildNumber
		
		response.setStatus(200)

		return null
	}
}