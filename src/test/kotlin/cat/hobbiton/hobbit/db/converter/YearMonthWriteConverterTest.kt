package cat.hobbiton.hobbit.db.converter

import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.time.YearMonth

class YearMonthWriteConverterTest : DescribeSpec() {

    init {
        val sut = YearMonthWriteConverter()

        describe("convert") {

            context("with month lesser than 10") {
                val actual = sut.convert(YearMonth.of(1999, 1))

                it("adds zeros to month if is lesser than 10") {
                    actual shouldBe "1999-01"
                }
            }

            context("with month greater than 9") {
                val actual = sut.convert(YearMonth.of(1999, 10))

                it("adds zeros to month if is lesser than 10") {
                    actual shouldBe "1999-10"
                }
            }
        }
    }
}
