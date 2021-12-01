package cat.hobbiton.hobbit.util.i18n

import cat.hobbiton.hobbit.messages.ValidationMessages
import cat.hobbiton.hobbit.messages.ValidationMessages.ERROR_PRODUCT_ID_BLANK
import cat.hobbiton.hobbit.messages.ValidationMessages.ERROR_PRODUCT_ID_LENGTH
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class I18nMessageExtensionTest : DescribeSpec() {

    init {

        describe("translate") {

            context("without params") {
                val actual = ERROR_PRODUCT_ID_BLANK.translate()

                it("translates the message") {
                    actual shouldBe "El id del producte no pot estar buit"
                }
            }

            context("with int param") {
                val actual = ERROR_PRODUCT_ID_LENGTH.translate(3)

                it("translates the message") {
                    actual shouldBe "El id del producte ha de tenir 3 carácters"
                }
            }

            context("with string param") {
                val actual = ValidationMessages.ERROR_PAYMENT_TYPE_CONFIGURATION_UNDEFINED.translate("RECTIFICATION")

                it("translates the message") {
                    actual shouldBe "El valor de configuració paymentTypeNotes.RECTIFICATION no està informat"
                }
            }
        }

        describe("translateWithParamsArray") {

            context("without params") {
                val actual = ERROR_PRODUCT_ID_BLANK.translateWithParamsArray(arrayOf())

                it("translates the message") {
                    actual shouldBe "El id del producte no pot estar buit"
                }
            }

            context("with params") {
                val actual = ERROR_PRODUCT_ID_LENGTH.translateWithParamsArray(arrayOf(3))

                it("translates the message") {
                    actual shouldBe "El id del producte ha de tenir 3 carácters"
                }
            }
        }
    }
}