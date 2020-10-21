package cat.hobbiton.hobbit.util

import cat.hobbiton.hobbit.messages.TestsMessages.ERROR_TEST_ONE_PARAMS
import cat.hobbiton.hobbit.messages.TestsMessages.ERROR_TEST_ZERO_PARAMS
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.util.*

class I18nMessageExtensionTest : DescribeSpec() {

    init {

        describe("translate") {

            context("english") {
                val actual = ERROR_TEST_ZERO_PARAMS.english()

                it("translates the message") {
                    actual shouldBe "TEST TEXT FOR ZERO PARAMS"
                }
            }

            context("other locale with translation") {
                val actual = ERROR_TEST_ONE_PARAMS.translate(
                        Locale("ca"),
                        3
                )

                it("translates the message") {
                    actual shouldBe "CAT TEXT FOR ONE PARAMS: 3"
                }
            }
        }

        describe("translateWithParamsArray") {

            context("english") {
                val actual = ERROR_TEST_ZERO_PARAMS.englishWithParamsArray(
                        arrayOf()
                )

                it("translates the message") {
                    actual shouldBe "TEST TEXT FOR ZERO PARAMS"
                }
            }

            context("other locale with translation") {
                val actual = ERROR_TEST_ONE_PARAMS.translateWithParamsArray(
                        Locale("ca"),
                        arrayOf(3)
                )

                it("translates the message") {
                    actual shouldBe "CAT TEXT FOR ONE PARAMS: 3"

                }
            }
        }
    }
}