package coriander.haarlem

import jetbrains.buildServer.Build
import jetbrains.buildServer.notification.Notificator
import jetbrains.buildServer.responsibility.TestNameResponsibilityEntry
import jetbrains.buildServer.serverSide.{SProject, SBuildType, SRunningBuild}
import jetbrains.buildServer.vcs.VcsRoot

class Notifier extends Notificator {
	def getDisplayName = "xxx"

	def getNotificatorType = "xxx"

	def notifyBuildStarted(
		sRunningBuild : SRunningBuild ,
		sUsers : java.util.Set[jetbrains.buildServer.users.SUser]
	) { }

    def notifyBuildSuccessful(
		sRunningBuild : SRunningBuild, 
		users : java.util.Set[jetbrains.buildServer.users.SUser]
	) {}

    def notifyBuildFailed(
		sRunningBuild : SRunningBuild,
		users : java.util.Set[jetbrains.buildServer.users.SUser]
	) {}

    def notifyLabelingFailed(
		build : Build,
		vcsRoot : VcsRoot,
		throwable : java.lang.Throwable,
		users : java.util.Set[jetbrains.buildServer.users.SUser]
	) {}

    def notifyBuildFailing(
		sRunningBuild : SRunningBuild ,
		users : java.util.Set[jetbrains.buildServer.users.SUser]
	) { }

    def notifyBuildProbablyHanging(
		sRunningBuild : SRunningBuild ,
		users : java.util.Set[jetbrains.buildServer.users.SUser]
	) { }

    def notifyResponsibleChanged(
		sBuildType : SBuildType, 
		users : java.util.Set[jetbrains.buildServer.users.SUser]
	) {}

    def notifyResponsibleAssigned(
		sBuildType : SBuildType,
		users : java.util.Set[jetbrains.buildServer.users.SUser]
	) {}

    def notifyResponsibleChanged(
		testNameResponsibilityEntry : TestNameResponsibilityEntry,
		testNameResponsibilityEntry1 : TestNameResponsibilityEntry,
		sProject : SProject,
		users : java.util.Set[jetbrains.buildServer.users.SUser]
	) { }

    def notifyResponsibleAssigned(
		testNameResponsibilityEntry : TestNameResponsibilityEntry ,
		testNameResponsibilityEntry1 : TestNameResponsibilityEntry ,
		sProject : SProject,
		users : java.util.Set[jetbrains.buildServer.users.SUser]
	) {}
}