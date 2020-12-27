package cat.hobbiton.hobbit.service.generate.bdd.string

import cat.hobbiton.hobbit.DATE
import cat.hobbiton.hobbit.init.BusinessProperties
import cat.hobbiton.hobbit.model.*
import cat.hobbiton.hobbit.model.extension.totalAmount
import cat.hobbiton.hobbit.service.aux.TimeService
import cat.hobbiton.hobbit.testChildren185
import cat.hobbiton.hobbit.testProductsMap
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class InvoicesToBddMapperImplTest : DescribeSpec() {

    init {
        val bddProperties = mockk<BusinessProperties>()
        val timeService = mockk<TimeService>()
        val sut = InvoicesToBddMapperImpl(bddProperties, timeService)

        describe("map()") {
            every { bddProperties.businessName } returns "Centre d'Educació Infantil Hobbiton, S.L."
            every { bddProperties.addressLine1 } returns "Carrer de Bisbe Rafael Josep Verger, 4"
            every { bddProperties.addressLine2 } returns "07010 Palma, Illes Balears"
            every { bddProperties.addressLine3 } returns "Tel.: 971 90 99 88"
            every { bddProperties.addressLine4 } returns "www.hobbiton.es . hobbit@hobbiton.es"
            every { bddProperties.taxIdLine } returns "NIF: B57398000"
            every { bddProperties.bddBusinessPrefix } returns "HOBB"
            every { bddProperties.bddBusinessId } returns "ES92000B57398000"
            every { bddProperties.bddBusinessIban } returns "ES8004872157762000009714"
            every { bddProperties.bddCountry } returns "ES"
            every { bddProperties.bddBankBic } returns "GBMNESMMXXX"
            every { bddProperties.bddPurposeCode } returns "OTHR"
            every { timeService.currentLocalDateTime } returns LocalDateTime.of(2018, 7, 7, 20, 43, 8)

            val customersMap = mapOf(
                148 to bddTestCustomer(),
                149 to bddTestBusinessCustomer()
            )

            val invoices = bddTestInvoices()
            val actual = sut.map(invoices, customersMap, testProductsMap)

            it("returns the complete Bdd object") {
                actual shouldBe Bdd(
                    messageIdentification = "HOBB-20180707204308000-24",
                    creationDateTime = "2018-07-07T20:43:08",
                    numberOfTransactions = 4,
                    controlSum = "146.60",
                    name = "Centre d'Educació Infantil Hobbiton, S.L.",
                    identification = "ES92000B57398000",
                    requestedCollectionDate = "2018-07-07",
                    country = "ES",
                    addressLine1 = "Carrer de Bisbe Rafael Josep Verger, 4",
                    addressLine2 = "07010 Palma, Illes Balears",
                    iban = "ES8004872157762000009714",
                    bic = "GBMNESMMXXX",
                    details = buildTestBddDetailList("HOBB-20180707204308000-24", invoices))
            }
        }
    }
}

fun buildTestBddDetailList(messageIdentification: String, invoices: List<Invoice>): List<BddDetail> {

    return listOf(
        buildTestBddDetail(messageIdentification,
            "ES4200036361882D",
            "Nom de la mare 1er llinatge_mare 2on llinatge_mare",
            false,
            invoices[0]),
        buildTestBddDetail(messageIdentification,
            "ES4200036361882D",
            "Nom de la mare 1er llinatge_mare 2on llinatge_mare",
            false,
            invoices[1]),
        buildTestBddDetail(messageIdentification,
            "ES5500037866397W",
            "Nom empresa",
            true,
            invoices[2]),
        buildTestBddDetail(messageIdentification,
            "ES5500037866397W",
            "Nom empresa",
            true,
            invoices[3]))
}

private fun buildTestBddDetail(messageIdentification: String, identification: String, name: String, isBusiness: Boolean, invoice: Invoice): BddDetail {
    return BddDetail(
        name = name,
        instructedAmount = invoice.totalAmount().toString(),
        dateOfSignature = "2018-07-07",
        endToEndIdentifier = messageIdentification + "." + invoice.id,
        identification = identification,
        iban = "ES2830668859978258529057",
        purposeCode = "OTHR",
        remittanceInformation = "1xTstProduct, 3xXxxProduct, 1.5xYyyProduct",
        isBusiness = isBusiness)
}

