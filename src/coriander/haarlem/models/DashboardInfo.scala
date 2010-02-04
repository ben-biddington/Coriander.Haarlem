package coriander.haarlem.models

import jetbrains.buildServer.serverSide.SBuildType

class DashboardInfo(build : SBuildType, html : String) {
	def getBuild = build
	def getHtml = html
}