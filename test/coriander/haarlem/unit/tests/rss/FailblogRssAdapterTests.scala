package coriander.haarlem.unit.tests.rss

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import coriander.haarlem.rss.FailblogRssAdapter
import java.util.Calendar
import coriander.haarlem.unit.tests.UnitTest

class FailblogRssAdapterTests extends UnitTest {
    @Test
	def get_first_works { 
		val expectedUrl = "http://failblog.files.wordpress.com/2010/01/epic-fail-lego-instruction-fail.jpg"
		val expectedTitle = "Lego Instruction Fail"
		val expectedDate = toDateAndTimeGmt(2010, Calendar.JANUARY, 28, 15, 0, 43)

		val actual = new FailblogRssAdapter().first(xml.toString)

		assertThat(actual.media.url, is(equalTo(expectedUrl)))
		assertThat(actual.title, is(equalTo(expectedTitle)))
		assertThat(actual.date, is(equalTo(expectedDate)))
	}

	@Test
	def given_the_current_fail_is_a_video_then_it_is_returned {
		val expectedHtml = "<div class='snap_preview'><br />"
		val expectedTitle = "Friday Rewind: Child Molester Fail"
		val expectedDate = toDateAndTimeGmt(2010, Calendar.JANUARY, 29, 22, 0, 38)

		val actual = new FailblogRssAdapter().first(xmlWhereFailIsVideo.toString)

		assertThat(actual.title, is(equalTo(expectedTitle)))
		assertThat(actual.date, is(equalTo(expectedDate)))
		assertTrue("Media URL should be null", null == actual.media.url)
		assertTrue(
			"Expected that trimmed media HTML should start with <" + expectedHtml + ">, " +
			"but it is <" + actual.media.html + ">",
			actual.media.html.trim().startsWith(expectedHtml))
	}

