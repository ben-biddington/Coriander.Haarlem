package coriander.haarlem.controllers

import jetbrains.buildServer.controllers.BaseController
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.{HttpServletResponse, HttpServletRequest}
import jetbrains.buildServer.serverSide.SBuildServer
import org.coriander.{QueryParser}
import coriander.haarlem.Grep
import java.io.File
import jetbrains.buildServer.web.openapi.{PluginDescriptor, WebControllerManager}

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
			val results = search(query.get("q").first.value)

			new ModelAndView(
				"plugins/coriander-haarlem/search/result.jsp",
				"results",
				results
			)
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

		val where = (buildServer.getServerRootPath + pluginDir).replace('\\', '/')

		val cmd = absolutePath+ " -r " + forWhat + " " + where

		println("GREP: " + cmd)

		var result = new Grep().run(cmd)

		println("result: " + result)

		new SearchResults(forWhat, result)
	}

	// @spav5: name of PluginDescriptor bean is
	//     plugin-descriptor-<plugin name>,
	// but it can be changed in the future
	private def pluginDir = {
		val bean : PluginDescriptor = getApplicationContext.getBean(
			"plugin-descriptor-" + "coriander-haarlem",
			classOf[PluginDescriptor]
		)

		var result = bean.getPluginResourcesPath.replace('/', '\\')

		if (result.endsWith("\\")) {
			result = result.substring(0, result.length - 1)
		}

		if (result.startsWith("\\")) {
			result = result.substring(1)
		}

		result
	}
}

class SearchResults(val query : String, val result : String) {
	def getResult = result
	def getQuery = query
}