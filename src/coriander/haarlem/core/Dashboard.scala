package coriander.haarlem.core

import jetbrains.buildServer.serverSide.SBuildType
import javax.xml.transform.{TransformerFactory}
import java.io.{StringWriter, File}
import javax.xml.transform.stream.{StreamResult, StreamSource}


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

		var stringWriter : StringWriter = null;

		try {
			stringWriter = new StringWriter

			val trans = newTransformer(xsltSource)
			
        	trans.transform(xmlSource, new StreamResult(stringWriter));

			return stringWriter.toString

		}  finally {
			if (stringWriter != null) {
				stringWriter.close
			}
		}
	}

	private def newTransformer(xsltSource : StreamSource) = {
		TransformerFactory.newInstance().newTransformer(xsltSource);
	}
}