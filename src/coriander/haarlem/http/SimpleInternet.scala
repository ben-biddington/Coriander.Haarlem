package coriander.haarlem.http


/**
 * Created by IntelliJ IDEA.
 * User: Phil
 * Date: 12-Jan-2010
 * Time: 13:37:36
 * To change this template use File | Settings | File Templates.
 */

import java.net.URI
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.http.HttpStatus
import org.apache.commons.httpclient.{NameValuePair, HttpMethodBase, HttpClient}
import query.QueryParameters

class SimpleInternet extends TheInternet {
	def get(resource : URI, parameters : QueryParameters) =
		execute(newGet(resource, parameters toArray))

	private def newGet(uri : URI, parameters : Array[NameValuePair]) = {
		val method = new GetMethod(uri toString)
		method.setQueryString(parameters)
		method
	}

	private def execute(method : HttpMethodBase) : Response = {
		setLogLevel

		val status = new HttpClient() executeMethod(method)
		var responseUri = new URI(method.getURI.getEscapedURI)

		if (status != HttpStatus.SC_OK)
			return new Response(
				status,
				responseUri,
				method getStatusText
			)

		new Response(
			status,
			responseUri,
			method.getResponseBodyAsString
		)
	}

	private def setLogLevel {
		System.setProperty(
			"org.apache.commons.logging.Log",
			"org.apache.commons.logging.impl.SimpleLog"
		);
		System.setProperty(
			"org.apache.commons.logging.simplelog.showdatetime",
			"false"
		);
		System.setProperty(
			"org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient",
			"debug"
		);
	}
}
