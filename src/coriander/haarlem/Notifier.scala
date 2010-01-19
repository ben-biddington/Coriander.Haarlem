package coriander.haarlem

import jetbrains.buildServer.Build
import jetbrains.buildServer.notification.Notificator
import jetbrains.buildServer.responsibility.TestNameResponsibilityEntry
import jetbrains.buildServer.serverSide.{SProject, SBuildType, SRunningBuild}
import jetbrains.buildServer.vcs.VcsRoot
import jetbrains.buildServer.users.SUser

class Notifier extends Notificator {
	def getDisplayName = "xxx"

	def getNotificatorType = "xxx"

	def notifyBuildStarted(runningBuild : SRunningBuild, users : Set[SUser]) { }

    def notifyBuildSuccessful(runningBuild : SRunningBuild, users : Set[SUser]) {}

    def notifyBuildFailed(runningBuild : SRunningBuild, users : Set[SUser]) {}

    def notifyLabelingFailed(
		build : Build,
		vcsRoot : VcsRoot,
		throwable : java.lang.Throwable,
		users : Set[SUser]
	) {}

    def notifyBuildFailing(runningBuild : SRunningBuild, users : Set[SUser]) { }

    def notifyBuildProbablyHanging(runningBuild : SRunningBuild, users : Set[SUser]) { }

    def notifyResponsibleChanged(buildType : SBuildType, users : Set[SUser]) {}

    def notifyResponsibleAssigned(buildType : SBuildType, users : Set[SUser]) {}

    def notifyResponsibleChanged(
		testNameResponsibilityEntry : TestNameResponsibilityEntry,
		testNameResponsibilityEntry1 : TestNameResponsibilityEntry,
		project : SProject,
		users : Set[SUser]
	) { }

    def notifyResponsibleAssigned(
		testNameResponsibilityEntry : TestNameResponsibilityEntry ,
		testNameResponsibilityEntry1 : TestNameResponsibilityEntry ,
		project : SProject,
		users : Set[SUser]
	) {}
}