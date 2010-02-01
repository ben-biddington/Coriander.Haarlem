package coriander.haarlem.tabs

import javax.servlet.http.HttpServletRequest
import org.springframework.context.{ApplicationContext, ApplicationContextAware}
import jetbrains.buildServer.web.openapi._
import coriander.haarlem.http.query.Query
import java.io.File
import java.lang.Long._
import coriander.haarlem.Grep
import coriander.haarlem.controllers.SearchResults
import jetbrains.buildServer.serverSide.{SBuild, SBuildServer}
import collection.mutable.ListBuffer
import jetbrains.buildServer.serverSide.buildLog.{LogMessage, BuildLog}
import coriander.haarlem.core.BuildLogSearch

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

		val theBuild = buildServer.findBuildInstanceById(
			parseLong(query.value("buildId"))
		)

		val artifactsDir = theBuild.getArtifactsDirectory.getCanonicalPath
		val buildNumber = buildServer.getBuildNumber

		val buildId 	= query.value("buildId")
		val tab 		= query.value("tab")
		val buildTypeId = query.value("buildTypeId")
		val buildLog 	= theBuild.getBuildLog

		model.put("buildId"				, buildId)
		model.put("buildNumber"			, buildNumber)
		model.put("buildTypeId"			, buildTypeId)
		model.put("tab"					, tab)
		model.put("artifactsDirectory" 	, artifactsDir)
		model.put("buildName" 			, theBuild.getBuildDescription)

		if (query.contains("q") && query.value("q") != null && query.value("q").trim != "") {
			val keywords = query.value("q")
			val results = search(keywords, artifactsDir)
			
			model.put("q"			, keywords)
			model.put("results"		, results.result)
			model.put("buildLog" 	, logSearch(keywords, buildLog))
		}
	}

	private def logSearch(forWhat : String, where : BuildLog) : String = {
		val results = new BuildLogSearch(where).searchFor(forWhat)

		var buffer = new StringBuffer()

		results.foreach(result => buffer.append(result + "\r\n"))

		buffer toString
	}

	private def search(forWhat : String, where : String) = {
		val file = new File(
			buildServer.getServerRootPath +
			pluginDir +
			"\\bin\\search.bat"
		)

		val absolutePath = file.getCanonicalPath.replace('\\', '/')

		val cmd = absolutePath+ " " + forWhat + " " + where.replace('\\', '/')

		var result = new Grep().run(cmd)

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