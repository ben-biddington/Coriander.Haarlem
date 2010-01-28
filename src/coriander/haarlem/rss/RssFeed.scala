package coriander.haarlem.rss

import coriander.haarlem.http.SimpleInternet
import java.net.URI

class RssFeed(url : String, adapter : RssFeedAdapter) {
	def find() : String =  {
		val response = new SimpleInternet().get(new URI(url)).text
		
		adapter.getFirst(response)
	}
}