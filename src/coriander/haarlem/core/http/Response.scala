package coriander.haarlem.http


import java.net.URI

class Response(val status : Int, val uri : URI, val text : String)

object Response {
	def okay(text : String) = new Response(200, null, text)
}