package cat.hobbiton.hobbit.util

import cat.hobbiton.hobbit.messages.ValidationMessages.ERROR_PRODUCT_ID_BLANK
import cat.hobbiton.hobbit.messages.ValidationMessages.ERROR_PRODUCT_ID_LENGTH
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.util.*

class I18nTranslatorTest : DescribeSpec() {

    init {
        describe("english") {

            context("without params") {
                val actual = I18nTranslator.english(ERROR_PRODUCT_ID_BLANK)

                it("translates the message") {
                    actual shouldBe "Product id cannot be blank"
                }
            }

            context("with params") {
                val actual = I18nTranslator.english(ERROR_PRODUCT_ID_LENGTH, 3)

                it("translates the message") {
                    actual shouldBe "Product id must be 3 characters long"
                }
            }
        }

        describe("englishWithParamsArray") {

            context("without params") {
                val actual = I18nTranslator.englishWithParamsArray(ERROR_PRODUCT_ID_BLANK, arrayOf()
                )

                it("translates the message") {
                    actual shouldBe "Product id cannot be blank"
                }
            }

            context("with params") {
                val actual = I18nTranslator.englishWithParamsArray(ERROR_PRODUCT_ID_LENGTH, arrayOf(3))

                it("translates the message") {
                    actual shouldBe "Product id must be 3 characters long"
                }
            }

        }

        describe("translate") {

            context("other locale with translation") {
                val actual = I18nTranslator.translate(Locale("ca"), ERROR_PRODUCT_ID_BLANK)

                it("translates the message") {
                    actual shouldBe "El id del producte no pot estar buit"
                }
            }

            context("other locale without translation") {
                val actual = I18nTranslator.translate(Locale("ru"), ERROR_PRODUCT_ID_BLANK)

                it("translates the message") {
                    actual shouldBe "Product id cannot be blank"
                }
            }

        }
    }

}