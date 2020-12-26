package cat.hobbiton.hobbit

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
    taxId = "60235657Z",
    birthDate = DATE,
    group = GroupType.EI_1
)

fun testChild3() = Child(
    code = 1860,
    name = "Laia",
    surname = "Llull",
    secondSurname = "Bibiloni",
    birthDate = DATE,
    group = GroupType.EI_2
)

fun testChild4() = Child(
    code = 1870,
    name = "Ona",
    surname = "Llull",
    secondSurname = "Bibiloni",
    birthDate = DATE,
    group = GroupType.EI_3
)

fun testChild5() = Child(
    code = 1880,
    name = "Nil",
    surname = "Llull",
    secondSurname = "Bibiloni",
    birthDate = DATE,
    group = GroupType.EI_3
)