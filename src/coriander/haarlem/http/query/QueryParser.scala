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

        queryString split(delimiter) foreach(pair => {
            val nameAndValue = parser.parse(pair)

            if (nameAndValue != null) {
                buffer += nameAndValue
            }
        });

        buffer toList
    }

	val parser = new NameValuePairParser

	val DEFAULT_DELIMITER = "&"
}