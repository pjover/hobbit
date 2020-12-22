package cat.hobbiton.hobbit

import cat.hobbiton.hobbit.model.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

val DATE: LocalDate = LocalDate.of(2019, 5, 25)
val DATE_TIME: LocalDateTime = LocalDateTime.of(2019, 5, 25, 10, 29, 59)
val YEAR_MONTH: YearMonth = YearMonth.of(2019, 5)

fun testProduct() = Product("TST",
    "TST product",
    "Test",
    10.9.toBigDecimal()
)

fun testChild1() = Child(
    code = 1850,
    name = "Laura",
    surname = "Llull",
    secondSurname = "Bibiloni",
    birthDate = DATE,
    group = GroupType.EI_1
)

fun testChild2() = Child(
    code = 1851,
    name = "Aina",
    surname = "Llull",
    secondSurname = "Bibiloni",
    birthDate = DATE,
    group = GroupType.EI_1
)

fun testChild3() = Child(
    code = 1852,
    name = "Laia",
    surname = "Llull",
    secondSurname = "Bibiloni",
    birthDate = DATE,
    group = GroupType.EI_2
)

fun testChildren1() = listOf(testChild1())
fun testChildren2() = listOf(testChild1(), testChild2())
fun testChildren3Inactive() = listOf(testChild1(), testChild2(), testChild3().copy(active = false))

fun testAddress() = Address(
    "Arlington Road, 1999 ",
    "07007",
    "Palma",
    "Illes Balears"
)

fun testAdultMother() = Adult(
    name = "Joana",
    surname = "Bibiloni",
    secondSurname = "Oliver",
    role = AdultRole.MOTHER,
    taxId = "12238561P",
    address = testAddress()
)

fun testAdultFather() = Adult(
    name = "Pere",
    surname = "Llull",
    secondSurname = "Adrover",
    role = AdultRole.FATHER
)

fun testAdultTutor() = Adult(
    name = "Silvia",
    surname = "Mayol",
    secondSurname = "Alcover",
    role = AdultRole.TUTOR
)

fun testAdults() = listOf(testAdultMother(), testAdultFather())

fun testInvoiceHolder() = InvoiceHolder(
    name = "Joana Bibiloni Oliver",
    taxId = "12238561P",
    address = testAddress(),
    email = "jbibiloni@gmail.com",
    paymentType = PaymentType.BANK_DIRECT_DEBIT,
    bankAccount = "ES28 3066 8859 9782 5852 9057"
)

fun testCustomer(id: Int = 185, children: List<Child> = testChildren2(), adults: List<Adult> = testAdults(), invoiceHolder: InvoiceHolder = testInvoiceHolder()) = Customer(
    id = id,
    children = children,
    adults = adults,
    invoiceHolder = invoiceHolder
)

fun testInvoice(
    id: Int = 103,
    paymentType: PaymentType = PaymentType.BANK_DIRECT_DEBIT,
    invoiceDate: LocalDate = DATE,
    childrenCodes: List<Int> = listOf(1850, 1851),
    lines: List<InvoiceLine> = testInvoiceLines()) = Invoice(
    id = "${paymentType.sequenceType.prefix}-$id",
    date = invoiceDate,
    customerId = 148,
    lines = lines,
    note = "Invoice note",
    emailed = false,
    printed = false,
    paymentType = paymentType,
    childrenCodes = childrenCodes
)

fun testInvoiceLines() = listOf(
    InvoiceLine(productId = "AAA",
        units = 1.toBigDecimal(),
        productPrice = 11.toBigDecimal(),
        taxPercentage = BigDecimal.ZERO,
        childCode = 1850
    ),
    InvoiceLine(productId = "BBB",
        units = 3.toBigDecimal(),
        productPrice = 5.5.toBigDecimal(),
        taxPercentage = 0.1.toBigDecimal(),
        childCode = 1850
    ),
    InvoiceLine(productId = "CCC",
        units = 1.5.toBigDecimal(),
        productPrice = 5.toBigDecimal(),
        taxPercentage = BigDecimal.ZERO,
        childCode = 1851
    )
)

fun testInvoices(firstId: Int = 100, paymentType: PaymentType = PaymentType.BANK_DIRECT_DEBIT, invoiceDate: LocalDate = DATE) = listOf(
    testInvoice(firstId, paymentType, invoiceDate),
    testInvoice(firstId + 1, paymentType, invoiceDate, listOf(1851, 1852)),
    testInvoice(firstId + 2, paymentType, invoiceDate, listOf(1850, 1851, 1852)),
    testInvoice(firstId + 3, paymentType, invoiceDate, listOf(1850))
)

fun testProducts() = listOf(
    Product(
        "AAA",
        "AAA product long name",
        "AAA product",
        price = 11.toBigDecimal()
    ),
    Product(
        "BBB",
        "BBB product long name",
        "BBB product",
        price = 5.5.toBigDecimal(),
        taxPercentage = 0.1.toBigDecimal()),
    Product(
        "CCC",
        "CCC product long name",
        "CCC product",
        price = 5.toBigDecimal()
    ))