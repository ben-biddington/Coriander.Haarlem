package coriander.haarlem.rss

import xml.XML
import java.text.{SimpleDateFormat, ParsePosition}

class FailblogRssAdapter extends RssFeedAdapter {
	def first(text : String) : RssFeedItem = {
		val feedXml = XML.loadString(text)

		val firstItem = (feedXml\"channel"\"item").first

		val title = (firstItem\"title").first.text
		val url = (firstItem\"content").last.attribute("url").get.text
		val date = parseDate((firstItem\"pubDate").first.text)
		
		new RssFeedItem(title, url, date)
	}

	private def parseDate(dateText : String) = {
		println(dateText)
		dateFormat parse(dateText, new ParsePosition(0))
	}

	private lazy val dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ")
}