package coriander.haarlem.unit.tests.core.calendar

import org.scalatest.{Spec, BeforeAndAfterEach}
import org.scalatest.matchers.MustMatchers
import coriander.haarlem.core.StringMatcher
import java.util.regex.PatternSyntaxException

class StringMatcherTests extends Spec
	with MustMatchers
	with BeforeAndAfterEach {

	describe("matches") {
		it("handles whole words") {
			matcher.matches("xxx", "yyy") must be(false)
			matcher.matches("xxx", "xxx") must be(true)
		}

		it("handles regex patterns") {
			matcher.matches(example, example) must be(true)
			matcher.matches(example, "ccc$") must be(true)
			matcher.matches(example, "^aaa") must be(true)
			matcher.matches(example, "^[a-c]{3}") must be(true)
			matcher.matches(example, "^[a-c\\-]+$") must be(true)
			matcher.matches(example, "(aaa|ddd)") must be(true)
			matcher.matches(example, "(ddd|fff)") must be(false)
		}

		it("handles null by returning false") {
			matcher.matches(null, "xxx") must be(false)
		}

		it("handles null pattern by failing") {
			intercept[IllegalArgumentException] {
				matcher.matches("xxx", null) must be(false)
			}
		}

		it("does case insensitive matching") {
			matcher.matches("AAA", "^aaa$") must be(true)
		}

		it("handles malformed patterns by throwing PatternSyntaxException") {
			intercept[PatternSyntaxException] {
				matcher.matches("AAA", "[li")
			}
		}

		it("handles character groups") {
			matcher.matches("systest xxx smoke", "systest.+smoke") must be(true)
			matcher.matches("systest xxx smoke", "systest[a-z\\s]+smoke") must be(true)
		}
	}

	private val example = "aaa-bbb-ccc"
	private val matcher = new StringMatcher
}