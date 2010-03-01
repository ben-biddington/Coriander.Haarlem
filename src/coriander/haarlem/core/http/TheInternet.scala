package coriander.haarlem.http


import java.net.URI
import query.QueryParameters

abstract class TheInternet {
	def get(resource : URI) : Response = get(resource, new QueryParameters())
	def get(resource : URI, parameters : QueryParameters) : Response
}