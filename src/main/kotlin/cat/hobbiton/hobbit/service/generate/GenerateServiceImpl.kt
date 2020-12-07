package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.api.model.CustomerInvoicesDTO
import cat.hobbiton.hobbit.api.model.PaymentTypeDTO
import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.PaymentType
import cat.hobbiton.hobbit.model.extension.getFirstAdult
import cat.hobbiton.hobbit.model.extension.shortName
import cat.hobbiton.hobbit.model.extension.totalAmount
import cat.hobbiton.hobbit.service.billing.getInvoiceDto
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
class GenerateServiceImpl(
    private val invoiceRepository: InvoiceRepository,
    private val customerRepository: CachedCustomerRepository
) : GenerateService {

    override fun generateSepa(): Resource {
        TODO("Not yet implemented")
    }

    override fun simulateSepa(): PaymentTypeInvoicesDTO {
        val invoices = invoiceRepository.findByPaymentTypeAndSentToBank(PaymentType.BANK_DIRECT_DEBIT, false)
        return PaymentTypeInvoicesDTO(
            PaymentTypeDTO.BANK_DIRECT_DEBIT,
            invoices.totalAmount().toDouble(),
            getCustomerInvoicesDTOs(invoices)
        )
    }

    private fun getCustomerInvoicesDTOs(invoices: List<Invoice>): List<CustomerInvoicesDTO> {
        return invoices
            .groupBy { it.customerId }
            .map { (customerCode, customerInvoices) ->
                val customer = customerRepository.getCustomer(customerCode)
                CustomerInvoicesDTO(
                    code = customerCode,
                    shortName = customer.getFirstAdult().shortName(),
                    totalAmount = customerInvoices.totalAmount().toDouble(),
                    invoices = customerInvoices.map { getInvoiceDto(customer, it) }
                )
            }
    }
}