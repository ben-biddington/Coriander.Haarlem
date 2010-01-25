package coriander.haarlem.tabs

import jetbrains.buildServer.web.openapi.{PageExtension, CustomTab}
import javax.servlet.http.HttpServletRequest

class DilbertTab extends CustomTab with PageExtension {
	def getTabId = "coriander.haarlem.dilbert"
	def getTabTitle = "Dilbert"
	def getIncludeUrl = "DilbertController"
	def getPluginName = "dilbert tab"

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
}