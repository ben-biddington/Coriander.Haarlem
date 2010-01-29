package coriander.haarlem.rss

import xml.XML

class FailblogRssAdapter extends RssFeedAdapter {
	def first(text : String) : RssFeedItem = {
		val feedXml = XML.loadString(text)

		val firstItem = (feedXml\"channel"\"item").first

		val title = (firstItem\"title").first.text
		val url = (firstItem\"content").last.attribute("url").get.text

		new RssFeedItem(title, url)
	}
}