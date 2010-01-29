package coriander.haarlem.rss

import xml.XML
import java.text.{ParsePosition, SimpleDateFormat}

class DilbertRssAdapter extends RssFeedAdapter {
	def first(text : String) : RssFeedItem = {
		val feedXml = XML.loadString(text)

		val firstItem = (feedXml\"channel"\"item").first

		val escaped = (firstItem\"description").first.text

		val pattern = "http://[^\"]+".r

		val title = (firstItem\"title").first.text
		val url = pattern.findFirstIn(escaped).get
		val date = parseDate((feedXml\"channel"\"pubDate").first.text)
		new RssFeedItem("Dilbert -- " + title, url, date)
	}

	private def parseDate(dateText : String) = {
		println(dateText)
		dateFormat parse(dateText, new ParsePosition(0))
	}

	private lazy val dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z")
}