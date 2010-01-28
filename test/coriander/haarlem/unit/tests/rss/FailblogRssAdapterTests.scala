package coriander.haarlem.unit.tests.rss

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import coriander.haarlem.rss.FailblogRssAdapter

class FailblogRssAdapterTests {
    @Test
	def get_first_works { 
		val expected = "http://failblog.files.wordpress.com/2010/01/epic-fail-lego-instruction-fail.jpg"
		val actual = new FailblogRssAdapter().getFirst(xml.toString)

		assertThat(actual, is(equalTo(expected)))
    }

	val xml =
	<rss xmlns:content="http://purl.org/rss/1.0/modules/content/" xmlns:wfw="http://wellformedweb.org/CommentAPI/" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:sy="http://purl.org/rss/1.0/modules/syndication/" xmlns:slash="http://purl.org/rss/1.0/modules/slash/" xmlns:georss="http://www.georss.org/georss" xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#" xmlns:media="http://search.yahoo.com/mrss/" xmlns:feedburner="http://rssnamespace.org/feedburner/ext/1.0" version="2.0">
		<channel>
			<title>FAIL Blog: Epic Fail Pictures and Videos of Owned, Pwnd and Fail Moments</title>
			<link>http://failblog.org</link>
			<description>Fail and Epic Fail Pictures and Videos in one blog. For all those precious fail moments, whip out your camera and see your epic fail picture and videos here.</description>
			<lastBuildDate>Thu, 28 Jan 2010 15:00:43 +0000</lastBuildDate>
			<generator>http://wordpress.com/</generator>
			<language>en</language>
			<sy:updatePeriod>hourly</sy:updatePeriod>
			<sy:updateFrequency>1</sy:updateFrequency>
			<cloud domain="failblog.org" port="80" path="/?rsscloud=notify" registerProcedure="" protocol="http-post" />
			<image>
				<url>http://www.gravatar.com/blavatar/7dc1e9564c5ef58f72ff71ae0b828a63?s=96&amp;d=http://s.wordpress.com/i/buttonw-com.png</url>
				<title>FAIL Blog: Epic Fail Pictures and Videos of Owned, Pwnd and Fail Moments</title>
				<link>http://failblog.org</link>
			</image>
			<atom:link rel="search" type="application/opensearchdescription+xml" href="http://failblog.org/osd.xml" title="FAIL Blog: Epic Fail Pictures and Videos of Owned, Pwnd and Fail Moments" />
			<atom10:link xmlns:atom10="http://www.w3.org/2005/Atom" rel="self" type="application/rss+xml" href="http://feeds.feedburner.com/failblog" /><feedburner:info uri="failblog" /><atom10:link xmlns:atom10="http://www.w3.org/2005/Atom" rel="hub" href="http://pubsubhubbub.appspot.com" />
			<item>
				<title>Lego Instruction Fail</title>
				<link>http://feedproxy.google.com/~r/failblog/~3/a_zay0pvQfU/</link>
				<comments>http://failblog.org/2010/01/28/lego-instruction-fail/#comments</comments>
				<pubDate>Thu, 28 Jan 2010 15:00:43 +0000</pubDate>
				<dc:creator>Cheezburger Network</dc:creator>
				<category><![CDATA[1099689]]></category>
				<category><![CDATA[voting-page]]></category>
				<category><![CDATA[G-rated]]></category>
				<category><![CDATA[impossible]]></category>
				<category><![CDATA[instructions]]></category>
				<category><![CDATA[lego]]></category>
				<category><![CDATA[toys]]></category>
				<guid isPermaLink="false">http://failblog.org/?p=37721</guid>
				<description></description>
				<content:encoded/>
				<wfw:commentRss>http://failblog.org/2010/01/28/lego-instruction-fail/feed/</wfw:commentRss>
				<slash:comments>0</slash:comments> 
				<media:content url="http://failblog.files.wordpress.com/2010/01/epic-fail-lego-instruction-fail.jpg" medium="image">
					<media:title type="html">epic-fail-lego-instruction-fail</media:title>
				</media:content>
				<feedburner:origLink>http://failblog.org/2010/01/28/lego-instruction-fail/</feedburner:origLink>
			</item>
		</channel>
	</rss>
}