package cat.hobbiton.hobbit.init

import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import kotlin.test.assertFailsWith

class BusinessPropertiesTest : DescribeSpec() {

    init {
        val sut = BusinessProperties()

        describe("init()") {
            sut.paymentTypeNotes = mutableMapOf(
                "BANK_DIRECT_DEBIT" to "BANK_DIRECT_DEBIT note",
                "BANK_TRANSFER" to "BANK_TRANSFER note",
                "VOUCHER" to "VOUCHER note",
                "CASH" to "CASH note"
            )

            context("Child with blank name") {
                val executor = {
                    sut.init()
                }

                it("throws an error") {
                    val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    exception.message shouldBe "Missing configuration value for paymentTypeNotes.RECTIFICATION"
                }

            }
        }
    }
}