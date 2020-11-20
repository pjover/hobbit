package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.testAddress
import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import kotlin.test.assertFailsWith


class AddressExtensionTest : DescribeSpec() {

    init {

        val sut = testAddress()

        describe("validate") {

            context("valid") {

                sut.validate()

                it("should not throw any error") {}

            }

            context("not valid") {

                context("blank street") {
                    val executor = {
                        sut.copy(street = "").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Address street cannot be blank"
                    }
                }

                context("blank zip code") {
                    val executor = {
                        sut.copy(zipCode = "").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Address zip code cannot be blank"
                    }
                }

                context("blank city") {
                    val executor = {
                        sut.copy(city = "").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Address city cannot be blank"
                    }
                }

                context("blank state") {
                    val executor = {
                        sut.copy(state = "").validate()
                    }

                    it("throws an error") {
                        val exception = assertFailsWith<IllegalArgumentException> { executor.invoke() }
                        exception.message shouldBe "Address state cannot be blank"
                    }
                }
            }
        }
    }
}
