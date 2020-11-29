package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.completeTestProduct
import cat.hobbiton.hobbit.model.extension.validate
import cat.hobbiton.hobbit.util.AppException
import io.kotlintest.specs.DescribeSpec
import io.kotlintest.shouldBe
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
                every { productRepository.findById(any()) } returns Optional.of(completeTestProduct())

                val actual = sut.getProduct("TST")

                it("returns the product") {
                    actual shouldBe completeTestProduct()
                }

                it("calls the repository once") {
                    verify(exactly = 1) {
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
                    val exception = assertFailsWith<AppException> { executor.invoke() }
                    exception.message shouldBe "Cannot find product with id TST"
                }
            }
        }
    }
}