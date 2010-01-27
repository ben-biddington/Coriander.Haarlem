package coriander.haarlem.integration.tests

import org.junit.Test
import org.junit.Assert._
import org.hamcrest.core.Is._
import org.hamcrest.core.IsNot._
import org.hamcrest.core.IsEqual._
import coriander.haarlem.using
import java.io.{InputStreamReader, BufferedReader}

class GrepTests {
    @Test
	def grep_can_be_run_as_a_standalone_executable { 
    	val command = "bin/grep.exe -rL GrepTests.scala ."

		var grep : Process = Runtime.getRuntime().exec(command)

		val input = new BufferedReader(
			new InputStreamReader(grep.getInputStream())
		)

		var actualResultCount = 0;

		using (input) {
			var line : String = ""

			var done = false

			while (false == done) {
				line = input.readLine()
				done = line == null

				if (false == done) {
					actualResultCount +=1
				}
		  	}
		}

		assertTrue(actualResultCount > 1)
    }

	// TEST: I can pipe the results from grep into the stdin of another process, like tail
}