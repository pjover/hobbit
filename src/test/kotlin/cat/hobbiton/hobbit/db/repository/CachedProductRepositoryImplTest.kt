package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.testProduct1
import cat.hobbiton.hobbit.util.error.NotFoundException
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.*
import kotlin.test.assertFailsWith

class CachedProductRepositoryImplTest : DescribeSpec() {

    init {
        val productRepository = mockk<ProductRepository>()
        val sut = CachedProductRepositoryImpl(productRepository)

        describe("getProduct") {
            context("existing product") {
                every { productRepository.findById(any()) } returns Optional.of(testProduct1)

                val actual = sut.getProduct("TST")

                it("returns the product") {
                    actual shouldBe testProduct1
                }

                it("calls the repository once") {
                    verify {
                        productRepository.findById("TST")
                    }
                }
            }

            context("non existing product") {
                every { productRepository.findById(any()) } returns Optional.empty()

                val executor = {
                    sut.getProduct("TST")
                }

                it("throws an error") {
                    val exception = assertFailsWith<NotFoundException> { executor.invoke() }
                    exception.errorMessage shouldBe ErrorMessages.ERROR_PRODUCT_NOT_FOUND
                }
            }
        }
    }
}