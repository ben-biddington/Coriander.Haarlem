package coriander.haarlem.tabs

import jetbrains.buildServer.serverSide.SBuildServer
import jetbrains.buildServer.web.openapi.{PlaceId, WebControllerManager, CustomTab, PageExtension}
import org.springframework.context.{ApplicationContext, ApplicationContextAware}
import javax.servlet.http.HttpServletRequest

class MetricsTab(buildServer : SBuildServer)
	extends CustomTab
	with PageExtension
	with ApplicationContextAware
{
	def register() {
		val mgr = applicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])

		mgr.getPlaceById(PlaceId.MY_TOOLS_TABS).addExtension(this)
	}

	def setApplicationContext(applicationContext : ApplicationContext) {
		this.applicationContext = applicationContext
	}

	def getTabId 		= "coriander.haarlem.metrics.tab"
	def getTabTitle 	= "Metrics"
	def getIncludeUrl 	= "/plugins/coriander-haarlem/tabs/metrics/default.jsp"
	def getPluginName 	= "coriander-haarlem"

	def fillModel(
		model : java.util.Map[java.lang.String,java.lang.Object],
		request : HttpServletRequest
	) {
		
	}

	override def isAvailable(request : HttpServletRequest) : Boolean = true

	def isVisible : Boolean = true
	def getCssPaths = new java.util.ArrayList[String]()
	def getJsPaths = new java.util.ArrayList[String]()
	def setPlaceId(placeId : String) = this.placeId = placeId
	def setPluginName(pluginName: String) { }
	def setIncludeUrl(includeUrl: String) { }

	private var applicationContext : ApplicationContext = null
	private var placeId 		= ""
	private val QUERY_BUILD_ID 	= "buildId"
}