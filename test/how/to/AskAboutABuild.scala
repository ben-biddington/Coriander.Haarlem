package how.to

import java.net.URL
import jetbrains.buildServer.agentServer.AgentBuild
import jetbrains.buildServer.serverSide.{BuildServerAdapter, ProjectManager}
import org.apache.xmlrpc.TCXmlRpcClient
import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._

class AskAboutABuild {
	@Test
	def example {
		// http://javadoc.jetbrains.net/teamcity/openapi/current/jetbrains/buildServer/serverSide/SBuildServer.html
		//		This is one of the core components of the TeamCity server-side support. It manages many aspects of TeamCity, for instance:
		//		Access to TeamCity installation information, system and configuration directories
		//		Global TeamCity listener, which is notified about most events in TeamCity BuildServerAdapter
		//		Global build history access via appropriate findNNN methods
		//		Access to various TeamCity managers (but it is much better to use dependency injection provided by Spring to achive this, because these getXXXManager methods may disappear in the future)
		//		Extension of TeamCity via various extension points, see ServerExtensionHolder (please also use DI)
		//		TeamCity server version, build number
		//		Executor service for short-time processes

		// Yeah, but how do you create one?
		//
		// val server = new BuildServer("http://teamcity").findBuildByName("CHUBBY_BAT")
    }
}