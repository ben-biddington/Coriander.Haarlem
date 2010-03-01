package coriander.haarlem.rss

import com.sun.syndication.fetcher.impl.{HttpURLFeedFetcher, HashMapFeedInfoCache}
import java.net.{URI}
import com.sun.syndication.feed.synd.{SyndContentImpl, SyndEntryImpl, SyndFeed}
import coriander.haarlem.http.SimpleInternet

class RssFeed(url : String, adapter : RssFeedAdapter) {
	def find() : RssFeedItem =  {
		val response = new SimpleInternet().get(new URI(url)).text
		
		adapter.first(response)
	}

	// TODO: Incomplete
	private def getFirst(uri : URI) = {
		val feedInfoCache = HashMapFeedInfoCache.getInstance
    	val feedFetcher = new HttpURLFeedFetcher(feedInfoCache)
    	val feed : SyndFeed = feedFetcher.retrieveFeed(uri toURL)

		val allEntries : java.util.List[SyndEntryImpl] = feed.getEntries.asInstanceOf[java.util.List[SyndEntryImpl]]

		val firstFeedItem : SyndEntryImpl = allEntries.get(0)

		firstFeedItem.getContents.asInstanceOf[java.util.List[SyndContentImpl]].get(0).toString
	}
}