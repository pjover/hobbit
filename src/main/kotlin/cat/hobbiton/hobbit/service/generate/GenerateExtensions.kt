package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.api.model.CustomerInvoicesDTO
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.CachedProductRepository
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.Product
import cat.hobbiton.hobbit.model.extension.getFirstAdult
import cat.hobbiton.hobbit.model.extension.shortName
import cat.hobbiton.hobbit.model.extension.totalAmount
import cat.hobbiton.hobbit.service.billing.getInvoiceDto
import cat.hobbiton.hobbit.util.i18n.translate

fun CachedCustomerRepository.getCustomerInvoicesDTOs(invoices: List<Invoice>): List<CustomerInvoicesDTO> {
    return invoices
        .groupBy { it.customerId }
        .map { (customerCode, customerInvoices) ->
            val customer = this.getCustomer(customerCode)
            CustomerInvoicesDTO(
                code = customerCode,
                shortName = customer.getFirstAdult().shortName(),
                totalAmount = customerInvoices.totalAmount(),
                invoices = customerInvoices.map { getInvoiceDto(customer, it) }
            )
        }
}

fun CachedCustomerRepository.getCustomersMap(invoices: List<Invoice>): Map<Int, Customer> {
    return invoices
        .map { it.customerId }
        .toSet()
        .map { it to this.getCustomer(it) }
        .toMap()
}

fun Map<Int, Customer>.getCustomer(id: Int) = this[id] ?: error(ErrorMessages.ERROR_CUSTOMER_NOT_FOUND.translate(id))

fun CachedProductRepository.getProductsMap(invoices: List<Invoice>): Map<String, Product> {
    return invoices
        .flatMap { it.lines }
        .map { it.productId }
        .toSet()
        .map { it to this.getProduct(it) }
        .toMap()
}

fun Map<String, Product>.getProduct(id: String) = this[id] ?: error(ErrorMessages.ERROR_PRODUCT_NOT_FOUND.translate(id))