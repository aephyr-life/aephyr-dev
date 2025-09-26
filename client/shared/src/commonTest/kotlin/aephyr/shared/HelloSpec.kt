package aephyr.shared

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class HelloSpec : StringSpec({

    "simple hello world test" {
        val message = "Hello, Aephyr!"
        message shouldBe "Hello, Aephyr!"
    }

})
