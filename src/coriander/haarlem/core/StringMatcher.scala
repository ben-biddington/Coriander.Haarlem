package coriander.haarlem.core

class StringMatcher {
	def matches(what : String, pattern : String) = {
		require(pattern != null, "You need the pattern")

		// See: http://www.javaranch.com/journal/2003/04/RegexTutorial.htm
		val caseInsensitivePattern = "(?i)" + pattern

		if (what != null) caseInsensitivePattern.r.findFirstMatchIn(what).isDefined else false
	}
}