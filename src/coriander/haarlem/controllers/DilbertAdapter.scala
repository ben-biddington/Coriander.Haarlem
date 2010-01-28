package coriander.haarlem.controllers

import xml.XML

class DilbertAdapter {
	def getFirst(text : String) : String = {
		val feedXml = XML.loadString(text)

		val firstItem = (feedXml\"channel"\"item").first

		val escaped = (firstItem\"description").first.text

		val pattern = "http://[^\"]+".r

		pattern.findFirstIn(escaped).get
	}
}