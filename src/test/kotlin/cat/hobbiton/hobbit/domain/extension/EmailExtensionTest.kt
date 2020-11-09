package cat.hobbiton.hobbit.domain.extension

import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec


class EmailExtensionTest : DescribeSpec() {

    init {

        describe("isEmailValid") {

            context("valid email") {

                it("should return true") {
                    "jbibiloni@gmail.com".isValidEmail() shouldBe true
                }
            }

            context("invalid email") {

                it("should return false") {
                    "".isValidEmail() shouldBe false
                    "jbibiloni".isValidEmail() shouldBe false
                    "jbibiloni.gmail.com".isValidEmail() shouldBe false
                    "@jbibiloni.gmail.com".isValidEmail() shouldBe false
                    "jbibiloni@gmail".isValidEmail() shouldBe false
                    "jbibiloni@gmail com".isValidEmail() shouldBe false
                    "jbibiloni#gmail.com".isValidEmail() shouldBe false
                }

            }
        }
    }
}
