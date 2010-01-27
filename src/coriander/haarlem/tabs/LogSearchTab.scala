package coriander.haarlem.tabs

import javax.servlet.http.HttpServletRequest
import org.springframework.context.{ApplicationContext, ApplicationContextAware}
import jetbrains.buildServer.web.openapi._

class LogSearchTab
	extends CustomTab
	with PageExtension
	with ApplicationContextAware {

	def register() {
		val mgr : WebControllerManager = applicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])

		println("Place <" + placeId + ">")

		val place = new PlaceId(placeId)

		println("Adding to <" + place.toString + ">")

		mgr.getPlaceById(PlaceId.BUILD_RESULTS_TAB).addExtension(this)
	}

	def setApplicationContext(applicationContext : ApplicationContext) {
		this.applicationContext = applicationContext
	}

	def getTabId 		= "coriander.haarlem.log.search.tab"
	def getTabTitle 	= "Log Search"
	def getIncludeUrl 	= "search/input.jsp"
	def getPluginName 	= "coriander-haarlem"

	def fillModel(
		stringObjectMap : java.util.Map[java.lang.String,java.lang.Object],
		httpServletRequest : HttpServletRequest
	) {

	}

	override def isAvailable(httpServletRequest : HttpServletRequest) = {
		true
	}

	def isVisible : Boolean = {
		true
	}

	def getCssPaths : java.util.List[String] = new java.util.ArrayList[String]()
	def getJsPaths : java.util.List[String] = new java.util.ArrayList[String]()

	def setPlaceId(placeId : String) = this.placeId = placeId
	def setPluginName(pluginName: String) { }
	def setIncludeUrl(includeUrl: String) { }

	private var applicationContext : ApplicationContext = null
	private var placeId : String = ""
}