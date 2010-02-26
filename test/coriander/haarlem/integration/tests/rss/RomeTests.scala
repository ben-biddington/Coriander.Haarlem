package coriander.haarlem.integration.tests.rss

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import java.net.URL
import com.sun.syndication.fetcher.impl.{HashMapFeedInfoCache, HttpURLFeedFetcher}
import com.sun.syndication.feed.synd.{SyndContentImpl, SyndEntryImpl, SyndFeed}

class RomeTests {
	val failblog = "http://feeds.feedburner.com/failblog?format=xml"
	
    @Test
	def how_to_load_rss_feed_with_rome { 
       	val feedInfoCache = HashMapFeedInfoCache.getInstance();
    	val feedFetcher = new HttpURLFeedFetcher(feedInfoCache);
    	val feed : SyndFeed = feedFetcher.retrieveFeed(new URL(failblog));

		val allEntries : java.util.List[SyndEntryImpl] = feed.getEntries.asInstanceOf[java.util.List[SyndEntryImpl]]
		
		val firstFeedItem : SyndEntryImpl = allEntries.get(0)

		val firstContent : SyndContentImpl = firstFeedItem.getContents.asInstanceOf[java.util.List[SyndContentImpl]].get(0)

		val x = firstFeedItem.get
		
		println("Title: " 		+ firstFeedItem.getTitle)
		println("Contents: " 	+ firstContent.getValue)
    }
}