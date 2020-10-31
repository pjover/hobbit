package cat.hobbiton.hobbit

import cat.hobbiton.hobbit.domain.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

val DATE: LocalDate = LocalDate.of(2019, 5, 25)
val DATE_TIME: LocalDateTime = LocalDateTime.of(2019, 5, 25, 10, 29, 59)
val YEAR_MONTH: YearMonth = YearMonth.of(2019, 5)

fun completeTestProduct() = Product("TST",
        "Test product",
        "Test",
        BigDecimal.valueOf(10.9)
)

fun testChild1() = Child(
        code = 1,
        name = "Laura",
        surname = "Llull",
        secondSurname = "Bibiloni",
        birthDate = DATE
)

fun testChild2() = Child(
        code = 2,
        name = "Aina",
        surname = "Llull",
        secondSurname = "Bibiloni",
        birthDate = DATE
)

fun testChild3() = Child(
        code = 3,
        name = "Laia",
        surname = "Llull",
        secondSurname = "Bibiloni",
        birthDate = DATE
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

fun testAdults() = listOf(testAdultMother(), testAdultFather())

fun testInvoiceHolder() = InvoiceHolder(
        name = "Joana Bibiloni Oliver",
        taxId = "12238561P",
        address = testAddress(),
        email = "jbibiloni@gmail.com",
        paymentType = PaymentType.BANK_DIRECT_DEBIT,
        bankAccount = "ES28 3066 8859 9782 5852 9057"
)

fun testCustomer() = Customer(
        id = 185,
        children = testChildren2(),
        adults = testAdults(),
        invoiceHolder = testInvoiceHolder()
)

fun testInvoiceFtype(invoiceDate: LocalDate = DATE) = Invoice(
        id = "F-103",
        date = invoiceDate,
        customerId = 148,
        lines = testInvoiceLines(),
        note = "Invoice note",
        emailed = false,
        printed = false,
        paymentType = PaymentType.BANK_DIRECT_DEBIT,
        childrenCodes = listOf(1800, 1801)
)

fun testInvoiceLines() = listOf(
        InvoiceLine(productId = "AAA",
                productName = "AAA product long name",
                units = BigDecimal.valueOf(1),
                productPrice = BigDecimal.valueOf(11),
                taxPercentage = BigDecimal.ZERO
        ),
        InvoiceLine(productId = "BBB",
                productName = "BBB product long name",
                units = BigDecimal.valueOf(3),
                productPrice = BigDecimal.valueOf(5.5),
                taxPercentage = BigDecimal.valueOf(0.1)
        ),
        InvoiceLine(productId = "CCC",
                productName = "CCC product long name",
                units = BigDecimal.valueOf(1.5),
                productPrice = BigDecimal.valueOf(5),
                taxPercentage = BigDecimal.ZERO
        )
)

fun testInvoices(invoiceDate: LocalDate? = DATE) = listOf(
        Invoice(
                id = "F-100",
                date = invoiceDate!!,
                customerId = 148,
                lines = testInvoiceLines(),
                note = "Invoice note",
                emailed = false,
                printed = false,
                paymentType = PaymentType.BANK_DIRECT_DEBIT,
                childrenCodes = listOf(1800, 1801)),
        Invoice(
                id = "F-101",
                date = invoiceDate,
                customerId = 148,
                lines = testInvoiceLines(),
                note = "Invoice note",
                emailed = false,
                printed = false,
                paymentType = PaymentType.BANK_DIRECT_DEBIT,
                childrenCodes = listOf(1801, 1802)),
        Invoice(
                id = "F-102",
                date = invoiceDate,
                customerId = 149,
                lines = testInvoiceLines(),
                note = "Invoice note",
                emailed = false,
                printed = false,
                paymentType = PaymentType.BANK_DIRECT_DEBIT,
                childrenCodes = listOf(1800, 1801, 1802)),
        Invoice(
                id = "F-103",
                customerId = 149,
                date = invoiceDate,
                lines = testInvoiceLines(),
                note = "Invoice note",
                emailed = false,
                printed = false,
                paymentType = PaymentType.BANK_DIRECT_DEBIT,
                childrenCodes = listOf(1800))
)