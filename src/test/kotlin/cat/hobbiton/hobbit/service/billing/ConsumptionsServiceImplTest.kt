package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.*
import cat.hobbiton.hobbit.api.model.*
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.CachedProductRepository
import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.model.Consumption
import cat.hobbiton.hobbit.service.aux.TimeService
import cat.hobbiton.hobbit.util.error.AppException
import cat.hobbiton.hobbit.util.error.NotFoundException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertFailsWith

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
                every { consumptionRepository.findByChildCodeAndInvoiceId(1) } returns emptyList()

                val actual = sut.getChildConsumptions(1)

                it("throws an error") {
                    actual shouldBe emptyList()
                }
            }

            context("with consumptions") {

                mockAuxReaders(customerRepository, productRepository)
                every { consumptionRepository.findByChildCodeAndInvoiceId(1) } returns listOf(
                    Consumption(
                        id = "AA1",
                        childCode = 1850,
                        productId = "TST",
                        units = 2.toBigDecimal(),
                        yearMonth = YEAR_MONTH,
                        note = "Note 1"
                    ),
                    Consumption(
                        id = "AA2",
                        childCode = 1850,
                        productId = "TST",
                        units = 2.toBigDecimal(),
                        yearMonth = YEAR_MONTH,
                        note = "Note 2"
                    ),
                    Consumption(
                        id = "AA3",
                        childCode = 1850,
                        productId = "TST",
                                units = 2.toBigDecimal(),
                                yearMonth = YEAR_MONTH,
                                note = "Note 3"
                        ),
                        Consumption(
                                id = "AA4",
                                childCode = 1850,
                                productId = "XXX",
                                units = 2.toBigDecimal(),
                                yearMonth = YEAR_MONTH,
                                note = "Note 4"
                        ),
                        Consumption(
                                id = "AA5",
                                childCode = 1850,
                                productId = "TST",
                                units = 2.toBigDecimal(),
                                yearMonth = YEAR_MONTH,
                                note = "Note 5"
                        )
                )

                val actual = sut.getChildConsumptions(1)

                it("return the consumpion of this child") {
                    actual shouldBe listOf(
                            YearMonthConsumptionsDTO(
                                    yearMonth = YEAR_MONTH.toString(),
                                    grossAmount = 105.4.toBigDecimal(),
                                    listOf(
                                            ChildConsumtionDTO(
                                                    code = 1850,
                                                    shortName = "Laura Llull",
                                                    grossAmount = 105.4.toBigDecimal(),
                                                    listOf(
                                                            ConsumtionDTO("TST", 8.toBigDecimal(), 87.2.toBigDecimal(), "Note 1, Note 2, Note 3, Note 5"),
                                                            ConsumtionDTO("XXX", 2.toBigDecimal(), 18.2.toBigDecimal(), "Note 4")
                                                    )
                                            )
                                    )
                            )
                    )
                }
            }

            context("with empty notes consumptions") {

                mockAuxReaders(customerRepository, productRepository)
                every { consumptionRepository.findByChildCodeAndInvoiceId(1) } returns listOf(
                    Consumption(
                        id = "AA1",
                        childCode = 1850,
                        productId = "TST",
                        units = 2.toBigDecimal(),
                        yearMonth = YEAR_MONTH,
                        note = ""
                    ),
                    Consumption(
                        id = "AA2",
                        childCode = 1850,
                        productId = "TST",
                        units = 2.toBigDecimal(),
                        yearMonth = YEAR_MONTH,
                        note = ""
                    ),
                    Consumption(
                        id = "AA3",
                        childCode = 1850,
                        productId = "TST",
                        units = 2.toBigDecimal(),
                        yearMonth = YEAR_MONTH,
                        note = ""
                    ),
                    Consumption(
                        id = "AA5",
                        childCode = 1850,
                        productId = "TST",
                        units = 2.toBigDecimal(),
                        yearMonth = YEAR_MONTH,
                        note = ""
                    )
                )

                val actual = sut.getChildConsumptions(1)

                it("return the consumpion of this child") {
                    actual shouldBe listOf(
                        YearMonthConsumptionsDTO(
                            yearMonth = YEAR_MONTH.toString(),
                            grossAmount = 87.2.toBigDecimal(),
                            listOf(
                                ChildConsumtionDTO(
                                    code = 1850,
                                    shortName = "Laura Llull",
                                    grossAmount = 87.2.toBigDecimal(),
                                    listOf(
                                        ConsumtionDTO("TST", 8.toBigDecimal(), 87.2.toBigDecimal(), ""),
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
                every { consumptionRepository.findByInvoiceId() } returns emptyList()

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
                                units = 2.toBigDecimal(),
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
                                                    SetConsumtionDTO("TST", 4.toBigDecimal(), "Note 1, Note 2")
                                            )
                                    ),
                                    SetChildConsumtionDTO(
                                            code = 1851,
                                            listOf(
                                                    SetConsumtionDTO("TST", 2.toBigDecimal(), "Note 3"),
                                                    SetConsumtionDTO("XXX", 2.toBigDecimal(), "Note 4")
                                            )
                                    ),
                                    SetChildConsumtionDTO(
                                            code = 1852,
                                            listOf(
                                                    SetConsumtionDTO("TST", 2.toBigDecimal(), "Note 5")
                                            )
                                    )
                            )
                    )
                }
            }
        }

        describe("setConsumptions") {

            context("Child not found") {
                mockAuxReaders(customerRepository, productRepository)
                mockConsumptionsReader(
                    consumptionRepository,
                    listOf(
                        Consumption(
                            id = "AA1",
                            childCode = 7777,
                            productId = "TST",
                            units = 2.toBigDecimal(),
                            yearMonth = YEAR_MONTH,
                                        note = "Note 1"
                                )
                        )
                )
                val executor = {
                    sut.setConsumptions(
                            SetYearMonthConsumptionsDTO(
                                    yearMonth = YEAR_MONTH.toString(),
                                    listOf(
                                            SetChildConsumtionDTO(
                                                    code = 7777,
                                                    listOf(
                                                            SetConsumtionDTO("TST", 2.toBigDecimal(), "Note 1"),
                                                            SetConsumtionDTO("TST", 2.toBigDecimal(), "Note 2")
                                                    )
                                            )
                                    )
                            )
                    )
                }

                it("throws an error") {
                    val exception = assertFailsWith<NotFoundException> { executor.invoke() }
                    exception.errorMessage shouldBe ErrorMessages.ERROR_CHILD_NOT_FOUND
                }
            }

            context("Inactive consumer") {
                mockAuxReaders(customerRepository, productRepository)
                mockConsumptionsReader(
                    consumptionRepository,
                    listOf(
                        Consumption(
                            id = "AA2",
                            childCode = 8888,
                            productId = "TST",
                            units = 2.toBigDecimal(),
                            yearMonth = YEAR_MONTH,
                            note = "Note 1"
                        )
                    )
                )
                val executor = {
                    sut.setConsumptions(
                        SetYearMonthConsumptionsDTO(
                            yearMonth = YEAR_MONTH.toString(),
                            listOf(
                                SetChildConsumtionDTO(
                                    code = 8888,
                                    listOf(
                                        SetConsumtionDTO("TST", 2.toBigDecimal(), "Note 1"),
                                        SetConsumtionDTO("TST", 2.toBigDecimal(), "Note 2")
                                    )
                                )
                            )
                        )
                    )
                }

                it("throws an error") {
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.errorMessage shouldBe ErrorMessages.ERROR_CUSTOMER_INACTIVE
                }
            }

            context("Inactive child") {
                mockAuxReaders(customerRepository, productRepository)
                mockConsumptionsReader(
                    consumptionRepository,
                    listOf(
                        Consumption(
                            id = "AA2",
                            childCode = 9999,
                            productId = "TST",
                            units = 2.toBigDecimal(),
                            yearMonth = YEAR_MONTH,
                            note = "Note 1"
                        )
                    )
                )
                val executor = {
                    sut.setConsumptions(
                        SetYearMonthConsumptionsDTO(
                            yearMonth = YEAR_MONTH.toString(),
                            listOf(
                                SetChildConsumtionDTO(
                                    code = 9999,
                                    listOf(
                                        SetConsumtionDTO("TST", 2.toBigDecimal(), "Note 1"),
                                        SetConsumtionDTO("TST", 2.toBigDecimal(), "Note 2")
                                    )
                                )
                            )
                        )
                    )
                }

                it("throws an error") {
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.errorMessage shouldBe ErrorMessages.ERROR_CHILD_INACTIVE
                }
            }

            context("Known children") {
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
                                                        SetConsumtionDTO("TST", 2.toBigDecimal(), "Note 1"),
                                                        SetConsumtionDTO("TST", 2.toBigDecimal(), "Note 2")
                                                )
                                        ),
                                        SetChildConsumtionDTO(
                                                code = 1851,
                                                listOf(
                                                        SetConsumtionDTO("TST", 2.toBigDecimal(), "Note 3"),
                                                        SetConsumtionDTO("XXX", 2.toBigDecimal(), "Note 4")
                                                )
                                        ),
                                        SetChildConsumtionDTO(
                                                code = 1852,
                                                listOf(
                                                        SetConsumtionDTO("TST", 2.toBigDecimal(), "Note 5")
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
                    capturedConsumptions[0].units shouldBe 2.toBigDecimal()
                    capturedConsumptions[0].yearMonth shouldBe YEAR_MONTH
                    capturedConsumptions[0].note shouldBe "Note 1"
                    capturedConsumptions[0].isRectification shouldBe false

                    capturedConsumptions[1].productId shouldBe "TST"
                    capturedConsumptions[1].childCode shouldBe 1850
                    capturedConsumptions[1].units shouldBe 2.toBigDecimal()
                    capturedConsumptions[1].yearMonth shouldBe YEAR_MONTH
                    capturedConsumptions[1].note shouldBe "Note 2"
                    capturedConsumptions[1].isRectification shouldBe false

                    capturedConsumptions[2].productId shouldBe "TST"
                    capturedConsumptions[2].childCode shouldBe 1851
                    capturedConsumptions[2].units shouldBe 2.toBigDecimal()
                    capturedConsumptions[2].yearMonth shouldBe YEAR_MONTH
                    capturedConsumptions[2].note shouldBe "Note 3"
                    capturedConsumptions[2].isRectification shouldBe false

                    capturedConsumptions[3].productId shouldBe "XXX"
                    capturedConsumptions[3].childCode shouldBe 1851
                    capturedConsumptions[3].units shouldBe 2.toBigDecimal()
                    capturedConsumptions[3].yearMonth shouldBe YEAR_MONTH
                    capturedConsumptions[3].note shouldBe "Note 4"
                    capturedConsumptions[3].isRectification shouldBe false

                    capturedConsumptions[4].productId shouldBe "TST"
                    capturedConsumptions[4].childCode shouldBe 1852
                    capturedConsumptions[4].units shouldBe 2.toBigDecimal()
                    capturedConsumptions[4].yearMonth shouldBe YEAR_MONTH
                    capturedConsumptions[4].note shouldBe "Note 5"
                    capturedConsumptions[4].isRectification shouldBe false
                }
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
                                                    SetConsumtionDTO("TST", 2.toBigDecimal(), "Note 1"),
                                                    SetConsumtionDTO("TST", 2.toBigDecimal(), "Note 2")
                                            )
                                    ),
                                    SetChildConsumtionDTO(
                                            code = 1851,
                                            listOf(
                                                    SetConsumtionDTO("TST", 2.toBigDecimal(), "Note 3"),
                                                    SetConsumtionDTO("XXX", 2.toBigDecimal(), "Note 4")
                                            )
                                    ),
                                    SetChildConsumtionDTO(
                                            code = 1852,
                                            listOf(
                                                    SetConsumtionDTO("TST", 2.toBigDecimal(), "Note 5")
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
                capturedConsumptions[0].units shouldBe 2.toBigDecimal()
                capturedConsumptions[0].yearMonth shouldBe YEAR_MONTH
                capturedConsumptions[0].note shouldBe "Note 1"
                capturedConsumptions[0].isRectification shouldBe true

                capturedConsumptions[1].productId shouldBe "TST"
                capturedConsumptions[1].childCode shouldBe 1850
                capturedConsumptions[1].units shouldBe 2.toBigDecimal()
                capturedConsumptions[1].yearMonth shouldBe YEAR_MONTH
                capturedConsumptions[1].note shouldBe "Note 2"
                capturedConsumptions[1].isRectification shouldBe true

                capturedConsumptions[2].productId shouldBe "TST"
                capturedConsumptions[2].childCode shouldBe 1851
                capturedConsumptions[2].units shouldBe 2.toBigDecimal()
                capturedConsumptions[2].yearMonth shouldBe YEAR_MONTH
                capturedConsumptions[2].note shouldBe "Note 3"
                capturedConsumptions[2].isRectification shouldBe true

                capturedConsumptions[3].productId shouldBe "XXX"
                capturedConsumptions[3].childCode shouldBe 1851
                capturedConsumptions[3].units shouldBe 2.toBigDecimal()
                capturedConsumptions[3].yearMonth shouldBe YEAR_MONTH
                capturedConsumptions[3].note shouldBe "Note 4"
                capturedConsumptions[3].isRectification shouldBe true

                capturedConsumptions[4].productId shouldBe "TST"
                capturedConsumptions[4].childCode shouldBe 1852
                capturedConsumptions[4].units shouldBe 2.toBigDecimal()
                capturedConsumptions[4].yearMonth shouldBe YEAR_MONTH
                capturedConsumptions[4].note shouldBe "Note 5"
                capturedConsumptions[4].isRectification shouldBe true
            }
        }
    }

    private fun allChildrenConsumptions() = listOf(
            YearMonthConsumptionsDTO(
                    yearMonth = YEAR_MONTH.toString(),
                    grossAmount = 105.4.toBigDecimal(),
                    listOf(
                            ChildConsumtionDTO(
                                    code = 1850,
                                    shortName = "Laura Llull",
                                    grossAmount = 43.6.toBigDecimal(),
                                    listOf(
                                            ConsumtionDTO("TST", 4.toBigDecimal(), 43.6.toBigDecimal(), "Note 1, Note 2")
                                    )
                            ),
                            ChildConsumtionDTO(
                                    code = 1851,
                                    shortName = "Aina Llull",
                                    grossAmount = 40.0.toBigDecimal(),
                                    listOf(
                                            ConsumtionDTO("TST", 2.toBigDecimal(), 21.8.toBigDecimal(), "Note 3"),
                                            ConsumtionDTO("XXX", 2.toBigDecimal(), 18.2.toBigDecimal(), "Note 4")
                                    )
                            ),
                            ChildConsumtionDTO(
                                    code = 1852,
                                    shortName = "Laia Mayol",
                                    grossAmount = 21.8.toBigDecimal(),
                                    listOf(
                                            ConsumtionDTO("TST", 2.toBigDecimal(), 21.8.toBigDecimal(), "Note 5")
                                    )
                            )
                    )
            )
    )
}

