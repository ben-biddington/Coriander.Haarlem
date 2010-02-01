package coriander.haarlem.http.query

class Query(queryString : String) {
	val namesAndValues = new QueryParser().parse(queryString)	

	def value(name : String) :  String = {
		if (false == contains(name))
			return null
	
		namesAndValues.find(_.getName == name).get.getValue
	}

	def contains(name : String) = {
		namesAndValues.exists(_.getName == name)
	}
}

object Query {
	def apply(queryString : String) = new Query(queryString)
}