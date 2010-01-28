package coriander.haarlem.rss

class DilbertRssFeed {
	def find = {
		new RssFeed(
			"http://feeds.dilbert.com/DilbertDailyStrip?format=xml",
			new DilbertRssAdapter
		).find
	}
}