fun mockAuxReaders(customerRepository: CachedCustomerRepository, productRepository: CachedProductRepository) {
    clearMocks(customerRepository, productRepository)
    every { productRepository.getProduct("TST") } returns testProduct1
    every { productRepository.getProduct("XXX") } returns testProduct2
    val testCustomer185plus = testCustomer185
        .copy(children = listOf(testChild1850, testChild1851, testChild1851.copy(code = 1852)))
    every { customerRepository.getCustomerByChildCode(1850) } returns testCustomer185plus
    every { customerRepository.getChild(1850) } returns testChild1850
    every { customerRepository.getCustomerByChildCode(1851) } returns testCustomer185plus
    every { customerRepository.getChild(1851) } returns testChild1851
    every { customerRepository.getCustomerByChildCode(1852) } returns testCustomer185plus
    every { customerRepository.getChild(1852) } returns testChild1860
    every { customerRepository.getCustomerByChildCode(7777) } throws NotFoundException(
        ErrorMessages.ERROR_CHILD_NOT_FOUND,
        7777
    )
    every { customerRepository.getCustomerByChildCode(8888) } returns testCustomer186.copy(active = false)
    val inactiveChild = testChild1860.copy(code = 9999, active = false)
    every { customerRepository.getCustomerByChildCode(9999) } returns testCustomer186.copy(
        children = listOf(
            inactiveChild
        )
    )
}

