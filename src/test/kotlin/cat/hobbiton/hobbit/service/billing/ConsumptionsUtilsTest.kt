package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.YEAR_MONTH
import cat.hobbiton.hobbit.model.Consumption
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.math.BigDecimal

class ConsumptionsUtilsTest : DescribeSpec() {

    init {
        describe("groupConsumptions") {

            val actual = groupConsumptions(
                childCode = 1,
                consumptions = listOf(
                    Consumption(
                        id = "AA1",
                        childCode = 1,
                        productId = "TST",
                        units = BigDecimal.valueOf(2),
                        yearMonth = YEAR_MONTH,
                        note = "Note 1",
                        invoicedOn = null
                    ),
                    Consumption(
                        id = "AA2",
                        childCode = 1,
                        productId = "TST",
                        units = BigDecimal.valueOf(2),
                        yearMonth = YEAR_MONTH,
                        note = "Note 2",
                        invoicedOn = null
                    ),
                    Consumption(
                        id = "AA3",
                        childCode = 1,
                        productId = "TST",
                        units = BigDecimal.valueOf(2),
                        yearMonth = YEAR_MONTH,
                        note = "Note 3",
                        invoicedOn = null
                    ),
                    Consumption(
                        id = "AA4",
                        childCode = 1,
                        productId = "STS",
                        units = BigDecimal.valueOf(2),
                        yearMonth = YEAR_MONTH,
                        note = "Note 4",
                        invoicedOn = null
                    ),
                    Consumption(
                        id = "AA5",
                        childCode = 1,
                        productId = "TST",
                        units = BigDecimal.valueOf(2),
                        yearMonth = YEAR_MONTH,
                        note = "Note 5",
                        invoicedOn = null
                    )
                )
            )

            it("groups the same consumption and sums quantities") {
                actual.first shouldBe 1

                actual.second[0].childCode shouldBe 1
                actual.second[0].productId shouldBe "TST"
                actual.second[0].units shouldBe BigDecimal.valueOf(8)
                actual.second[0].yearMonth shouldBe YEAR_MONTH
                actual.second[0].note shouldBe "Note 1, Note 2, Note 3, Note 5"

                actual.second[1].childCode shouldBe 1
                actual.second[1].productId shouldBe "STS"
                actual.second[1].units shouldBe BigDecimal.valueOf(2)
                actual.second[1].yearMonth shouldBe YEAR_MONTH
                actual.second[1].note shouldBe "Note 4"
            }
        }
    }

}