package coriander.haarlem

import java.io.{InputStreamReader, BufferedReader}

class Grep {
	def run(command : String) : String = {
		var grep = Runtime.getRuntime().exec(command)

		val input = new BufferedReader(
			new InputStreamReader(grep.getInputStream())
		)

		this readAll input
	}

	private def readAll(input : BufferedReader) : String = {
		val buffer = new StringBuffer()

		using (input) {
			var line : String = ""

			var done = false

			while (false == done) {
				line = input.readLine()
				done = line == null

				if (line != null) {
					buffer.append(line + WINDOWS_LINE_ENDING)
				}
		  	}
		}

		buffer.toString
	}

	private val WINDOWS_LINE_ENDING = "\r\n"
}