package coriander.haarlem.rss

import xml.XML

class DilbertRssAdapter extends RssFeedAdapter {
	def first(text : String) : RssFeedItem = {
		val feedXml = XML.loadString(text)

		val firstItem = (feedXml\"channel"\"item").first

		val escaped = (firstItem\"description").first.text

		val pattern = "http://[^\"]+".r

		val title = (firstItem\"title").first.text
		val url = pattern.findFirstIn(escaped).get

		new RssFeedItem("Dilbert -- " + title, url)
	}
}