package cat.hobbiton.hobbit.domain.aux

import cat.hobbiton.hobbit.domain.Product
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
                        Product(
                                "",
                                "Test",
                                "Test product",
                                BigDecimal.TEN
                        ).validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Product id cannot be blank"
                    }
                }

                context("and the id has less than 3 characters") {
                    val executor = {
                        Product(
                                "AA",
                                "Test",
                                "Test product",
                                BigDecimal.TEN
                        ).validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Product id must be 3 characters long"
                    }
                }

                context("and the id has more than 3 characters") {
                    val executor = {
                        Product(
                                "AAAA",
                                "Test",
                                "Test product",
                                BigDecimal.TEN
                        ).validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Product id must be 3 characters long"
                    }
                }

                context("and the id is lower case") {
                    val executor = {
                        Product(
                                "aaa",
                                "Test",
                                "Test product",
                                BigDecimal.TEN
                        ).validate()
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
                        Product(
                                "AAA",
                                "",
                                "Test product",
                                BigDecimal.TEN
                        ).validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Product name cannot be blank"
                    }
                }

                context("and the name is longer than 150 characters") {
                    val executor = {
                        Product(
                                "AAA",
                                "12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                                        "1234567890123456789012345678901234567890123456789012345678901234567890A",
                                "Test product",
                                BigDecimal.TEN
                        ).validate()
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
                        Product(
                                "AAA",
                                "Test product",
                                "",
                                BigDecimal.TEN
                        ).validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Product short name cannot be blank"
                    }
                }

                context("and the shortName is longer than 12 characters") {
                    val executor = {
                        Product(
                                "AAA",
                                "Test product",
                                "1234567890123",
                                BigDecimal.TEN
                        ).validate()
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
                        Product(
                                "AAA",
                                "Test product",
                                "Test product",
                                BigDecimal.ZERO
                        ).validate()
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
                        Product(
                                "AAA",
                                "Test product",
                                "Test product",
                                BigDecimal.TEN,
                                BigDecimal.valueOf(-1)
                        ).validate()
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