package cat.hobbiton.hobbit.util.i18n

import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.messages.ValidationMessages.ERROR_PRODUCT_ID_BLANK
import cat.hobbiton.hobbit.messages.ValidationMessages.ERROR_PRODUCT_ID_LENGTH
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class I18nTranslatorTest : DescribeSpec() {

    init {
        describe("translate") {

            context("without params") {
                val actual = I18nTranslator.translate(ERROR_PRODUCT_ID_BLANK)

                it("translates the message") {
                    actual shouldBe "El id del producte no pot estar buit"
                }
            }

            context("with int param") {
                val actual = I18nTranslator.translate(ERROR_PRODUCT_ID_LENGTH, 3)

                it("translates the message") {
                    actual shouldBe "El id del producte ha de tenir 3 carácters"
                }
            }

            context("with string param") {
                val actual = I18nTranslator.translate(ValidationMessages.ERROR_CHILD_TAX_ID_INVALID, "12345H")

                it("translates the message") {
                    actual shouldBe "El NIF/NIE 12345H de l´infant no és vàlid"
                }
            }
        }

        describe("translateWithParamsArray") {

            context("without params") {
                val actual = I18nTranslator.translateWithParamsArray(ERROR_PRODUCT_ID_BLANK, arrayOf())

                it("translates the message") {
                    actual shouldBe "El id del producte no pot estar buit"
                }
            }

            context("with params") {
                val actual = I18nTranslator.translateWithParamsArray(ERROR_PRODUCT_ID_LENGTH, arrayOf(3))

                it("translates the message") {
                    actual shouldBe "El id del producte ha de tenir 3 carácters"
                }
            }
        }
    }
}