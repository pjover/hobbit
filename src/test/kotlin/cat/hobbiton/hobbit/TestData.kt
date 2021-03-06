package cat.hobbiton.hobbit

import cat.hobbiton.hobbit.model.*
import java.time.LocalDate
import java.time.YearMonth

const val YEAR: Int = 2019
val YEAR_MONTH: YearMonth = YearMonth.of(2019, 5)
val DATE: LocalDate = LocalDate.of(2019, 5, 25)

val testProduct1 = Product("TST",
    "TST product",
    "TstProduct",
    10.9.toBigDecimal()
)

val testProduct2 = Product("XXX",
    "XXX product",
    "XxxProduct",
    9.1.toBigDecimal()
)

val testProduct3 = Product("YYY",
    "YYY product",
    "YyyProduct",
    5.0.toBigDecimal()
)

val testProductsMap = mapOf(
    "TST" to testProduct1,
    "XXX" to testProduct2,
    "YYY" to testProduct3
)

val testChild1850 = Child(
    code = 1850,
    name = "Laura",
    surname = "Llull",
    secondSurname = "Bibiloni",
    birthDate = DATE,
    group = GroupType.EI_1
)

val testChild1851 = Child(
    code = 1851,
    name = "Aina",
    surname = "Llull",
    secondSurname = "Bibiloni",
    taxId = "60235657Z",
    birthDate = DATE,
    group = GroupType.EI_1
)

val testChild1860 = Child(
    code = 1860,
    name = "Laia",
    surname = "Mayol",
    secondSurname = "Alcover",
    birthDate = DATE,
    group = GroupType.EI_2
)

val testChild1870 = Child(
    code = 1870,
    name = "Ona",
    surname = "Santamaria",
    birthDate = DATE,
    group = GroupType.EI_3
)

val testChild1880 = Child(
    code = 1880,
    name = "Nil",
    surname = "Brown",
    taxId = "12238561P",
    birthDate = LocalDate.of(2019, 1, 28),
    group = GroupType.EI_1,
    note = "Nil's note"
)

val testChildren185 = listOf(testChild1850, testChild1851)
val testChildren186 = listOf(testChild1860)
val testChildren187 = listOf(testChild1870)
val testChildren188 = listOf(testChild1880)

fun testAddress() = Address(
    "Arlington Road, 1999",
    "07007",
    "Palma",
    "Illes Balears"
)

val testAdultMother185 = Adult(
    name = "Joana",
    surname = "Bibiloni",
    secondSurname = "Oliver",
    role = AdultRole.MOTHER,
    taxId = "12238561P",
    address = testAddress(),
    email = "jbibiloni@gmail.com",
    mobilePhone = "644432654"
)

val testAdultFather185 = Adult(
    name = "Pere",
    surname = "Llull",
    secondSurname = "Adrover",
    role = AdultRole.FATHER,
    taxId = "58921214K",
    email = "pllull@gmail.com",
    mobilePhone = "666432654"
)

val testAdultTutor186 = Adult(
    name = "Silvia",
    surname = "Mayol",
    secondSurname = "Alcover",
    role = AdultRole.TUTOR,
    taxId = "97505522N",
    email = "silvia@gmail.com",
    mobilePhone = "677732654"
)

val testAdultMother187 = Adult(
    name = "Cara",
    surname = "Santamaria",
    role = AdultRole.MOTHER,
    taxId = "X2113060G",
    email = "cara@sgu.org",
    mobilePhone = "+1699932654"
)

val testAdultTutor188 = Adult(
    name = "Andrew",
    surname = "Brown",
    role = AdultRole.TUTOR,
    taxId = "Y8304421D",
    email = "abrown@gmail.com"
)

val testInvoiceHolder185 = InvoiceHolder(
    name = "Joana Bibiloni Oliver",
    taxId = "12238561P",
    address = testAddress(),
    email = "jbibiloni@gmail.com",
    paymentType = PaymentType.BANK_DIRECT_DEBIT,
    bankAccount = "ES28 3066 8859 9782 5852 9057"
)

val testCustomer185 = Customer(
    id = 185,
    children = testChildren185,
    adults = listOf(testAdultMother185, testAdultFather185),
    invoiceHolder = testInvoiceHolder185
)

val testCustomer186 = Customer(
    id = 186,
    children = testChildren186,
    adults = listOf(testAdultTutor186),
    invoiceHolder = InvoiceHolder(
        name = "Silvia Mayol",
        taxId = "97505522N",
        address = testAddress(),
        paymentType = PaymentType.BANK_DIRECT_DEBIT,
        bankAccount = "ES5131906344094856219847",
        email = "silvia@gmail.com"
    )
)

val testCustomer187 = Customer(
    id = 187,
    children = testChildren187,
    adults = listOf(testAdultMother187),
    invoiceHolder = InvoiceHolder(
        name = "Cara Santamaria",
        taxId = "X2113060G",
        address = testAddress(),
        paymentType = PaymentType.BANK_DIRECT_DEBIT,
        bankAccount = "GB86BARC20038048628281",
        email = "cara@sgu.org"
    )
)

val testCustomer188 = Customer(
    id = 188,
    children = testChildren188,
    adults = listOf(testAdultTutor188),
    invoiceHolder = InvoiceHolder(
        name = "Andrew Brown",
        taxId = "Y8304421D",
        address = testAddress(),
        paymentType = PaymentType.BANK_DIRECT_DEBIT,
        bankAccount = "SE5696461254175518254512",
        email = "abrown@gmail.com"
    )
)

val testCustomers = listOf(
    testCustomer185,
    testCustomer186,
    testCustomer187,
    testCustomer188
)

val testCustomersMap = mapOf(
    185 to testCustomer185,
    186 to testCustomer186,
    187 to testCustomer187,
    188 to testCustomer188
)

val testInvoice185 = Invoice(
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

val testInvoice186 = Invoice(
    id = "??",
    customerId = 186,
    date = DATE,
    yearMonth = YEAR_MONTH,
    childrenCodes = listOf(1860),
    paymentType = PaymentType.BANK_DIRECT_DEBIT,
    lines = listOf(
        InvoiceLine(
            productId = "TST",
            units = 2.toBigDecimal(),
            productPrice = 10.9.toBigDecimal(),
            childCode = 1860
        )
    ),
    note = "Note 5"
)

val testInvoice187 = Invoice(
    id = "??",
    customerId = 187,
    date = DATE,
    yearMonth = YEAR_MONTH,
    childrenCodes = listOf(1870),
    paymentType = PaymentType.RECTIFICATION,
    lines = listOf(
        InvoiceLine(
            productId = "TST",
            units = (-2).toBigDecimal(),
            productPrice = 10.9.toBigDecimal(),
            childCode = 1870
        )
    ),
    note = "Note 6"
)

val testInvoices = listOf(
    testInvoice185,
    testInvoice186,
    testInvoice187
)