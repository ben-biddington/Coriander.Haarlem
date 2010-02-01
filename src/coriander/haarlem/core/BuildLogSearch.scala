package coriander.haarlem.core

import jetbrains.buildServer.serverSide.buildLog.{LogMessage, BuildLog}
import java.util.regex.Pattern
import util.matching.Regex
import collection.mutable.ListBuffer

class BuildLogSearch(buildLog : BuildLog)  {
	def searchForRegex(pattern : String) : List[LogMessage] = {
		searchFor(pattern.r)
	}
	
	def searchForPattern(pattern : String) : List[LogMessage] = {
		searchFor(Pattern.quote(pattern).r)
	}

	private def searchFor(forWhat : Regex) : List[LogMessage] = {
		val iterator = getIterator

		var current : LogMessage = null

		var result = new ListBuffer[LogMessage]

		while (iterator.hasNext) {
			current = iterator.next

			if (false == forWhat.findFirstIn(current.getText).isEmpty) {
				result.append(current)
			}
		}

		result toList
	}

	private def getIterator = buildLog.getMessagesIterator.
		asInstanceOf[java.util.Iterator[LogMessage]]
}