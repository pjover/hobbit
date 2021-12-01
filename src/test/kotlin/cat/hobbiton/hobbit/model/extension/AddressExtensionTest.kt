package cat.hobbiton.hobbit.model.extension

import cat.hobbiton.hobbit.testAddress
import io.kotest.core.spec.style.DescribeSpec
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
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }

                context("blank zip code") {
                    val executor = {
                        sut.copy(zipCode = "").validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }

                context("blank city") {
                    val executor = {
                        sut.copy(city = "").validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }

                context("blank state") {
                    val executor = {
                        sut.copy(state = "").validate()
                    }

                    it("throws an error") {
                        assertFailsWith<IllegalArgumentException> { executor.invoke() }
                    }
                }
            }
        }
    }
}
