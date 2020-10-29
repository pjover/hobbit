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
        surname = "Llull Bibiloni",
        birthDate = DATE
)

fun testChild2() = Child(
        code = 2,
        name = "Aina",
        surname = "Llull Bibiloni",
        birthDate = DATE
)

fun testChild3() = Child(
        code = 3,
        name = "Laia",
        surname = "Llull Bibiloni",
        birthDate = DATE
)

fun testChildren1() = listOf(testChild1())
fun testChildren2() = listOf(testChild1(), testChild2())
fun testChildren3() = listOf(testChild1(), testChild2(), testChild3())

fun testAddress() = Address(
        "Arlington Road, 1999 ",
        "07007",
        "Palma",
        "Illes Balears"
)

fun testAdultMother() = Adult(
        name = "Joana",
        surname = "Bibiloni Pinya",
        role = AdultRole.MOTHER,
        taxId = "12238561P",
        address = testAddress()
)

fun testAdultFather() = Adult(
        name = "Pere",
        surname = "Llull Adrover",
        role = AdultRole.FATHER
)

fun testAdults() = listOf(testAdultMother(), testAdultFather())

fun testInvoiceHolder() = InvoiceHolder(
        name = "Joana Bibiloni Pinya",
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
