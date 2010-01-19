package coriander.haarlem.plugins

import jetbrains.buildServer.serverSide.{SRunningBuild, BuildServerAdapter}
import jetbrains.buildServer.messages.{Status, BuildMessage1}
import java.util.Date

class SimplePlugin extends BuildServerAdapter {
	override def beforeBuildFinish(runningBuild : SRunningBuild) {
		runningBuild.addBuildMessage(
			new BuildMessage1(
				"",
				"",
				Status.NORMAL,
				new Date,
				"Any message"
			)
		)
	}
}