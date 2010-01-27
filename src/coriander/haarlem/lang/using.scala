package coriander.haarlem

import java.io.Closeable

object using {
	def apply(resources : Closeable*) (block : => Any) {
		try {
			block
		} finally {
			if (resources != null) {
				resources.foreach(_.close)
			}
		}
	}
}