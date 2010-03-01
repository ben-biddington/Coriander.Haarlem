package coriander.haarlem.rss

import java.util.Date

class RssFeedItem(val title : String, val media : RssFeedItemMedia, val date : Date) {
	def this(title : String, media : RssFeedItemMedia) {
		this(title, media, null)
	}

	def getTitle = title
	def getMedia = media
}

class RssFeedItemMedia(val url : String, val html : String) {
	def getUrl = url
	def getHtml = html
}

object RssFeedItemMedia {
	def apply(url : String) : RssFeedItemMedia = apply(url, null)
	def apply(url : String, html : String) : RssFeedItemMedia = new RssFeedItemMedia(url, html)
}