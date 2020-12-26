package cat.hobbiton.hobbit

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