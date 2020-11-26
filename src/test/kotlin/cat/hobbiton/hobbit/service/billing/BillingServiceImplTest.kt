package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.api.model.*
import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.db.repository.ProductRepository
import cat.hobbiton.hobbit.model.Consumption
import cat.hobbiton.hobbit.model.Product
import cat.hobbiton.hobbit.service.aux.TimeService
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.math.BigDecimal
import java.util.*

class BillingServiceImplTest : DescribeSpec() {

    init {
        val consumptionRepository = mockk<ConsumptionRepository>()
        val customerRepository = mockk<CustomerRepository>()
        val productRepository = mockk<ProductRepository>()
        val timeService = mockk<TimeService>()
        val sut = BillingServiceImpl(consumptionRepository, customerRepository, productRepository, timeService)

        every { customerRepository.findByChildCode(1) } returns testCustomer(children = listOf(testChild1()))
        every { productRepository.findById("TST") } returns Optional.of(
                Product(
                        id = "TST",
                        name = "Test product",
                        shortName = "Test",
                        price = BigDecimal.valueOf(10.9)
                ))
        every { productRepository.findById("STS") } returns Optional.of(
                Product(
                        id = "STS",
                        name = "Test product",
                        shortName = "Test",
                        price = BigDecimal.valueOf(9.1)
                ))

        describe("getChildConsumptions") {
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

            val actual = sut.getChildConsumptions(1)

            it("return the consumpion of this child") {
                actual shouldBe listOf(
                        YearMonthConsumptionsDTO(
                                yearMonth = YEAR_MONTH.toString(),
                                grossAmount = 105.4,
                                listOf(
                                        ChildConsumtionDTO(
                                                code = 1,
                                                shortName = "Laura Llull",
                                                grossAmount = 105.4,
                                                listOf(
                                                        ConsumtionDTO("TST", 8.0, 87.2, "Note 1, Note 2, Note 3, Note 5"),
                                                        ConsumtionDTO("STS", 2.0, 18.2, "Note 4")
                                                )
                                        )
                                )
                        )
                )
            }
        }

        describe("getConsumptions") {
            every { consumptionRepository.findByInvoicedOnNull() } returns allChildren()
            mockFindByChildCode(customerRepository)

            val actual = sut.getConsumptions()

            it("return the consumpion of all children") {
                actual shouldBe allChildrenConsumptions()
            }
        }

        describe("getLastMonthConsumptions") {
            every { consumptionRepository.findByInvoicedOnNull() } returns allChildren() + listOf(
                    Consumption(
                            id = "AA1",
                            childCode = 1,
                            productId = "TST",
                            units = BigDecimal.valueOf(2),
                            yearMonth = YEAR_MONTH.minusMonths(1),
                            note = "Note 1",
                            invoicedOn = null
                    ))
            mockFindByChildCode(customerRepository)
            every { timeService.currentYearMonth } returns YEAR_MONTH.plusMonths(1)

            val actual = sut.getLastMonthConsumptions()

            it("return the consumpion of all children") {
                actual shouldBe listOf(
                        SetYearMonthConsumptionsDTO(
                                yearMonth = YEAR_MONTH.toString(),
                                listOf(
                                        SetChildConsumtionDTO(
                                                code = 1,
                                                listOf(
                                                        SetConsumtionDTO("TST", 4.0, "Note 1, Note 2")
                                                )
                                        ),
                                        SetChildConsumtionDTO(
                                                code = 2,
                                                listOf(
                                                        SetConsumtionDTO("TST", 2.0, "Note 3"),
                                                        SetConsumtionDTO("STS", 2.0, "Note 4")
                                                )
                                        ),
                                        SetChildConsumtionDTO(
                                                code = 3,
                                                listOf(
                                                        SetConsumtionDTO("TST", 2.0, "Note 5")
                                                )
                                        )
                                )
                        )
                )
            }
        }

        describe("setConsumptions") {
            every { consumptionRepository.findByInvoicedOnNull() } returns allChildren()
            mockFindByChildCode(customerRepository)
            every { consumptionRepository.save(any()) } answers { firstArg() }

            val actual = sut.setConsumptions(
                    SetYearMonthConsumptionsDTO(
                            yearMonth = YEAR_MONTH.toString(),
                            listOf(
                                    SetChildConsumtionDTO(
                                            code = 1,
                                            listOf(
                                                    SetConsumtionDTO("TST", 2.0, "Note 1"),
                                                    SetConsumtionDTO("TST", 2.0, "Note 2")
                                            )
                                    ),
                                    SetChildConsumtionDTO(
                                            code = 2,
                                            listOf(
                                                    SetConsumtionDTO("TST", 2.0, "Note 3"),
                                                    SetConsumtionDTO("STS", 2.0, "Note 4")
                                            )
                                    ),
                                    SetChildConsumtionDTO(
                                            code = 3,
                                            listOf(
                                                    SetConsumtionDTO("TST", 2.0, "Note 5")
                                            )
                                    )
                            )
                    )
            )

            it("return the consumpion of all children") {
                actual shouldBe allChildrenConsumptions()
            }

            verify(exactly = 5) {
                consumptionRepository.save(any())
            }
        }
    }

    private fun allChildrenConsumptions() = listOf(
            YearMonthConsumptionsDTO(
                    yearMonth = YEAR_MONTH.toString(),
                    grossAmount = 105.4,
                    listOf(
                            ChildConsumtionDTO(
                                    code = 1,
                                    shortName = "Laura Llull",
                                    grossAmount = 43.6,
                                    listOf(
                                            ConsumtionDTO("TST", 4.0, 43.6, "Note 1, Note 2")
                                    )
                            ),
                            ChildConsumtionDTO(
                                    code = 2,
                                    shortName = "Aina Llull",
                                    grossAmount = 40.0,
                                    listOf(
                                            ConsumtionDTO("TST", 2.0, 21.8, "Note 3"),
                                            ConsumtionDTO("STS", 2.0, 18.2, "Note 4")
                                    )
                            ),
                            ChildConsumtionDTO(
                                    code = 3,
                                    shortName = "Laia Llull",
                                    grossAmount = 21.8,
                                    listOf(
                                            ConsumtionDTO("TST", 2.0, 21.8, "Note 5")
                                    )
                            )
                    )
            )
    )

    private fun allChildren() = listOf(
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

    private fun mockFindByChildCode(customerRepository: CustomerRepository) {
        every { customerRepository.findByChildCode(1) } returns testCustomer(children = listOf(testChild1()))
        every { customerRepository.findByChildCode(2) } returns testCustomer(children = listOf(testChild2()))
        every { customerRepository.findByChildCode(3) } returns testCustomer(children = listOf(testChild3()))
    }
}