package coriander.haarlem.core.calendar

import jetbrains.buildServer.serverSide.ProjectManager

class BuildFinder(val projectManager : ProjectManager) {
	def find() {
		val allBuildTypes = projectManager.getAllBuildTypes;	
	}
}