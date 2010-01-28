package coriander.haarlem.controllers

import coriander.haarlem.http.SimpleInternet
import java.net.URI

class DilbertFinder {
	def find() : String =  {
		val url = "http://feeds.dilbert.com/DilbertDailyStrip?format=xml"

		new DilbertAdapter().getFirst(
			new SimpleInternet().get(new URI(url)).text
		)
	}
}