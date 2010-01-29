package coriander.haarlem.rss

class FailblogRssFeed {
	def find = {
		new RssFeed(
			"http://feeds.feedburner.com/failblog?format=xml",
			new DilbertRssAdapter
		).find
	}
}