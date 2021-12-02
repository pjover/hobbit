package cat.hobbiton.hobbit.db.converter

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.YearMonth

class YearMonthReadConverterTest : DescribeSpec() {

    init {
        val sut = YearMonthReadConverter()

        describe("convert") {
            val actual = sut.convert("1999-01")

            it("should convert the string to the right month") {
                actual shouldBe YearMonth.of(1999, 1)
            }
        }
    }
}
