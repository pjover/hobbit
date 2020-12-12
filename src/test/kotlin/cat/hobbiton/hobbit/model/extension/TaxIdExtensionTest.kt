package cat.hobbiton.hobbit.model.extension

import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec


class TaxIdExtensionTest : DescribeSpec() {

    init {
        describe("CIF") {
            context("a correct CIF") {
                it("should return true") {
                    "J8518436D".isValidTaxId() shouldBe true
                    "F9873859D".isValidTaxId() shouldBe true
                    "H69159929".isValidTaxId() shouldBe true
                    "N4585129B".isValidTaxId() shouldBe true
                    "S2910252B".isValidTaxId() shouldBe true
                    "R2305254A".isValidTaxId() shouldBe true
                    "A58818501".isValidTaxId() shouldBe true
                    "Z4614782K".isValidTaxId() shouldBe true
                    "Q8453568A".isValidTaxId() shouldBe true
                }
            }

            context("an incorrect CIF") {
                it("should return false") {
                    "N8486733K".isValidTaxId() shouldBe false
                    "J8528436D".isValidTaxId() shouldBe false
                    "F9883859D".isValidTaxId() shouldBe false
                    "H69169929".isValidTaxId() shouldBe false
                    "N4586129B".isValidTaxId() shouldBe false
                    "S2919252B".isValidTaxId() shouldBe false
                    "R2306254A".isValidTaxId() shouldBe false
                    "A58838501".isValidTaxId() shouldBe false
                    "Y58838501".isValidTaxId() shouldBe false
                }
            }
        }

        describe("NIE") {
            context("a correct NIE") {
                it("should return true") {
                    "X2010159M".isValidTaxId() shouldBe true
                }
            }

            context("an incorrect NIE") {
                it("should return false") {
                    "X2010159K".isValidTaxId() shouldBe false
                    "Y2010159K".isValidTaxId() shouldBe false
                    "Z2010159K".isValidTaxId() shouldBe false
                    "Z2010159".isValidTaxId() shouldBe false
                }
            }
        }

        describe("NIF") {
            context("a correct NIF") {
                it("should return true") {
                    "48592145E".isValidTaxId() shouldBe true
                }
            }

            context("an incorrect NIF") {
                it("should return false") {
                    "48592145K".isValidTaxId() shouldBe false
                }
            }
        }

        describe("getSepaIndentifier()") {

            it("should format the IBAN code") {
                "36361882D".getSepaIndentifier("ES", "000") shouldBe "ES4200036361882D"
                "37866397W".getSepaIndentifier("ES", "000") shouldBe "ES5500037866397W"
            }
        }

        describe("calculateControlCode()") {

            it("should calculate the Control Code") {
                calculateControlCode("3066 8859 9782 5852 9057ES") shouldBe "28"
                calculateControlCode("3001 2859 0880 2660 6142ES") shouldBe "02"
                calculateControlCode("31795040719243310258ES") shouldBe "87"
                calculateControlCode("3118-2176-0723-9984-7410ES") shouldBe "60"
                calculateControlCode("HOBB", "20180707204308000") shouldBe "24"
            }
        }
    }
}
