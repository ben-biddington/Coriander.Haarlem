package coriander.haarlem.rss

import xml.XML

class DilbertRssAdapter extends RssFeedAdapter {
	def getFirst(text : String) : String = {
		val feedXml = XML.loadString(text)

		val firstItem = (feedXml\"channel"\"item").first

		val escaped = (firstItem\"description").first.text

		val pattern = "http://[^\"]+".r

		pattern.findFirstIn(escaped).get
	}
}