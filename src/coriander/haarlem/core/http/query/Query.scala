package coriander.haarlem.http.query

class Query(queryString : String) {
	private val namesAndValues = new QueryParser().parse(queryString)	

	def value(name : String) 				= if (contains(name)) get(name) else null
	def containsWithValue(name : String) 	= contains(name) && value(name) != null
	def contains(name : String) 			= namesAndValues.exists(_.getName == name)

	private def get(what : String) =
		namesAndValues.find(_.getName == what).get.getValue
}

object Query {
	def apply(queryString : String) = new Query(queryString)
}