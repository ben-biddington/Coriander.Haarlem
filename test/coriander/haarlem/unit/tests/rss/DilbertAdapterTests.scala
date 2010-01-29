package coriander.haarlem.unit.tests.rss

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import coriander.haarlem.rss.DilbertRssAdapter
import coriander.haarlem.unit.tests.UnitTest
import java.util.Calendar

class DilbertAdapterTests extends UnitTest {
    @Test
	def get_first_works {
		val expectedUrl = "http://dilbert.com/dyn/str_strip/000000000/00000000/0000000/000000/80000/0000/200/80274/80274.strip.print.gif"
		val expectedTitle = "Dilbert -- Comic for January 28, 2010"
		val expectedDateGmt = toDateAndTimeGmt(2010, Calendar.JANUARY, 28, 8, 0, 0)
		
		val actual = new DilbertRssAdapter().first(xml.toString)

		assertThat(actual.url, is(equalTo(expectedUrl)))
		assertThat(actual.title, is(equalTo(expectedTitle)))
		assertThat(actual.date, is(equalTo(expectedDateGmt)))
    }

	val xml =
	<rss xmlns:atom="http://www.w3.org/2005/Atom" xmlns:feedburner="http://rssnamespace.org/feedburner/ext/1.0" version="2.0">
		<channel>
			<title><![CDATA[Dilbert Daily Strip]]></title>
			<link><![CDATA[http://dilbert.com/]]></link>
			<description><![CDATA[The Official Dilbert Daily Comic Strip RSS Feed]]></description>
			<language><![CDATA[en-us]]></language>
			<generator><![CDATA[VPI.Net]]></generator>
			<managingEditor><![CDATA[service@dilbert.com (VPI.Net)]]></managingEditor>
			<webMaster><![CDATA[service@dilbert.com (VPI.Net)]]></webMaster>
			<ttl><![CDATA[5]]></ttl>
			<pubDate><![CDATA[Thu, 28 Jan 2010 00:00:00 PST]]></pubDate>
			<lastBuildDate><![CDATA[Thu, 28 Jan 2010 00:00:00 PST]]></lastBuildDate>
 			<atom10:link xmlns:atom10="http://www.w3.org/2005/Atom" rel="self" type="application/rss+xml" href="http://feeds.dilbert.com/DilbertDailyStrip" />
			<feedburner:info uri="dilbertdailystrip" />
			<atom10:link xmlns:atom10="http://www.w3.org/2005/Atom" rel="hub" href="http://pubsubhubbub.appspot.com" />
			<item>
				<title><![CDATA[Comic for January 28, 2010]]></title>
				<link>http://feeds.dilbert.com/~r/DilbertDailyStrip/~3/HbVF8tamEEg/</link>
				<description>&lt;img src="http://dilbert.com/dyn/str_strip/000000000/00000000/0000000/000000/80000/0000/200/80274/80274.strip.print.gif" border="0" /&gt;
				&lt;p&gt;&lt;a href="http://feedads.g.doubleclick.net/~at/gHyg-zYo0WD--afUZAmWz4gvxBM/0/da"&gt;&lt;img src="http://feedads.g.doubleclick.net/~at/gHyg-zYo0WD--afUZAmWz4gvxBM/0/di" border="0" ismap="true"&gt;&lt;/img&gt;&lt;/a&gt;&lt;br/&gt;
				&lt;a href="http://feedads.g.doubleclick.net/~at/gHyg-zYo0WD--afUZAmWz4gvxBM/1/da"&gt;&lt;img src="http://feedads.g.doubleclick.net/~at/gHyg-zYo0WD--afUZAmWz4gvxBM/1/di" border="0" ismap="true"&gt;&lt;/img&gt;&lt;/a&gt;&lt;/p&gt;&lt;img src="http://feeds.feedburner.com/~r/DilbertDailyStrip/~4/HbVF8tamEEg" height="1" width="1"/&gt;</description>
				<pubDate><![CDATA[Thu, 28 Jan 2010 00:00:00 PST]]></pubDate>
				<guid isPermaLink="false"><![CDATA[http://dilbert.com/strips/comic/2010-01-28/]]></guid>
				<feedburner:origLink>http://dilbert.com/strips/comic/2010-01-28/</feedburner:origLink>
			</item> 
		</channel>
	</rss>
}