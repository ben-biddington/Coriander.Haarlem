package coriander.haarlem.integration.tests

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import java.io.{InputStreamReader, BufferedReader}
import coriander.haarlem.{Grep, using}
import jetbrains.buildServer.web.openapi.PlaceId

class GrepTests {
    @Test
	def grep_can_be_run_as_a_standalone_executable { 
    	val command = "bin/grep.exe -Hr CHUBBY test/res/*.txt"

		val result = new Grep().run(command).trim
		val expected = "test/res/example-to-grep-for.txt:CHUBBY_RAIN"
		
		println(result)

		assertThat(
			"Expected single result to be returned",
			result, is(equalTo(expected))
		)
    }

	// TEST: I can pipe the results from grep into the stdin of another process, like tail
}