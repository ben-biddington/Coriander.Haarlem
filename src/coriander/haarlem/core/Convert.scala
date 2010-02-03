package coriander.haarlem.core

import collection.mutable.ListBuffer
import java.util.ArrayList

object Convert {
	def toScalaList[T](javaList : java.util.List[T]) : List[T] = {
		var result = new ListBuffer[T]()

		val iterator = javaList.iterator

		while (iterator.hasNext) {
			result += iterator.next
		}

		result toList
	}

	def toJavaList[T](from : List[T]) : java.util.List[T] = {
		var result = new ArrayList[T]()

		from.forall(thing => result.add(thing))

		result
	}
}