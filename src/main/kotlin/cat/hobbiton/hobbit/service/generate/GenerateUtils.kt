package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.api.model.CustomerInvoicesDTO
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.extension.getFirstAdult
import cat.hobbiton.hobbit.model.extension.shortName
import cat.hobbiton.hobbit.model.extension.totalAmount
import cat.hobbiton.hobbit.service.billing.getInvoiceDto

fun getCustomerInvoicesDTOs(invoices: List<Invoice>, customerRepository: CachedCustomerRepository): List<CustomerInvoicesDTO> {
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