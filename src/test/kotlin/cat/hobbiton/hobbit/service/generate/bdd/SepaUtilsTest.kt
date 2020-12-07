package cat.hobbiton.hobbit.service.generate.bdd

import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec

class SepaUtilsTest : DescribeSpec() {

    init {
        val sut = SepaUtils()

        describe("IBAN") {
            context("isValidIban()") {

                it("returns true with valid IBAN") {
                    sut.isValidIban("ES28 3066 8859 9782 5852 9057") shouldBe true
                    sut.isValidIban("ES02 3001 2859 0880 2660 6142") shouldBe true
                    sut.isValidIban("ES8731795040719243310258") shouldBe true
                    sut.isValidIban("ES60-3118-2176-0723-9984-7410") shouldBe true
                }

                it("returns false with invalid IBAN") {
                    sut.isValidIban(null) shouldBe false
                    sut.isValidIban("") shouldBe false
                    sut.isValidIban("ES873179504071924331025") shouldBe false
                    sut.isValidIban("3460-3118-2176-0723-9984-7410") shouldBe false
                    sut.isValidIban("ES50-3118-2176-0723-9984-7410") shouldBe false
                    sut.isValidIban("ES61-3118-2176-0723-9984-7410") shouldBe false
                    sut.isValidIban("ES60-3118-2176-0623-9984-7410") shouldBe false
                }
            }
        }

        describe("formatCode()") {

            it("should format the IBAN code") {
                sut.formatCode(null) shouldBe ""
                sut.formatCode("") shouldBe ""
                sut.formatCode(" ") shouldBe ""
                sut.formatCode("ES28 3066 8859 9782 5852 9057") shouldBe "ES2830668859978258529057"
                sut.formatCode("ES02 3001 2859 0880 2660 6142") shouldBe "ES0230012859088026606142"
                sut.formatCode("ES8731795040719243310258") shouldBe "ES8731795040719243310258"
                sut.formatCode("ES60-3118-2176-0723-9984-7410") shouldBe "ES6031182176072399847410"
            }
        }

        describe("getSepaIndentifier()") {

            it("should format the IBAN code") {
                sut.getSepaIndentifier("ES", "000", "37866397W") shouldBe sut.getSepaIndentifier("37866397W")
                sut.getSepaIndentifier("ES", "000", "37866397W") shouldBe "ES5500037866397W"
            }
        }

        describe("calculateControlCode()") {

            it("should calculate the Control Code") {
                sut.calculateControlCode("3066 8859 9782 5852 9057ES") shouldBe "28"
                sut.calculateControlCode("3001 2859 0880 2660 6142ES") shouldBe "02"
                sut.calculateControlCode("31795040719243310258ES") shouldBe "87"
                sut.calculateControlCode("3118-2176-0723-9984-7410ES") shouldBe "60"
            }
        }
    }
}
