package coriander.haarlem.tabs

import javax.servlet.http.HttpServletRequest
import org.springframework.context.{ApplicationContext, ApplicationContextAware}
import jetbrains.buildServer.web.openapi._
import jetbrains.buildServer.serverSide.{SBuildServer}
import java.lang.Long._

import jetbrains.buildServer.messages.Status
import coriander.haarlem.http.query.Query

class CarrotTab(buildServer : SBuildServer)
	extends CustomTab
	with PageExtension
	with ApplicationContextAware
{
	def register() {
		val mgr = applicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])
		
		mgr.getPlaceById(PlaceId.BUILD_RESULTS_TAB).addExtension(this)
	}

	def setApplicationContext(applicationContext : ApplicationContext) {
		this.applicationContext = applicationContext
	}
	
	def getTabId 		= "coriander.haarlem.carrot.tab"

	def getTabTitle 	= {
		"Dilbert"
		// [!] Images break layout pre TeamCity v5.x
		//<img src="/plugins/coriander-haarlem/server/tabs/dilbert/dilbert.gif"></img>.toString
	}

	def getIncludeUrl 	= "/plugins/coriander-haarlem/server/tabs/dilbert/default.jsp"
	def getPluginName 	= "coriander-haarlem"

	def fillModel(
		model : java.util.Map[java.lang.String,java.lang.Object],
		httpServletRequest : HttpServletRequest
	) { }

	override def isAvailable(request : HttpServletRequest) : Boolean = {
		val query = new Query(request.getQueryString)

		if (query.contains(QUERY_BUILD_ID)) {
			return buildSuccessful(parseLong(
				query.value(QUERY_BUILD_ID)
			))
		}

		return false
	}

	def isVisible : Boolean = true
	def getCssPaths = new java.util.ArrayList[String]()
	def getJsPaths = new java.util.ArrayList[String]()
	def setPlaceId(placeId : String) = this.placeId = placeId
	def setPluginName(pluginName: String) { }
	def setIncludeUrl(includeUrl: String) { }

	private def buildSuccessful(buildId : Long) = {
		var theBuild = buildServer.findBuildInstanceById(buildId)
		
		theBuild.getBuildStatus == Status.NORMAL && theBuild.isFinished
	}

	private var applicationContext : ApplicationContext = null
	private var placeId 		= ""
	private val QUERY_BUILD_ID 	= "buildId"
}