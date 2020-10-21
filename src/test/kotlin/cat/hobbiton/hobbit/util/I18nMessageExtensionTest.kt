package cat.hobbiton.hobbit.util

import cat.hobbiton.hobbit.messages.ValidationMessages.ERROR_PRODUCT_ID_BLANK
import cat.hobbiton.hobbit.messages.ValidationMessages.ERROR_PRODUCT_ID_LENGTH
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.util.*

class I18nMessageExtensionTest : DescribeSpec() {

    init {

        describe("translate") {

            context("english") {
                val actual = ERROR_PRODUCT_ID_BLANK.english()

                it("translates the message") {
                    actual shouldBe "Product id cannot be blank"
                }
            }

            context("other locale with translation") {
                val actual = ERROR_PRODUCT_ID_LENGTH.translate(
                        Locale("ca"),
                        3
                )

                it("translates the message") {
                    actual shouldBe "El id del producte ha de tenir 3 carácters"
                }
            }
        }

        describe("translateWithParamsArray") {

            context("english") {
                val actual = ERROR_PRODUCT_ID_BLANK.englishWithParamsArray(
                        arrayOf()
                )

                it("translates the message") {
                    actual shouldBe "Product id cannot be blank"
                }
            }

            context("other locale with translation") {
                val actual = ERROR_PRODUCT_ID_LENGTH.translateWithParamsArray(
                        Locale("ca"),
                        arrayOf(3)
                )

                it("translates the message") {
                    actual shouldBe "El id del producte ha de tenir 3 carácters"

                }
            }
        }
    }
}