package coriander.haarlem.tabs

import javax.servlet.http.HttpServletRequest
import org.springframework.context.{ApplicationContext, ApplicationContextAware}
import jetbrains.buildServer.web.openapi._
import jetbrains.buildServer.serverSide.{SBuildServer}
import java.lang.Long._

import jetbrains.buildServer.messages.Status
import org.coriander.{QueryParser}

class DilbertTab(buildServer : SBuildServer)
	extends CustomTab
	with PageExtension
	with ApplicationContextAware
{
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

	override def isAvailable(request : HttpServletRequest) : Boolean = {
		val query = new QueryParser().parse(request.getQueryString())

		if (query.contains(QUERY_BUILD_ID)) {
			return buildSuccessful(parseLong(
				query.get(QUERY_BUILD_ID).first.value
			))
		}

		return false
	}

	def isVisible : Boolean = true

	def getCssPaths : java.util.List[String] = new java.util.ArrayList[String]()
	def getJsPaths : java.util.List[String] = new java.util.ArrayList[String]()

	def setPlaceId(placeId : String) = this.placeId = placeId
	def setPluginName(pluginName: String) { }
	def setIncludeUrl(includeUrl: String) { }

	private def buildSuccessful(buildId : Long) = {
		// TODO: Find the current build being viewed (query string)
		buildServer.findBuildInstanceById(buildId).getBuildStatus == Status.NORMAL
	}

	private var applicationContext : ApplicationContext = null
	private var placeId : String = ""
	private val QUERY_BUILD_ID = "buildId"
}