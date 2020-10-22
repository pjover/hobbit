package cat.hobbiton.hobbit.util

import cat.hobbiton.hobbit.messages.ValidationMessages.ERROR_PRODUCT_ID_BLANK
import cat.hobbiton.hobbit.messages.ValidationMessages.ERROR_PRODUCT_ID_LENGTH
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec

class I18nTranslatorTest : DescribeSpec() {

    init {
        describe("translate") {

            context("without params") {
                val actual = I18nTranslator.translate(ERROR_PRODUCT_ID_BLANK)

                it("translates the message") {
                    actual shouldBe "Product id cannot be blank"
                }
            }

            context("with params") {
                val actual = I18nTranslator.translate(ERROR_PRODUCT_ID_LENGTH, 3)

                it("translates the message") {
                    actual shouldBe "Product id must be 3 characters long"
                }
            }
        }

        describe("translateWithParamsArray") {

            context("without params") {
                val actual = I18nTranslator.translateWithParamsArray(ERROR_PRODUCT_ID_BLANK, arrayOf())

                it("translates the message") {
                    actual shouldBe "Product id cannot be blank"
                }
            }

            context("with params") {
                val actual = I18nTranslator.translateWithParamsArray(ERROR_PRODUCT_ID_LENGTH, arrayOf(3))

                it("translates the message") {
                    actual shouldBe "Product id must be 3 characters long"
                }
            }
        }
    }
}