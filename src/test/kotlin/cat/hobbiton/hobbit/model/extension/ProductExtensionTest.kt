package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.testProduct1
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import java.math.BigDecimal
import kotlin.test.assertFailsWith

class ProductExtensionTest : DescribeSpec() {

    init {
        describe("validate") {

            context("Product id validation errors") {

                context("and the id is blank") {
                    val executor = {
                        testProduct1.copy(id = "").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Product id cannot be blank"
                    }
                }

                context("and the id has less than 3 characters") {
                    val executor = {
                        testProduct1.copy(id = "TS").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Product id must be 3 characters long"
                    }
                }

                context("and the id has more than 3 characters") {
                    val executor = {
                        testProduct1.copy(id = "TSTS").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Product id must be 3 characters long"
                    }
                }

                context("and the id is lower case") {
                    val executor = {
                        testProduct1.copy(id = "tst").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Product id must be in upper case"
                    }
                }
            }

            describe("Product name validation errors") {

                context("and the name is blank") {
                    val executor = {
                        testProduct1.copy(name = "").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Product name cannot be blank"
                    }
                }

                context("and the name is 150 characters long") {
                    testProduct1.copy(name = "12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                        "1234567890123456789012345678901234567890123456789012345678901234567890").validate()

                    it("do not throw any error") {
                    }
                }

                context("and the name is longer than 150 characters") {
                    val executor = {
                        testProduct1.copy(name = "12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                            "1234567890123456789012345678901234567890123456789012345678901234567890A").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Product name cannot exceed 150 chars length"
                    }
                }
            }

            describe("Product short name validation errors") {

                context("and the shortName is blank") {
                    val executor = {
                        testProduct1.copy(shortName = "").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Product short name cannot be blank"
                    }
                }

                context("and the shortName is12 characters long") {
                    testProduct1.copy(shortName = "123456789012").validate()

                    it("do not throw any error") {
                    }
                }

                context("and the shortName is longer than 12 characters") {
                    val executor = {
                        testProduct1.copy(shortName = "1234567890123").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Product short name cannot exceed 12 chars length"
                    }
                }
            }

            describe("Price name validation errors") {

                context("and the price is zero") {
                    val executor = {
                        testProduct1.copy(price = BigDecimal.ZERO).validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Product price cannot be zero"
                    }
                }
            }

            describe("Tax percentage name validation errors") {

                context("and the tax percentage is negative") {
                    val executor = {
                        testProduct1.copy(taxPercentage = -1.toBigDecimal()).validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Product tax percentage cannot be negative"
                    }
                }
            }
        }
    }
}