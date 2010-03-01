package how.to

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import jetbrains.buildServer.web.openapi.{PagePlace, PlaceId}

class CreatePlaceIds {
    @Test
	def name_must_start_with_hash() {
		val pageFooter = new PlaceId("#PAGE_FOOTER");
	}

	@Test { val expected =  classOf[IllegalArgumentException]}
	def omitting_hash_throws_exception() {
		val pageFooter = new PlaceId("PAGE_FOOTER");
	}
}