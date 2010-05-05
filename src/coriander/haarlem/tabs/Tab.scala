package coriander.haarlem.tabs

import java.lang.Long._
import javax.servlet.http.HttpServletRequest
import coriander.haarlem.http.query.Query
import jetbrains.buildServer.serverSide.{SBuild, SBuildServer}
import org.springframework.context.{ApplicationContext, ApplicationContextAware}

class Tab(buildServer : SBuildServer) extends ApplicationContextAware {
	protected def theCurrentBuild(request : HttpServletRequest) : SBuild = {
		var theBuild = request.getAttribute("BUILD_KEY").asInstanceOf[SBuild]

		if (null == theBuild) {
			val query = new Query(request.getQueryString)

			if (query.contains(QUERY_BUILD_ID)) {
				theBuild = buildServer.findBuildInstanceById(
					parseLong(query.value(QUERY_BUILD_ID))
				)
			}
		}

		theBuild
	}

	def setApplicationContext(applicationContext : ApplicationContext) {
		this.applicationContext = applicationContext
	}

	protected var applicationContext : ApplicationContext = null
	private val QUERY_BUILD_ID 	= "buildId"
}