package coriander.haarlem.models

import jetbrains.buildServer.users.SUser

class MetricsModel(
	user : SUser,
	builds : java.util.List[DashboardInfo]
) {
	def this(user : SUser) { this(user, null) }

	def getUser = user
	def getBuilds = builds

	def getError = error
	def setError(err : String) = this.error = err

	private var error : String = null
}