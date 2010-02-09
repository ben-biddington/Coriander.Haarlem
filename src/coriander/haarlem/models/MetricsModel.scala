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

	private var error : String = null
}