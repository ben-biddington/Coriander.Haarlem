package coriander.haarlem.core

import collection.mutable.ListBuffer
import java.util.ArrayList

object Convert {
	def toScalaList[T](javaList : java.util.List[T]) : List[T] = {
		toScalaList(javaList.iterator)
	}

	def toScalaList[T](iterator : java.util.Iterator[T]) : List[T] = {
		var result = new ListBuffer[T]()

		while (iterator.hasNext) {
			result += iterator.next
		}

		result toList
	}

	def toJavaList[T](from : Seq[T]) : java.util.List[T] = {
		var result = new ArrayList[T]()

		from.forall(thing => result.add(thing))

		result
	}
}