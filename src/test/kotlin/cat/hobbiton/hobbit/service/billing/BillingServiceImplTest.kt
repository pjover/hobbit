package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.api.model.ChildConsumtionDTO
import cat.hobbiton.hobbit.api.model.ConsumtionDTO
import cat.hobbiton.hobbit.api.model.YearMonthConsumptionsDTO
import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.model.Consumption
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import java.math.BigDecimal

class BillingServiceImplTest : DescribeSpec() {

    init {
        val consumptionRepository = mockk<ConsumptionRepository>()
        val customerRepository = mockk<CustomerRepository>()
        val sut = BillingServiceImpl(consumptionRepository, customerRepository)

        describe("getConsumptions") {
            context("") {
                every { consumptionRepository.findByInvoicedOnNullAndChildCode(1) } returns listOf(
                        Consumption(
                                id = "AA1",
                                childCode = 1,
                                productId = "TST",
                                units = BigDecimal.valueOf(2),
                                yearMonth = YEAR_MONTH,
                                note = "Note 1",
                                invoicedOn = null
                        ),
                        Consumption(
                                id = "AA2",
                                childCode = 1,
                                productId = "TST",
                                units = BigDecimal.valueOf(2),
                                yearMonth = YEAR_MONTH,
                                note = "Note 2",
                                invoicedOn = null
                        ),
                        Consumption(
                                id = "AA3",
                                childCode = 1,
                                productId = "TST",
                                units = BigDecimal.valueOf(2),
                                yearMonth = YEAR_MONTH,
                                note = "Note 3",
                                invoicedOn = null
                        ),
                        Consumption(
                                id = "AA4",
                                childCode = 1,
                                productId = "STS",
                                units = BigDecimal.valueOf(2),
                                yearMonth = YEAR_MONTH,
                                note = "Note 4",
                                invoicedOn = null
                        ),
                        Consumption(
                                id = "AA5",
                                childCode = 1,
                                productId = "TST",
                                units = BigDecimal.valueOf(2),
                                yearMonth = YEAR_MONTH,
                                note = "Note 5",
                                invoicedOn = null
                        )
                )
                every { customerRepository.findByChildCode(1) } returns testCustomer(children = listOf(testChild1()))

                val actual = sut.getConsumptions(1)

                it("return the consumpion of this child") {
                    actual shouldBe listOf(
                            YearMonthConsumptionsDTO(
                                    YEAR_MONTH.toString(),
                                    listOf(
                                            ChildConsumtionDTO(
                                                    code = 1,
                                                    shortName = "Laura Llull",
                                                    listOf(
                                                            ConsumtionDTO("TST", 8.0, "Note 1, Note 2, Note 3, Note 5"),
                                                            ConsumtionDTO("STS", 2.0, "Note 4")
                                                    )
                                            )
                                    )
                            )
                    )
                }
            }

            context("for all children") {
                every { consumptionRepository.findByInvoicedOnNull() } returns listOf(
                        Consumption(
                                id = "AA1",
                                childCode = 1,
                                productId = "TST",
                                units = BigDecimal.valueOf(2),
                                yearMonth = YEAR_MONTH,
                                note = "Note 1",
                                invoicedOn = null
                        ),
                        Consumption(
                                id = "AA2",
                                childCode = 1,
                                productId = "TST",
                                units = BigDecimal.valueOf(2),
                                yearMonth = YEAR_MONTH,
                                note = "Note 2",
                                invoicedOn = null
                        ),
                        Consumption(
                                id = "AA3",
                                childCode = 2,
                                productId = "TST",
                                units = BigDecimal.valueOf(2),
                                yearMonth = YEAR_MONTH,
                                note = "Note 3",
                                invoicedOn = null
                        ),
                        Consumption(
                                id = "AA4",
                                childCode = 2,
                                productId = "STS",
                                units = BigDecimal.valueOf(2),
                                yearMonth = YEAR_MONTH,
                                note = "Note 4",
                                invoicedOn = null
                        ),
                        Consumption(
                                id = "AA5",
                                childCode = 3,
                                productId = "TST",
                                units = BigDecimal.valueOf(2),
                                yearMonth = YEAR_MONTH,
                                note = "Note 5",
                                invoicedOn = null
                        )
                )
                every { customerRepository.findByChildCode(1) } returns testCustomer(children = listOf(testChild1()))
                every { customerRepository.findByChildCode(2) } returns testCustomer(children = listOf(testChild2()))
                every { customerRepository.findByChildCode(3) } returns testCustomer(children = listOf(testChild3()))


                val actual = sut.getConsumptions(null)

                it("return the consumpion of all children") {
                    actual shouldBe listOf(
                            YearMonthConsumptionsDTO(
                                    YEAR_MONTH.toString(),
                                    listOf(
                                            ChildConsumtionDTO(
                                                    code = 1,
                                                    shortName = "Laura Llull",
                                                    listOf(
                                                            ConsumtionDTO("TST", 4.0, "Note 1, Note 2")
                                                    )
                                            ),
                                            ChildConsumtionDTO(
                                                    code = 2,
                                                    shortName = "Aina Llull",
                                                    listOf(
                                                            ConsumtionDTO("TST", 2.0, "Note 3"),
                                                            ConsumtionDTO("STS", 2.0, "Note 4")
                                                    )
                                            ),
                                            ChildConsumtionDTO(
                                                    code = 3,
                                                    shortName = "Laia Llull",
                                                    listOf(
                                                            ConsumtionDTO("TST", 2.0, "Note 5")
                                                    )
                                            )
                                    )
                            )
                    )

                }
            }
        }
    }

}