	val xmlWhereFailIsVideo =
	<rss xmlns:content="http://purl.org/rss/1.0/modules/content/" xmlns:wfw="http://wellformedweb.org/CommentAPI/" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:atom="http://www.w3.org/2005/Atom" xmlns:sy="http://purl.org/rss/1.0/modules/syndication/" xmlns:slash="http://purl.org/rss/1.0/modules/slash/" xmlns:georss="http://www.georss.org/georss" xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#" xmlns:media="http://search.yahoo.com/mrss/" xmlns:feedburner="http://rssnamespace.org/feedburner/ext/1.0" version="2.0">
		<channel>
			<item>
				<title>Friday Rewind: Child Molester Fail</title>
				<link>http://feedproxy.google.com/~r/failblog/~3/aC3f-D93V_c/</link>
				<comments>http://failblog.org/2010/01/29/friday-rewind-child-molester-fail-2/#comments</comments>
				<pubDate>Fri, 29 Jan 2010 22:00:38 +0000</pubDate>
				<dc:creator>Cheezburger Network</dc:creator>
				<category><![CDATA[1099689]]></category>
				<category><![CDATA[voting-page]]></category>
				<category><![CDATA[child]]></category>
				<category><![CDATA[molester]]></category>
				<category><![CDATA[news]]></category>
				<category><![CDATA[video]]></category>
				<category><![CDATA[wrong]]></category>
				<guid isPermaLink="false">http://failblog.org/?p=38614</guid>
				<description><![CDATA[epic fail- Friday Rewind: Child Molester Fail, video, news, child, molester, wrong>
					<img alt="" border="0" src="http://stats.wordpress.com/b.gif?host=failblog.org&blog=2441444&post=38614&subd=failblog&ref=&feed=1" />
					<br clear="both" style="clear: both;"/>
					<br clear="both" style="clear: both;"/>
					<a href="http://ads.pheedo.com/click.phdo?s=9a90744c290b19969f00a6b7d0c7f660&p=1"><img alt="" style="border: 0;" border="0" src="http://ads.pheedo.com/img.phdo?s=9a90744c290b19969f00a6b7d0c7f660&p=1"/></a>
					<img alt="" height="0" width="0" border="0" style="display:none" src="http://a.rfihub.com/eus.gif?eui=2222"/>]]>
				</description>
				<content:encoded>
					<![CDATA[>
					<div class='snap_preview'><br />
						<p class='mine_asset assetid_2069761'><object classid='clsid:D27CDB6E-AE6D-11cf-96B8-444553540000' width='500' height='412' id='viddler'><param name='movie' value='http://www.viddler.com/player/e9aeef97' /><param name='allowScriptAccess' value='always' /><embed src='http://www.viddler.com/player/e9aeef97' width='500' height='412' type='application/x-shockwave-flash' allowScriptAccess='always' name='viddler' allowFullScreen='true'></embed></object></p></p>
						<p>This video is also available at <a href="http://www.youtube.com/watch?v=Z1UF6eDZutk" rel="nofollow" target="_blank"><strong>Youtube</strong></a> | <a href="http://www.dailymotion.com/video/xc0zo1_child-molester-fail_fun" rel="nofollow" target="_blank"><strong>DailyMotion</strong></a> | <strong><a href="http://vids.myspace.com/index.cfm?fuseaction=vids.individual&amp;videoid=102566078" rel="nofollow" target="_blank">MySpaceTV</a></strong></p>
						<a rel="nofollow" href="http://feeds.wordpress.com/1.0/gocomments/failblog.wordpress.com/38614/">
							<img alt="" border="0" src="http://feeds.wordpress.com/1.0/comments/failblog.wordpress.com/38614/" />
						</a>
						<a rel="nofollow" href="http://feeds.wordpress.com/1.0/godelicious/failblog.wordpress.com/38614/">
							<img alt="" border="0" src="http://feeds.wordpress.com/1.0/delicious/failblog.wordpress.com/38614/" />
						</a>
						<a rel="nofollow" href="http://feeds.wordpress.com/1.0/gostumble/failblog.wordpress.com/38614/">
							<img alt="" border="0" src="http://feeds.wordpress.com/1.0/stumble/failblog.wordpress.com/38614/" />
						</a>
						<a rel="nofollow" href="http://feeds.wordpress.com/1.0/godigg/failblog.wordpress.com/38614/">
							<img alt="" border="0" src="http://feeds.wordpress.com/1.0/digg/failblog.wordpress.com/38614/" />
						</a>
						<a rel="nofollow" href="http://feeds.wordpress.com/1.0/goreddit/failblog.wordpress.com/38614/">
							<img alt="" border="0" src="http://feeds.wordpress.com/1.0/reddit/failblog.wordpress.com/38614/" />
						</a>
						<img alt="" border="0" src="http://stats.wordpress.com/b.gif?host=failblog.org&blog=2441444&post=38614&subd=failblog&ref=&feed=1" />
					</div>
					<br clear="both" style="clear: both;"/>
					<br clear="both" style="clear: both;"/>
					<a href="http://ads.pheedo.com/click.phdo?s=9a90744c290b19969f00a6b7d0c7f660&p=1">
						<img alt="" style="border: 0;" border="0" src="http://ads.pheedo.com/img.phdo?s=9a90744c290b19969f00a6b7d0c7f660&p=1"/>
					</a>
					<img alt="" height="0" width="0" border="0" style="display:none" src="http://a.rfihub.com/eus.gif?eui=2222"/>
					<div class="feedflare">
						<a href="http://feeds.feedburner.com/~ff/failblog?a=aC3f-D93V_c:Hyf8C-zM6X4:yIl2AUoC8zA">
							<img src="http://feeds.feedburner.com/~ff/failblog?d=yIl2AUoC8zA" border="0"></img>
						</a>
						<a href="http://feeds.feedburner.com/~ff/failblog?a=aC3f-D93V_c:Hyf8C-zM6X4:F7zBnMyn0Lo">
							<img src="http://feeds.feedburner.com/~ff/failblog?i=aC3f-D93V_c:Hyf8C-zM6X4:F7zBnMyn0Lo" border="0"></img>
						</a>
						<a href="http://feeds.feedburner.com/~ff/failblog?a=aC3f-D93V_c:Hyf8C-zM6X4:V_sGLiPBpWU">
							<img src="http://feeds.feedburner.com/~ff/failblog?i=aC3f-D93V_c:Hyf8C-zM6X4:V_sGLiPBpWU" border="0"></img>
						</a>
						<a href="http://feeds.feedburner.com/~ff/failblog?a=aC3f-D93V_c:Hyf8C-zM6X4:qj6IDK7rITs">
							<img src="http://feeds.feedburner.com/~ff/failblog?d=qj6IDK7rITs" border="0"></img>
						</a>
						<a href="http://feeds.feedburner.com/~ff/failblog?a=aC3f-D93V_c:Hyf8C-zM6X4:gIN9vFwOqvQ">
							<img src="http://feeds.feedburner.com/~ff/failblog?i=aC3f-D93V_c:Hyf8C-zM6X4:gIN9vFwOqvQ" border="0"></img>
						</a>
					</div>
					<img src="http://feeds.feedburner.com/~r/failblog/~4/aC3f-D93V_c" height="1" width="1"/>
					]]>
				</content:encoded>
				<wfw:commentRss>http://failblog.org/2010/01/29/friday-rewind-child-molester-fail-2/feed/</wfw:commentRss>
				<slash:comments>15</slash:comments>
				<feedburner:origLink>http://failblog.org/2010/01/29/friday-rewind-child-molester-fail-2/</feedburner:origLink>
				</item>
	     </channel>
	</rss>

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