package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.api.model.PaymentTypeDTO
import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
class GenerateServiceImpl(
    private val invoiceRepository: InvoiceRepository
) : GenerateService {

    override fun generateSepa(): Resource {
        TODO("Not yet implemented")
    }

    override fun simulateSepa(): PaymentTypeInvoicesDTO {

        return PaymentTypeInvoicesDTO(
            PaymentTypeDTO.BANK_DIRECT_DEBIT,
            0.0,
            emptyList()
        )
    }
}