private fun bddTestBusinessCustomer(id: Int = 149, paymentType: PaymentType = PaymentType.BANK_TRANSFER): Customer {
    return Customer(
        id = id,
        children = testChildren185,
        adults = listOf(
            Adult(
                name = "Nom de la mare",
                surname = "1er llinatge_mare",
                secondSurname = "2on llinatge_mare",
                taxId = "36361882D",
                role = AdultRole.MOTHER
            ),
            Adult(
                name = "Nom del pare",
                surname = "1er llinatge_pare",
                secondSurname = "2on llinatge_pare",
                taxId = "71032204Q",
                role = AdultRole.FATHER
            )
        ),
        invoiceHolder = InvoiceHolder(
            name = "Nom empresa",
            taxId = "37866397W",
            address = Address(
                street = "Address first line",
                zipCode = "07007",
                city = "Palma",
                state = "Illes Balears"
            ),
            email = "email@gmail.com",
            paymentType = paymentType,
            bankAccount = "ES2830668859978258529057",
            isBusiness = true
        ),
        note = "Nota del client",
        language = Language.CA,
        active = true
    )
}


private fun bddTestCustomer(id: Int = 148, paymentType: PaymentType = PaymentType.BANK_DIRECT_DEBIT): Customer {
    return Customer(
        id = id,
        children = testChildren185,
        adults = listOf(
            Adult(
                name = "Nom de la mare",
                surname = "1er llinatge_mare",
                secondSurname = "2on llinatge_mare",
                taxId = "36361882D",
                role = AdultRole.MOTHER
            ),
            Adult(
                name = "Nom del pare",
                surname = "1er llinatge_pare",
                secondSurname = "2on llinatge_pare",
                taxId = "71032204Q",
                role = AdultRole.FATHER
            )
        ),
        invoiceHolder = bddTestInvoiceHolder(paymentType),
        note = "Nota del client",
        language = Language.CA,
        active = true
    )
}

private fun bddTestInvoiceHolder(paymentType: PaymentType = PaymentType.BANK_DIRECT_DEBIT) = InvoiceHolder(
    name = "Nom de la mare 1er llinatge_mare 2on llinatge_mare",
    taxId = "36361882D",
    address = Address(
        street = "Address first line",
        zipCode = "07007",
        city = "Palma",
        state = "Illes Balears"
    ),
    email = "email@gmail.com",
    paymentType = paymentType,
    bankAccount = "ES2830668859978258529057"
)

fun bddTestInvoices(invoiceDate: LocalDate? = DATE): List<Invoice> {
    return listOf(
        Invoice(
            id = "F-100",
            date = invoiceDate!!,
            customerId = 148,
            lines = bddTestInvoiceLines(),
            note = "Invoice note",
            emailed = false,
            printed = false,
            paymentType = PaymentType.BANK_DIRECT_DEBIT,
            childrenCodes = listOf(1800, 1801)),
        Invoice(
            id = "F-101",
            date = invoiceDate,
            customerId = 148,
            lines = bddTestInvoiceLines(),
            note = "Invoice note",
            emailed = false,
            printed = false,
            paymentType = PaymentType.BANK_DIRECT_DEBIT,
            childrenCodes = listOf(1801, 1802)),
        Invoice(
            id = "F-102",
            date = invoiceDate,
            customerId = 149,
            lines = bddTestInvoiceLines(),
            note = "Invoice note",
            emailed = false,
            printed = false,
            paymentType = PaymentType.BANK_DIRECT_DEBIT,
            childrenCodes = listOf(1800, 1801, 1802)),
        Invoice(
            id = "F-103",
            customerId = 149,
            date = invoiceDate,
            lines = bddTestInvoiceLines(),
            note = "Invoice note",
            emailed = false,
            printed = false,
            paymentType = PaymentType.BANK_DIRECT_DEBIT,
            childrenCodes = listOf(1800))
    )
}

private fun bddTestInvoiceLines(): List<InvoiceLine> {
    return listOf(
        InvoiceLine(productId = "TST",
            units = 1.toBigDecimal(),
            productPrice = 11.toBigDecimal(),
            taxPercentage = BigDecimal.ZERO,
            childCode = 1850),
        InvoiceLine(productId = "XXX",
            units = 3.toBigDecimal(),
            productPrice = 5.5.toBigDecimal(),
            taxPercentage = 0.1.toBigDecimal(),
            childCode = 1850),
        InvoiceLine(productId = "YYY",
            units = 1.5.toBigDecimal(),
            productPrice = 5.toBigDecimal(),
            taxPercentage = BigDecimal.ZERO,
            childCode = 1850)
    )
}
