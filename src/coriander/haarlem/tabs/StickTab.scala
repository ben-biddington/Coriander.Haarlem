package coriander.haarlem.tabs

import javax.servlet.http.HttpServletRequest
import jetbrains.buildServer.web.openapi._
import jetbrains.buildServer.messages.Status
import jetbrains.buildServer.serverSide.{SBuild, SBuildServer}

class StickTab(buildServer : SBuildServer)
	extends Tab(buildServer)
	with PageExtension
	with CustomTab
{
	def register() {
		val mgr = applicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])

		mgr.getPlaceById(PlaceId.BUILD_RESULTS_TAB).addExtension(this)
	}

	def getTabId 		= "coriander.haarlem.stick.tab"

	def getTabTitle 	= {
		"Fail!"
		// [!] Images break layout pre TeamCity v5.x
		//	<img src="/plugins/coriander-haarlem/server/tabs/fail/fail.gif"
		//	 	width="16" height="16"
		//	 	alt="Stick"
		//	 	style="margin-bottom:3px"
		//	/>.toString
	}

	def getIncludeUrl 	= "/plugins/coriander-haarlem/server/tabs/fail/default.jsp"
	def getPluginName 	= "coriander-haarlem"

	def fillModel(
		model : java.util.Map[java.lang.String,java.lang.Object],
		httpServletRequest : HttpServletRequest
	) {	}

	override def isAvailable(request : HttpServletRequest) : Boolean = {
		val result = failed(theCurrentBuild(request))
		
		result
	}

	private def failed(build : SBuild) : Boolean =
		build != null &&
			build.isFinished &&
			(build.getBuildStatus != Status.NORMAL)

	def isVisible : Boolean = true

	def getCssPaths = new java.util.ArrayList[String]()
	def getJsPaths = new java.util.ArrayList[String]()
	def setPlaceId(placeId : String) = this.placeId = placeId
	def setPluginName(pluginName: String) {}
	def setIncludeUrl(includeUrl: String) {}

	private var placeId : String = ""
}