fun mockConsumptionsReader(consumptionRepository: ConsumptionRepository, additionalList: List<Consumption> = emptyList()) {
    clearMocks(consumptionRepository)
    every { consumptionRepository.findByInvoiceId() } returns listOf(
        Consumption(
            id = "AA1",
            childCode = 1850,
            productId = "TST",
            units = 2.toBigDecimal(),
            yearMonth = YEAR_MONTH,
            note = "Note 1"
        ),
        Consumption(
            id = "AA2",
            childCode = 1850,
            productId = "TST",
            units = 2.toBigDecimal(),
            yearMonth = YEAR_MONTH,
            note = "Note 2"
        ),
        Consumption(
            id = "AA3",
            childCode = 1851,
            productId = "TST",
                    units = 2.toBigDecimal(),
                    yearMonth = YEAR_MONTH,
                    note = "Note 3"
            ),
            Consumption(
                    id = "AA4",
                    childCode = 1851,
                    productId = "XXX",
                    units = 2.toBigDecimal(),
                    yearMonth = YEAR_MONTH,
                    note = "Note 4"
            ),
            Consumption(
                    id = "AA5",
                    childCode = 1852,
                    productId = "TST",
                    units = 2.toBigDecimal(),
                    yearMonth = YEAR_MONTH,
                    note = "Note 5"
            )
    ) + additionalList
}
