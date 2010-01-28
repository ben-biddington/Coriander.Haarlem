package coriander.haarlem.http.query

import org.apache.commons.httpclient.NameValuePair

class NameValuePairParser {
	def parse(parameter : String) : NameValuePair = {
        val parts = parameter split("=");

        val name = parts(0) trim

        if (name.length == 0)
            return null

        new NameValuePair(
            urlDecode(name),
            if (parts.length == 1) null else urlDecode(parts(1).trim)
        )
    }

    private def urlDecode(str : String) = java.net.URLDecoder.decode(str, "UTF-8")
}