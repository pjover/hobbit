package cat.hobbiton.hobbit.util.i18n

import cat.hobbiton.hobbit.messages.ValidationMessages.ERROR_PRODUCT_ID_BLANK
import cat.hobbiton.hobbit.messages.ValidationMessages.ERROR_PRODUCT_ID_LENGTH
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec

class I18nMessageExtensionTest : DescribeSpec() {

    init {

        describe("translate") {

            context("without params") {
                val actual = ERROR_PRODUCT_ID_BLANK.translate()

                it("translates the message") {
                    actual shouldBe "Product id cannot be blank"
                }
            }

            context("with params") {
                val actual = ERROR_PRODUCT_ID_LENGTH.translate(3)

                it("translates the message") {
                    actual shouldBe "Product id must be 3 characters long"
                }
            }
        }

        describe("translateWithParamsArray") {

            context("without params") {
                val actual = ERROR_PRODUCT_ID_BLANK.translateWithParamsArray(arrayOf())

                it("translates the message") {
                    actual shouldBe "Product id cannot be blank"
                }
            }

            context("with params") {
                val actual = ERROR_PRODUCT_ID_LENGTH.translateWithParamsArray(arrayOf(3))

                it("translates the message") {
                    actual shouldBe "Product id must be 3 characters long"
                }
            }
        }
    }
}