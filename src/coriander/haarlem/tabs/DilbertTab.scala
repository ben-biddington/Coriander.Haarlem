package coriander.haarlem.tabs

import javax.servlet.http.HttpServletRequest
import org.springframework.context.{ApplicationContext, ApplicationContextAware}
import jetbrains.buildServer.web.openapi.{PlaceId, PagePlaces, PageExtension, CustomTab}

class DilbertTab
	extends CustomTab
	with PageExtension
	with ApplicationContextAware {

	def register() {
		// TODO: Find anj instance of PagePlaces
		//val bean = applicationContext.getBean("pagePlaces").asInstanceOf[PagePlaces];
	}

	def setApplicationContext(applicationContext : ApplicationContext) {
		this.applicationContext = applicationContext
	}
	
	def getTabId 		= "coriander.haarlem.dilbert"
	def getTabTitle 	= "Dilbert"
	def getIncludeUrl 	= "DilbertController"
	def getPluginName 	= "dilbert tab"

	def fillModel(
		stringObjectMap : java.util.Map[java.lang.String,java.lang.Object],
		httpServletRequest : HttpServletRequest
	) {

	}

	override def isAvailable(httpServletRequest : HttpServletRequest) = {
		true
	}

	def isVisible : Boolean = true
	def getCssPaths : java.util.List[String] = new java.util.ArrayList[String]()
	def getJsPaths : java.util.List[String] = new java.util.ArrayList[String]()

	def setPlaceId(placeId : String) { }
	def setPluginName(pluginName: String) { }
	def setIncludeUrl(includeUrl: String) { }

	private var applicationContext : ApplicationContext = null
}