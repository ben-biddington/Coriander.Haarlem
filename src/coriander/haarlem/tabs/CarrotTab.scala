package coriander.haarlem.tabs

import javax.servlet.http.HttpServletRequest
import jetbrains.buildServer.web.openapi._
import jetbrains.buildServer.messages.Status
import jetbrains.buildServer.serverSide.{SBuild, SBuildServer}

class CarrotTab(buildServer : SBuildServer)
	extends Tab(buildServer)
	with PageExtension
	with CustomTab
{
	def register() {
		val mgr = applicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])
		
		mgr.getPlaceById(PlaceId.BUILD_RESULTS_TAB).addExtension(this)
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
		successful(theCurrentBuild(request))
	}

	def isVisible : Boolean = true
	def getCssPaths = new java.util.ArrayList[String]()
	def getJsPaths = new java.util.ArrayList[String]()
	def setPlaceId(placeId : String) = this.placeId = placeId
	def setPluginName(pluginName: String) { }
	def setIncludeUrl(includeUrl: String) { }

	private def buildSuccessful(buildId : Long) =
		successful(buildServer.findBuildInstanceById(buildId))

	private def successful(build : SBuild) : Boolean =
		build != null &&
			build.isFinished &&
			build .getBuildStatus == Status.NORMAL

	private var placeId = ""
}