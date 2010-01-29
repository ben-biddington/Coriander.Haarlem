package coriander.haarlem.rss

import coriander.haarlem.http.SimpleInternet
import java.net.URI

class RssFeed(url : String, adapter : RssFeedAdapter) {
	def find() : RssFeedItem =  {
		val response = new SimpleInternet().get(new URI(url)).text
		
		adapter.first(response)
	}
}