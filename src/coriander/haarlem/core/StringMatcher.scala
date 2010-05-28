package coriander.haarlem.core

class StringMatcher {
	def matches(what : String, pattern : String) = pattern.r.findFirstMatchIn(what).isDefined
}