package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.testProduct1
import io.kotest.core.spec.style.DescribeSpec
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
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }

                context("and the id has less than 3 characters") {
                    val executor = {
                        testProduct1.copy(id = "TS").validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }

                context("and the id has more than 3 characters") {
                    val executor = {
                        testProduct1.copy(id = "TSTS").validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }

                context("and the id is lower case") {
                    val executor = {
                        testProduct1.copy(id = "tst").validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }
            }

            describe("Product name validation errors") {

                context("and the name is blank") {
                    val executor = {
                        testProduct1.copy(name = "").validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
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
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }
            }

            describe("Product short name validation errors") {

                context("and the shortName is blank") {
                    val executor = {
                        testProduct1.copy(shortName = "").validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
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
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }
            }

            describe("Price name validation errors") {

                context("and the price is zero") {
                    val executor = {
                        testProduct1.copy(price = BigDecimal.ZERO).validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }
            }

            describe("Tax percentage name validation errors") {

                context("and the tax percentage is negative") {
                    val executor = {
                        testProduct1.copy(taxPercentage = -1.toBigDecimal()).validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }
            }
        }
    }
}