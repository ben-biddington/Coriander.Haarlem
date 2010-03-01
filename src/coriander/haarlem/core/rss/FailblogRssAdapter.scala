package coriander.haarlem.rss

import java.text.{SimpleDateFormat, ParsePosition}
import xml.{PCData, Elem, Node, XML}

class FailblogRssAdapter extends RssFeedAdapter {
	def first(text : String) : RssFeedItem = {
		val feedXml = XML.loadString(text)

		val firstItem = (feedXml\"channel"\"item").first

		val title 	= (firstItem\"title").first.text
		val date 	= parseDate((firstItem\"pubDate").first.text)
		val url 	= findContentUrl(firstItem)
		val media 	= findMediaHtml(firstItem)

		new RssFeedItem(title, RssFeedItemMedia(url, media), date)
	}

	private def findContentUrl(item : Node) : String = {
		return if ((item\"content") isEmpty)
			null
		else
			(item\"content").last.attribute("url").get.text
	}

	private def findMediaHtml(item : Node) : String = {
		val cdata = (item\"encoded").last.child

		return if ((item\"encoded") isEmpty)
			null
		else
			cleanCData((item\"encoded").last.text.trim)
	}

	private def cleanCData(cdata : String) : String = {
		// [!] Is CDATA malformed? Seems to have leading "<"
		if (cdata.startsWith(">")) {
			return cdata.substring(1)
		}

		return cdata
	}

	private def parseDate(dateText : String) = {
		println(dateText)
		dateFormat parse(dateText, new ParsePosition(0))
	}

	private lazy val dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss ZZZ")
}