package coriander.haarlem.unit.tests.core.calendar

import org.scalatest.{Spec, BeforeAndAfterEach}
import org.scalatest.matchers.MustMatchers
import coriander.haarlem.core.StringMatcher

class StringMatcherTests extends Spec
	with MustMatchers
	with BeforeAndAfterEach {

	describe("isMatch") {
		it("matches words") {
			matcher.matches("xxx", "yyy") must be(false)
			matcher.matches("xxx", "xxx") must be(true)
		}

		it("matches patterns") {
			val example = "aaa-bbb-ccc"

			matcher.matches(example, "ccc$") must be(true)
			matcher.matches(example, "^aaa") must be(true)
			matcher.matches(example, "^[a-c]{3}") must be(true)
			matcher.matches(example, "^[a-c\\-]+$") must be(true)
			matcher.matches(example, "(aaa|ddd)") must be(true)
			matcher.matches(example, "(ddd|fff)") must be(false)
		}
	}

	private val matcher = new StringMatcher
}