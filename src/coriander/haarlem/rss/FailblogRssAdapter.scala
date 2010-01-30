package coriander.haarlem.rss

import java.text.{SimpleDateFormat, ParsePosition}
import xml.{Node, XML}

class FailblogRssAdapter extends RssFeedAdapter {
	def first(text : String) : RssFeedItem = {
		val feedXml = XML.loadString(text)

		val firstItem = (feedXml\"channel"\"item").first

		val title 	= (firstItem\"title").first.text
		val url 	= findContentUrl(firstItem) 
		val media 	= (firstItem\"encoded").last.text
		val date 	= parseDate((firstItem\"pubDate").first.text)
		
		new RssFeedItem(title, RssFeedItemMedia(url, media), date)
	}

	private def findContentUrl(item : Node) : String = {
		return if ((item\"content") isEmpty)
			null
		else
			(item\"content").last.attribute("url").get.text
	}

	private def findMediaHtml(item : Node) : String = {
		return if ((item\"encoded") isEmpty)
			null
		else
			(item\"encoded").last.text
	}

	private def parseDate(dateText : String) = {
		println(dateText)
		dateFormat parse(dateText, new ParsePosition(0))
	}

	private lazy val dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ")
}