package coriander.haarlem.http.query

class Query(queryString : String) {
	val namesAndValues = new QueryParser().parse(queryString)

	def contains(name : String) = {
		namesAndValues.exists(_.getName == name)
	}

	def value(name : String) = {
		namesAndValues.find(_.getName == "q").get.getName
	}
}

object Query {
	def apply(queryString : String) = new Query(queryString)
}