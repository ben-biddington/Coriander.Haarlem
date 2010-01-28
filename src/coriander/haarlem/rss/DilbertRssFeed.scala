package coriander.haarlem.rss

class DilbertRssFeed {
	def find = {
		new RssFeed(
			"http://feeds.feedburner.com/failblog?format=xml",
			new DilbertRssAdapter
		).find
	}
}