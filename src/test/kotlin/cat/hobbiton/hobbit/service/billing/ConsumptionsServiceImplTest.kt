package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.api.model.*
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.CachedProductRepository
import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.model.Consumption
import cat.hobbiton.hobbit.model.Product
import cat.hobbiton.hobbit.service.aux.TimeService
import cat.hobbiton.hobbit.testChild1
import cat.hobbiton.hobbit.testChild2
import cat.hobbiton.hobbit.testChild3
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.math.BigDecimal

class ConsumptionsServiceImplTest : DescribeSpec() {

    init {
        val consumptionRepository = mockk<ConsumptionRepository>()
        val customerRepository = mockk<CachedCustomerRepository>()
        val productRepository = mockk<CachedProductRepository>()
        val timeService = mockk<TimeService>()
        val sut = ConsumptionsServiceImpl(consumptionRepository, customerRepository, productRepository, timeService)

        every { timeService.lastMonth } returns YEAR_MONTH

        describe("getChildConsumptions") {

            context("with no consumptions") {
                every { consumptionRepository.findByInvoiceIdNullAndChildCode(1) } returns emptyList()

                val actual = sut.getChildConsumptions(1)

                it("throws an error") {
                    actual shouldBe emptyList()
                }
            }

            context("with consumptions") {

                mockAuxReaders(customerRepository, productRepository)
                every { consumptionRepository.findByInvoiceIdNullAndChildCode(1) } returns listOf(
                    Consumption(
                        id = "AA1",
                        childCode = 1850,
                        productId = "TST",
                        units = BigDecimal.valueOf(2),
                        yearMonth = YEAR_MONTH,
                        note = "Note 1"
                    ),
                    Consumption(
                        id = "AA2",
                        childCode = 1850,
                        productId = "TST",
                        units = BigDecimal.valueOf(2),
                        yearMonth = YEAR_MONTH,
                        note = "Note 2"
                    ),
                    Consumption(
                        id = "AA3",
                        childCode = 1850,
                        productId = "TST",
                        units = BigDecimal.valueOf(2),
                        yearMonth = YEAR_MONTH,
                        note = "Note 3"
                    ),
                    Consumption(
                        id = "AA4",
                        childCode = 1850,
                        productId = "XXX",
                        units = BigDecimal.valueOf(2),
                        yearMonth = YEAR_MONTH,
                        note = "Note 4"
                    ),
                    Consumption(
                        id = "AA5",
                        childCode = 1850,
                        productId = "TST",
                        units = BigDecimal.valueOf(2),
                        yearMonth = YEAR_MONTH,
                        note = "Note 5"
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
                                    code = 1850,
                                    shortName = "Laura Llull",
                                    grossAmount = 105.4,
                                    listOf(
                                        ConsumtionDTO("TST", 8.0, 87.2, "Note 1, Note 2, Note 3, Note 5"),
                                        ConsumtionDTO("XXX", 2.0, 18.2, "Note 4")
                                    )
                                )
                            )
                        )
                    )
                }
            }
        }

        describe("getConsumptions") {
            mockAuxReaders(customerRepository, productRepository)
            mockConsumptionsReader(consumptionRepository)

            val actual = sut.getConsumptions()

            it("return the consumpion of all children") {
                actual shouldBe allChildrenConsumptions()
            }
        }

        describe("getLastMonthConsumptions") {


            context("with no consumptions") {
                clearMocks(customerRepository)
                every { consumptionRepository.findByInvoiceIdNull() } returns emptyList()

                val actual = sut.getLastMonthConsumptions()

                it("throws an error") {
                    actual shouldBe SetYearMonthConsumptionsDTO("2019-05", emptyList())
                }
            }

            context("with consumptions") {

                mockAuxReaders(customerRepository, productRepository)
                mockConsumptionsReader(consumptionRepository, listOf(
                    Consumption(
                        id = "AA1",
                        childCode = 1850,
                        productId = "TST",
                        units = BigDecimal.valueOf(2),
                        yearMonth = YEAR_MONTH.minusMonths(1),
                        note = "Note 1"
                    ))
                )

                val actual = sut.getLastMonthConsumptions()

                it("return the consumpion of all children") {
                    actual shouldBe SetYearMonthConsumptionsDTO(
                        yearMonth = YEAR_MONTH.toString(),
                        listOf(
                            SetChildConsumtionDTO(
                                code = 1850,
                                listOf(
                                    SetConsumtionDTO("TST", 4.0, "Note 1, Note 2")
                                )
                            ),
                            SetChildConsumtionDTO(
                                code = 1851,
                                listOf(
                                    SetConsumtionDTO("TST", 2.0, "Note 3"),
                                    SetConsumtionDTO("XXX", 2.0, "Note 4")
                                )
                            ),
                            SetChildConsumtionDTO(
                                code = 1852,
                                listOf(
                                    SetConsumtionDTO("TST", 2.0, "Note 5")
                                )
                            )
                        )
                    )
                }
            }
        }

        describe("setConsumptions") {
            mockAuxReaders(customerRepository, productRepository)
            mockConsumptionsReader(consumptionRepository)
            val capturedConsumptions = mutableListOf<Consumption>()
            every { consumptionRepository.save(capture(capturedConsumptions)) } answers { firstArg() }

            val actual = sut.setConsumptions(
                SetYearMonthConsumptionsDTO(
                    yearMonth = YEAR_MONTH.toString(),
                    listOf(
                        SetChildConsumtionDTO(
                            code = 1850,
                            listOf(
                                SetConsumtionDTO("TST", 2.0, "Note 1"),
                                SetConsumtionDTO("TST", 2.0, "Note 2")
                            )
                        ),
                        SetChildConsumtionDTO(
                            code = 1851,
                            listOf(
                                SetConsumtionDTO("TST", 2.0, "Note 3"),
                                SetConsumtionDTO("XXX", 2.0, "Note 4")
                            )
                        ),
                        SetChildConsumtionDTO(
                            code = 1852,
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

            it("saves the consumptions") {
                verify(exactly = 5) {
                    consumptionRepository.save(any())
                }

                capturedConsumptions[0].productId shouldBe "TST"
                capturedConsumptions[0].childCode shouldBe 1850
                capturedConsumptions[0].units shouldBe BigDecimal.valueOf(2.0)
                capturedConsumptions[0].yearMonth shouldBe YEAR_MONTH
                capturedConsumptions[0].note shouldBe "Note 1"
                capturedConsumptions[0].isRectification shouldBe false

                capturedConsumptions[1].productId shouldBe "TST"
                capturedConsumptions[1].childCode shouldBe 1850
                capturedConsumptions[1].units shouldBe BigDecimal.valueOf(2.0)
                capturedConsumptions[1].yearMonth shouldBe YEAR_MONTH
                capturedConsumptions[1].note shouldBe "Note 2"
                capturedConsumptions[1].isRectification shouldBe false

                capturedConsumptions[2].productId shouldBe "TST"
                capturedConsumptions[2].childCode shouldBe 1851
                capturedConsumptions[2].units shouldBe BigDecimal.valueOf(2.0)
                capturedConsumptions[2].yearMonth shouldBe YEAR_MONTH
                capturedConsumptions[2].note shouldBe "Note 3"
                capturedConsumptions[2].isRectification shouldBe false

                capturedConsumptions[3].productId shouldBe "XXX"
                capturedConsumptions[3].childCode shouldBe 1851
                capturedConsumptions[3].units shouldBe BigDecimal.valueOf(2.0)
                capturedConsumptions[3].yearMonth shouldBe YEAR_MONTH
                capturedConsumptions[3].note shouldBe "Note 4"
                capturedConsumptions[3].isRectification shouldBe false

                capturedConsumptions[4].productId shouldBe "TST"
                capturedConsumptions[4].childCode shouldBe 1852
                capturedConsumptions[4].units shouldBe BigDecimal.valueOf(2.0)
                capturedConsumptions[4].yearMonth shouldBe YEAR_MONTH
                capturedConsumptions[4].note shouldBe "Note 5"
                capturedConsumptions[4].isRectification shouldBe false
            }
        }

        describe("setRectificationConsumptions") {
            mockAuxReaders(customerRepository, productRepository)
            mockConsumptionsReader(consumptionRepository)
            val capturedConsumptions = mutableListOf<Consumption>()
            every { consumptionRepository.save(capture(capturedConsumptions)) } answers { firstArg() }

            val actual = sut.setRectificationConsumptions(
                SetYearMonthConsumptionsDTO(
                    yearMonth = YEAR_MONTH.toString(),
                    listOf(
                        SetChildConsumtionDTO(
                            code = 1850,
                            listOf(
                                SetConsumtionDTO("TST", 2.0, "Note 1"),
                                SetConsumtionDTO("TST", 2.0, "Note 2")
                            )
                        ),
                        SetChildConsumtionDTO(
                            code = 1851,
                            listOf(
                                SetConsumtionDTO("TST", 2.0, "Note 3"),
                                SetConsumtionDTO("XXX", 2.0, "Note 4")
                            )
                        ),
                        SetChildConsumtionDTO(
                            code = 1852,
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

            it("saves the consumptions") {
                verify(exactly = 5) {
                    consumptionRepository.save(any())
                }

                capturedConsumptions[0].productId shouldBe "TST"
                capturedConsumptions[0].childCode shouldBe 1850
                capturedConsumptions[0].units shouldBe BigDecimal.valueOf(2.0)
                capturedConsumptions[0].yearMonth shouldBe YEAR_MONTH
                capturedConsumptions[0].note shouldBe "Note 1"
                capturedConsumptions[0].isRectification shouldBe true

                capturedConsumptions[1].productId shouldBe "TST"
                capturedConsumptions[1].childCode shouldBe 1850
                capturedConsumptions[1].units shouldBe BigDecimal.valueOf(2.0)
                capturedConsumptions[1].yearMonth shouldBe YEAR_MONTH
                capturedConsumptions[1].note shouldBe "Note 2"
                capturedConsumptions[1].isRectification shouldBe true

                capturedConsumptions[2].productId shouldBe "TST"
                capturedConsumptions[2].childCode shouldBe 1851
                capturedConsumptions[2].units shouldBe BigDecimal.valueOf(2.0)
                capturedConsumptions[2].yearMonth shouldBe YEAR_MONTH
                capturedConsumptions[2].note shouldBe "Note 3"
                capturedConsumptions[2].isRectification shouldBe true

                capturedConsumptions[3].productId shouldBe "XXX"
                capturedConsumptions[3].childCode shouldBe 1851
                capturedConsumptions[3].units shouldBe BigDecimal.valueOf(2.0)
                capturedConsumptions[3].yearMonth shouldBe YEAR_MONTH
                capturedConsumptions[3].note shouldBe "Note 4"
                capturedConsumptions[3].isRectification shouldBe true

                capturedConsumptions[4].productId shouldBe "TST"
                capturedConsumptions[4].childCode shouldBe 1852
                capturedConsumptions[4].units shouldBe BigDecimal.valueOf(2.0)
                capturedConsumptions[4].yearMonth shouldBe YEAR_MONTH
                capturedConsumptions[4].note shouldBe "Note 5"
                capturedConsumptions[4].isRectification shouldBe true
            }
        }
    }

    private fun allChildrenConsumptions() = listOf(
        YearMonthConsumptionsDTO(
            yearMonth = YEAR_MONTH.toString(),
            grossAmount = 105.4,
            listOf(
                ChildConsumtionDTO(
                    code = 1850,
                    shortName = "Laura Llull",
                    grossAmount = 43.6,
                    listOf(
                        ConsumtionDTO("TST", 4.0, 43.6, "Note 1, Note 2")
                    )
                ),
                ChildConsumtionDTO(
                    code = 1851,
                    shortName = "Aina Llull",
                    grossAmount = 40.0,
                    listOf(
                        ConsumtionDTO("TST", 2.0, 21.8, "Note 3"),
                        ConsumtionDTO("XXX", 2.0, 18.2, "Note 4")
                    )
                ),
                ChildConsumtionDTO(
                    code = 1852,
                    shortName = "Laia Llull",
                    grossAmount = 21.8,
                    listOf(
                        ConsumtionDTO("TST", 2.0, 21.8, "Note 5")
                    )
                )
            )
        )
    )
}

fun mockAuxReaders(customerRepository: CachedCustomerRepository, productRepository: CachedProductRepository) {
    clearMocks(customerRepository, productRepository)
    every { productRepository.getProduct("TST") } returns Product(
        id = "TST",
        name = "TST product",
        shortName = "Test",
        price = BigDecimal.valueOf(10.9)
    )
    every { productRepository.getProduct("XXX") } returns Product(
        id = "XXX",
        name = "XXX product",
        shortName = "XXX product",
        price = BigDecimal.valueOf(9.1)
    )
    every { customerRepository.getChild(1850) } returns testChild1()
    every { customerRepository.getChild(1851) } returns testChild2()
    every { customerRepository.getChild(1852) } returns testChild3()
}

fun mockConsumptionsReader(consumptionRepository: ConsumptionRepository, additionalList: List<Consumption> = emptyList()) {
    clearMocks(consumptionRepository)
    every { consumptionRepository.findByInvoiceIdNull() } returns listOf(
        Consumption(
            id = "AA1",
            childCode = 1850,
            productId = "TST",
            units = BigDecimal.valueOf(2),
            yearMonth = YEAR_MONTH,
            note = "Note 1"
        ),
        Consumption(
            id = "AA2",
            childCode = 1850,
            productId = "TST",
            units = BigDecimal.valueOf(2),
            yearMonth = YEAR_MONTH,
            note = "Note 2"
        ),
        Consumption(
            id = "AA3",
            childCode = 1851,
            productId = "TST",
            units = BigDecimal.valueOf(2),
            yearMonth = YEAR_MONTH,
            note = "Note 3"
        ),
        Consumption(
            id = "AA4",
            childCode = 1851,
            productId = "XXX",
            units = BigDecimal.valueOf(2),
            yearMonth = YEAR_MONTH,
            note = "Note 4"
        ),
        Consumption(
            id = "AA5",
            childCode = 1852,
            productId = "TST",
            units = BigDecimal.valueOf(2),
            yearMonth = YEAR_MONTH,
            note = "Note 5"
        )
    ) + additionalList
}
