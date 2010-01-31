package coriander.haarlem.tabs

import javax.servlet.http.HttpServletRequest
import org.springframework.context.{ApplicationContext, ApplicationContextAware}
import jetbrains.buildServer.web.openapi._
import coriander.haarlem.http.query.Query
import jetbrains.buildServer.serverSide.SBuildServer
import java.io.File
import coriander.haarlem.Grep
import coriander.haarlem.controllers.SearchResults

class LogSearchTab(buildServer : SBuildServer, pluginDescriptor : PluginDescriptor)
	extends CustomTab
	with PageExtension
	with ApplicationContextAware
{
	def this() {
		this(null, null);
	}
	
	def register() {
		val mgr = context.getBean("webControllerManager", classOf[WebControllerManager])

		mgr.getPlaceById(PlaceId.BUILD_RESULTS_TAB).addExtension(this)
	}

	def setApplicationContext(context : ApplicationContext) {
		this.context = context
	}

	def fillModel(
		model : java.util.Map[java.lang.String,java.lang.Object],
		request : HttpServletRequest
	) {
		val query = Query(request.getQueryString)

		val artifactsDir = buildServer.getArtifactsDirectory
		val buildNumber = buildServer.getBuildNumber
		val buildId = query.value("buildId")

		val tab 		= query.value("tab")
		val buildTypeId = query.value("buildTypeId")
		
		model.put("buildId"		, buildId)
		model.put("buildNumber"	, buildNumber)
		model.put("buildTypeId"	, buildTypeId)
		model.put("tab"			, tab)
		
		if (query.contains("q")) {
			val keywords = query.value("q")
			val results = search(keywords)
			
			model.put("q", keywords)
			model.put("results", results.result)
		}
	}

	private def search(forWhat : String) = {
		val file = new File(
			buildServer.getServerRootPath +
			pluginDir +
			"\\bin\\grep.exe"
		)

		val absolutePath = file.getCanonicalPath.replace('\\', '/')

		val where = buildServer.getArtifactsDirectory.
			getCanonicalPath.replace('\\', '/')

		val cmd = absolutePath+ " -r " + forWhat + " " + where

		println("GREP: " + cmd)

		var result = new Grep().run(cmd)

		println("result: " + result)

		new SearchResults(forWhat, result)
	}

	private def pluginDir = {
		var result = pluginDescriptor.getPluginResourcesPath.replace('/', '\\')

		if (result.endsWith("\\")) {
			result = result.substring(0, result.length - 1)
		}

		if (result.startsWith("\\")) {
			result = result.substring(1)
		}

		result
	}

	def getTabId 		= "coriander.haarlem.log.search.tab"
	def getTabTitle 	= "Artifact Mine"
	def getIncludeUrl 	= "/plugins/coriander-haarlem/tabs/search/default.jsp"
	def getPluginName 	= "coriander-haarlem"

	override def isAvailable(httpServletRequest : HttpServletRequest) = {
		true
	}

	def isVisible : Boolean = true

	def getCssPaths = new java.util.ArrayList[String]()
	def getJsPaths = new java.util.ArrayList[String]()
	def setPlaceId(placeId : String) = this.placeId = placeId
	def setPluginName(pluginName: String) { }
	def setIncludeUrl(includeUrl: String) { }

	private var context : ApplicationContext = null
	private var placeId : String = ""
}