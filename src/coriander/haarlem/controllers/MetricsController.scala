package coriander.haarlem.controllers

import java.lang.Long._
import jetbrains.buildServer.controllers.BaseController
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import coriander.haarlem.http.query.Query
import jetbrains.buildServer.web.openapi.{PluginDescriptor, WebControllerManager}
import jetbrains.buildServer.users.SUser
import java.util.ArrayList
import collection.mutable.ListBuffer
import jetbrains.buildServer.serverSide._
import coriander.haarlem.core.Convert
import javax.xml.transform.stream.{StreamResult, StreamSource}
import javax.xml.transform.{Transformer, TransformerFactory}
import javax.xml.transform.dom.DOMResult
import java.io.{StringWriter, File}

class MetricsController(
	buildServer : SBuildServer,
	pluginDescriptor : PluginDescriptor
) extends BaseController {
	override protected def doHandle(
		request : HttpServletRequest,
		response : HttpServletResponse
	) : ModelAndView = {

		val query = Query(request.getQueryString)

		val userId = query.value("user")

		var result : MetricsModel = null

		var user : SUser = null

		if (userId != null) {
			user = buildServer.getUserModel.findUserById(parseLong(userId))

			if (user != null) {
				return run(user)
			}
		}

		response.setContentType("text/html")

		if (null == userId) {
			response.setStatus(400)
			response.getWriter.write("Missing the &lt;user&gt; parameter.")
		}  else if (null == user) {
			response.setStatus(404)
			response.getWriter.write(
				"Not found. " +
				"The user with id &lt;" + userId + "&gt; could not be found."
			)			
		}

		null
	}

	private def run(user: SUser) = {
		val allBuildsWithDashboards = findAllBuildsWithDashboards(user)

		var temp = new ListBuffer[DashboardInfo]
		val dashboardRenderer = new Dashboard()
		val xsl = new File(
			buildServer.getServerRootPath +
			pluginDescriptor.getPluginResourcesPath +
			"/metrics/dashboard.xsl"
		)

		allBuildsWithDashboards.foreach(build => {
			temp += new DashboardInfo(build, dashboardRenderer.run(build, xsl))
		})
		
		val result = new MetricsModel(user, Convert.toJavaList(temp.toList))
		
		new ModelAndView(
			pluginDescriptor.getPluginResourcesPath + "/metrics/default.jsp",
			"results",
			result
		)
	}

	def register() {
		val mgr = getApplicationContext.
			getBean("webControllerManager", classOf[WebControllerManager])

		mgr.registerController("/metrics.html", this)
	}

	private def findAllBuildsWithDashboards(user : SUser) : List[SBuildType] = {
		val result = new ListBuffer[SBuildType]()
		
		val projects = Convert.toScalaList(getAllProjects(user))

		projects.foreach(project => {
			val builds = Convert.toScalaList(project.getBuildTypes)

			builds.foreach(build => {
				if (hasDashboard(build)) {
					result += build
				}
			})
		})

		result toList
	}

	private def hasDashboard(buildType : SBuildType) : Boolean = {
		val dashboardFolderName = "dashboard"

		val lastSuccessful = buildType.getLastChangesSuccessfullyFinished

		if (null == lastSuccessful)
			return false

		val rootDir = lastSuccessful.getArtifactsDirectory.getCanonicalFile

		return if (rootDir.list != null)
			rootDir.list.contains(dashboardFolderName)
		else false
	}

	// TODO: Make sure they're projects I am following
	private def getAllProjects(user : SUser) = {
		// TODO: Spring it
		val projectManager = buildServer.getProjectManager

		val result = new ArrayList[SProject]()

		val projectIdIterator = user.getAllProjects.iterator

		var currentId : String = null

		while (projectIdIterator.hasNext) {
			currentId = projectIdIterator.next

			val project = projectManager.findProjectById(currentId)

			if (project != null) {
				result.add(project)
			}
		}

		result
	}
}

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

class DashboardInfo(build : SBuildType, html : String) {
	def getBuild = build
	def getHtml = html
}

class Dashboard {
	def run(build : SBuildType, xsl : File) : String = {
		val dir = build.getLastChangesSuccessfullyFinished.getArtifactsDirectory.getCanonicalPath
		val xml = new File(dir + "\\dashboard\\dashboard.xml")

		if (false == xml.exists)
			throw new Exception("Not found: " + xml.getCanonicalPath)

		return apply(xml, xsl)
	}

	private def apply(xml : File, xslt : File) : String = {
        require(xml.exists, "File not found <" + xml.getCanonicalPath + ">")
        require(xslt.exists, "File not found <" + xslt.getCanonicalPath + ">")

		val xmlSource = new StreamSource(xml);
        val xsltSource = new StreamSource(xslt);

        val transFact = TransformerFactory.newInstance();
        val trans : Transformer = transFact.newTransformer(xsltSource);

		if (null == trans)
			throw new Exception("No Transformer available")

		val domResult : DOMResult = new DOMResult

		var stringWriter : StringWriter = null;
		
		try {
			stringWriter = new StringWriter

        	trans.transform(xmlSource, new StreamResult(stringWriter));

			return stringWriter.toString

		}  finally {
			if (stringWriter != null) {
				stringWriter.close
			}
		}
	}
}