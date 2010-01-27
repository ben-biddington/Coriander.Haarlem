package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import jetbrains.buildServer.serverSide.SBuildServer
import org.coriander.{QueryParser}
import jetbrains.buildServer.web.openapi.WebControllerManager
import coriander.haarlem.Grep
import java.io.File

class LogSearchController(buildServer : SBuildServer)
	extends BaseController(buildServer) {
	def this() = this(null)
	
	def doHandle(
		request : HttpServletRequest,
		response : HttpServletResponse
	) : ModelAndView = {
		require(buildServer != null, "Not much can be done without a build server.")

		val query = new QueryParser().parse(request.getQueryString())

		val buildNumber = buildServer.getBuildNumber
		val artifactsDir = buildServer.getArtifactsDirectory 

		if (query.contains("q")) {
			val result = search(query.get("q").first.value)
			
			new ModelAndView("plugins/coriander-haarlem/search/result.jsp")
		}  else {
			new ModelAndView("plugins/coriander-haarlem/search/default.jsp")
		}
	}

	def register() {
		val mgr = getApplicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])

		mgr.registerController("/log-search.html", this)
	}

	private def search(forWhat : String) = {
		val file = new File(
			buildServer.getServerRootPath + 
			pluginDir +
			"\\bin\\grep.exe"
		)

		val absolutePath = file.getCanonicalPath.replace('\\', '/')

		val where = buildServer.getServerRootPath + pluginDir

		val cmd = absolutePath+ " -r " + forWhat + " " + where

		println("GREP: " + cmd)

		var result = new Grep().run(cmd)

		println("result: " + result)

		new SearchResults(forWhat, result)
	}

	// @spav5: name of PluginDescriptor bean is
	//     plugin-descriptor-<plugin name>,
	// but it can be changed in the future
	private def pluginDir = "plugins\\coriander-haarlem"
}

class SearchResults(val query : String, val result : String)