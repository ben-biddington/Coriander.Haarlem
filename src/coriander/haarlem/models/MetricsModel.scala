package coriander.haarlem.models

import jetbrains.buildServer.users.SUser

class MetricsModel(
	user : SUser,
	dashboards : java.util.List[DashboardInfo]
) {
	def this(user : SUser) { this(user, null) }

	def getUser = user
	def getDashboards = dashboards

	def getError = error
	def setError(err : String) = this.error = err

	def getInfo = info
	def setInfo(info : String) = this.info = info

	def getWatchedBuildCount = error
	def setWatchedBuildCount(count : Int) = this.watchedBuildCount = count

	def getDashboardCount = dashboards.size

	private var error : String = null
	private var info : String = null
	private var watchedBuildCount = 0
}