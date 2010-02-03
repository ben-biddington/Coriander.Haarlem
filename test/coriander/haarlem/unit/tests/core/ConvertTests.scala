package coriander.haarlem.unit.tests.core

import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import coriander.haarlem.core.Convert
import java.util.ArrayList
import org.junit.{Before, Test}

class ConvertTests {
	@Before
	def before {
		anExampleJavaList.clear
		anExampleJavaList.add(1)
		anExampleJavaList.add(2)
		anExampleJavaList.add(3)
	}

    @Test
	def to_java_list_converts_all_items {
		val original : List[Int] = List(1, 2, 3)
		val converted = Convert.toJavaList(original)

		assertThat("Converting to java lists should produce the same number of items",
			converted.size, is(equalTo(original.size))
		)
    }

	@Test
	def to_java_list_produces_items_in_correct_order {
		val original : List[Int] = anExampleScalaList
		val converted = Convert.toJavaList(original)

		assertThat("Converting to java lists should produce all items in correct order",
			converted, is(equalTo(anExampleJavaList))
		)
    }

	@Test
	def to_scala_list_converts_all_items {
		val converted = Convert.toScalaList(anExampleJavaList)

		assertThat("Converting to java lists should produce the same number of items",
			converted.size, is(equalTo(anExampleJavaList.size))
		)
    }

	@Test
	def to_scala_list_produces_items_in_correct_order {
		val converted : List[Int] = Convert.toScalaList(anExampleJavaList)

		assertThat("Converting to java lists should produce all items in correct order",
			converted, is(equalTo(List(1, 2, 3)))
		)
    }

	val anExampleJavaList : java.util.List[Int] = new java.util.ArrayList[Int]()
	val anExampleScalaList = List(1, 2, 3)
}