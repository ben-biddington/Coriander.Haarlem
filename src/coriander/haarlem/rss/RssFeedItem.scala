package coriander.haarlem.rss

import java.util.Date

class RssFeedItem(val title : String, val url : String, date : Date) {
	def this(title : String, url : String) {
		this(title, url, null)
	}
}