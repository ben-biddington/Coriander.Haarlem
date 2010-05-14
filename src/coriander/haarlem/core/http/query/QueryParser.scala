package coriander.haarlem.http.query

import org.apache.commons.httpclient.NameValuePair
import collection.mutable.ListBuffer

class QueryParser {
	def parse(queryString : String) : List[NameValuePair] =
		parse(queryString, DEFAULT_DELIMITER)
	
	def parse(queryString : String, delimiter : String) : List[NameValuePair] = {
        var buffer = new ListBuffer[NameValuePair]()

        if (null == queryString || queryString.trim == "")
            return List()

		queryString.split(delimiter).
			map(parser.parse(_)).
			filter(_ != null).
			toList
    }

	private val parser = new NameValuePairParser

	private val DEFAULT_DELIMITER = "&"
}