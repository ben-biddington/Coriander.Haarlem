package coriander.haarlem.rss

import xml.XML

class FailblogRssAdapter {
	def getFirst(text : String) : String = {
		val feedXml = XML.loadString(text)

		val firstItem = (feedXml\"channel"\"item").first

		(firstItem \ "content").last.attribute("url").get.text


		//(firstItem\"media:content").first.attribute("url").get.text
	}
}