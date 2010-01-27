package coriander.haarlem.tabs

import javax.servlet.http.HttpServletRequest
import org.springframework.context.{ApplicationContext, ApplicationContextAware}
import jetbrains.buildServer.web.openapi._

class DilbertTab
	extends CustomTab
	with PageExtension
	with ApplicationContextAware {

	def register() {
		val mgr = applicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])

		val place = new PlaceId(placeId)
		
		println("Adding dilbert to <" + place.toString + ">")

		mgr.getPlaceById(PlaceId.BUILD_RESULTS_TAB).addExtension(this)
	}

	def setApplicationContext(applicationContext : ApplicationContext) {
		this.applicationContext = applicationContext
	}
	
	def getTabId 		= "coriander.haarlem.dilbert.tab"
	def getTabTitle 	= "Dilbert"
	def getIncludeUrl 	= "dilbert.jsp"
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
		// TODO: Build was successful, otherwise a failblog entry.
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