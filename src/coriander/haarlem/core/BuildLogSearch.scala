package coriander.haarlem.core

import collection.mutable.ListBuffer
import jetbrains.buildServer.serverSide.buildLog.{LogMessage, BuildLog}

class BuildLogSearch(buildLog : BuildLog)  {
	def searchFor(pattern : String) : List[LogMessage] = {
		var result = new ListBuffer[LogMessage]

		val regex = pattern.r

		val iterator = getIterator

		var current : LogMessage = null
		
		while (iterator.hasNext) {
			current = iterator.next

			if (false == regex.findFirstIn(current.getText).isEmpty) {
				result.append(current)
			}
		}

		result toList
	}

	private def getIterator = buildLog.getMessagesIterator.
		asInstanceOf[java.util.Iterator[LogMessage]]
}