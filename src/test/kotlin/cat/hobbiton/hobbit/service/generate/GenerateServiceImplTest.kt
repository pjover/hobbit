package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.api.model.PaymentTypeDTO
import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import io.kotlintest.specs.DescribeSpec
import io.kotlintest.shouldBe
import io.mockk.mockk

class GenerateServiceImplTest : DescribeSpec() {

    init {
        val invoiceRepository = mockk<InvoiceRepository>()
        val sut = GenerateServiceImpl(invoiceRepository)

        describe("generateSepa") { }

        describe("simulateSepa") {

            val actual = sut.simulateSepa()

            it("should be the pending invoices for BANK_DIRECT_DEBIT") {

                actual shouldBe PaymentTypeInvoicesDTO(
                    PaymentTypeDTO.BANK_DIRECT_DEBIT,
                    0.0,
                    emptyList()
                )
            }

        }
    }

}