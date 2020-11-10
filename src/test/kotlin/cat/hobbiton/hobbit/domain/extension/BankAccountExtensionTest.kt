package cat.hobbiton.hobbit.domain.extension

import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec


class BankAccountExtensionTest : DescribeSpec() {

    init {

        describe("isValid") {

            context("valid AccountCode") {

                it("should return true") {
                    "ES28 3066 8859 9782 5852 9057".isValidBankAccount() shouldBe true
                    "ES02 3001 2859 0880 2660 6142".isValidBankAccount() shouldBe true
                    "ES8731795040719243310258".isValidBankAccount() shouldBe true
                    "ES60-3118-2176-0723-9984-7410".isValidBankAccount() shouldBe true
                }
            }

            context("invalid AccountCode") {

                context("that is empty") {
                    it("should return false") {
                        "".isValidBankAccount() shouldBe false
                    }
                }

                context("that has the wrong length") {
                    it("should return false") {
                        "ES873179504071924331025".isValidBankAccount() shouldBe false
                    }
                }

                context("that has no country") {
                    it("should return false") {
                        "3460-3118-2176-0723-9984-7410".isValidBankAccount() shouldBe false
                    }
                }

                context("that incorrect digits") {

                    it("should return false") {
                        "ES50-3118-2176-0723-9984-7410".isValidBankAccount() shouldBe false
                        "ES61-3118-2176-0723-9984-7410".isValidBankAccount() shouldBe false
                        "ES60-3118-2176-1723-9984-7410".isValidBankAccount() shouldBe false
                        "ES60-3118-2176-0623-9984-7410".isValidBankAccount() shouldBe false
                    }
                }
            }
        }
    }
}
