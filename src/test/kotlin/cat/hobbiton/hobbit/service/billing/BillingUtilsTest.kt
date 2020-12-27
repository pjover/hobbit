package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.DATE
import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.api.model.InvoiceDTO
import cat.hobbiton.hobbit.api.model.InvoiceLineDTO
import cat.hobbiton.hobbit.model.Consumption
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.InvoiceLine
import cat.hobbiton.hobbit.model.PaymentType
import cat.hobbiton.hobbit.testCustomer185
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec

class BillingUtilsTest : DescribeSpec() {

    init {
        describe("groupConsumptions") {

            val actual = groupConsumptions(
                childCode = 1,
                consumptions = listOf(
                    Consumption(
                        id = "AA1",
                        childCode = 1,
                        productId = "TST",
                        units = 2.toBigDecimal(),
                        yearMonth = YEAR_MONTH,
                        note = "Note 1"
                    ),
                    Consumption(
                        id = "AA2",
                        childCode = 1,
                        productId = "TST",
                        units = 2.toBigDecimal(),
                        yearMonth = YEAR_MONTH,
                        note = "Note 2"
                    ),
                    Consumption(
                        id = "AA3",
                        childCode = 1,
                        productId = "TST",
                        units = 2.toBigDecimal(),
                        yearMonth = YEAR_MONTH,
                        note = "Note 3"
                    ),
                    Consumption(
                        id = "AA4",
                        childCode = 1,
                        productId = "XXX",
                        units = 2.toBigDecimal(),
                        yearMonth = YEAR_MONTH,
                        note = "Note 4"
                    ),
                    Consumption(
                        id = "AA5",
                        childCode = 1,
                        productId = "TST",
                        units = 2.toBigDecimal(),
                        yearMonth = YEAR_MONTH,
                        note = "Note 5"
                    )
                )
            )

            it("groups the same consumption and sums quantities") {
                actual.first shouldBe 1

                actual.second[0].childCode shouldBe 1
                actual.second[0].productId shouldBe "TST"
                actual.second[0].units shouldBe 8.toBigDecimal()
                actual.second[0].yearMonth shouldBe YEAR_MONTH
                actual.second[0].note shouldBe "Note 1, Note 2, Note 3, Note 5"

                actual.second[1].childCode shouldBe 1
                actual.second[1].productId shouldBe "XXX"
                actual.second[1].units shouldBe 2.toBigDecimal()
                actual.second[1].yearMonth shouldBe YEAR_MONTH
                actual.second[1].note shouldBe "Note 4"
            }
        }

        describe("getInvoiceDto") {

            val customer = testCustomer185

            val invoice = Invoice(
                id = "??",
                customerId = 185,
                date = DATE,
                yearMonth = YEAR_MONTH,
                childrenCodes = listOf(1850, 1851),
                paymentType = PaymentType.BANK_DIRECT_DEBIT,
                lines = listOf(
                    InvoiceLine(
                        productId = "TST",
                        units = 4.toBigDecimal(),
                        productPrice = 10.9.toBigDecimal(),
                        childCode = 1850
                    ),
                    InvoiceLine(
                        productId = "TST",
                        units = 2.toBigDecimal(),
                        productPrice = 10.9.toBigDecimal(),
                        childCode = 1851
                    ),
                    InvoiceLine(
                        productId = "XXX",
                        units = 2.toBigDecimal(),
                        productPrice = 9.1.toBigDecimal(),
                        childCode = 1851
                    )
                ),
                note = "Note 1, Note 2, Note 3, Note 4"
            )

            val actual = getInvoiceDto(customer, invoice)

            it("returns the InvoiceDTO") {
                actual shouldBe InvoiceDTO(
                    code = "??",
                    yearMonth = YEAR_MONTH.toString(),
                    children = listOf("Laura", "Aina"),
                    totalAmount = 83.6.toBigDecimal(),
                    lines = listOf(
                        InvoiceLineDTO(
                            productId = "TST",
                            units = 4.toBigDecimal(),
                            totalAmount = 43.6.toBigDecimal(),
                            childCode = 1850
                        ),
                        InvoiceLineDTO(
                            productId = "TST",
                            units = 2.toBigDecimal(),
                            totalAmount = 21.8.toBigDecimal(),
                            childCode = 1851
                        ),
                        InvoiceLineDTO(
                            productId = "XXX",
                            units = 2.toBigDecimal(),
                            totalAmount = 18.2.toBigDecimal(),
                            childCode = 1851
                        )
                    ),
                    note = "Note 1, Note 2, Note 3, Note 4"
                )
            }

        }
    }

}