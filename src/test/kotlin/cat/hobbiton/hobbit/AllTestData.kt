package cat.hobbiton.hobbit

import cat.hobbiton.hobbit.model.Address
import cat.hobbiton.hobbit.model.Child
import cat.hobbiton.hobbit.model.GroupType
import cat.hobbiton.hobbit.model.Product
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
    surname = "Llull",
    secondSurname = "Bibiloni",
    birthDate = DATE,
    group = GroupType.EI_2
)

val testChild1870 = Child(
    code = 1870,
    name = "Ona",
    surname = "Llull",
    secondSurname = "Bibiloni",
    birthDate = DATE,
    group = GroupType.EI_3
)

val testChild1880 = Child(
    code = 1880,
    name = "Nil",
    surname = "Llull",
    secondSurname = "Bibiloni",
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