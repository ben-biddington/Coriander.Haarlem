package coriander.haarlem

import java.io.Closeable

object using {
	def apply(resources : Closeable*) (block : => Unit) {
		try {
			block
		} finally {
			if (resources != null) {
				println("Closing resources")
				resources.foreach(_.close)
			}
		}
	}
}