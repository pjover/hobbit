package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.api.model.CustomerInvoicesDTO
import cat.hobbiton.hobbit.api.model.InvoiceDTO
import cat.hobbiton.hobbit.api.model.PaymentTypeDTO
import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.CachedProductRepository
import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.model.Consumption
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.InvoiceLine
import cat.hobbiton.hobbit.model.extension.getFirstAdult
import cat.hobbiton.hobbit.model.extension.shortName
import cat.hobbiton.hobbit.service.aux.TimeService
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.YearMonth

@Service
class BillingServiceImpl(
    private val consumptionRepository: ConsumptionRepository,
    private val customerRepository: CachedCustomerRepository,
    private val productRepository: CachedProductRepository,
    private val timeService: TimeService,
    private val invoiceService: InvoiceService
) : BillingService {

    override fun getInvoices(): List<PaymentTypeInvoicesDTO> {
        return invoices(false)
    }

    fun invoices(save: Boolean): List<PaymentTypeInvoicesDTO> {
        val consumptions = consumptionRepository.findByInvoiceIdNull()
        return consumptions
            .map { Pair(customerRepository.getCustomerByChildCode(it.childCode), it) }
            .groupBy { it.first.invoiceHolder.paymentType }
            .map { (paymentType, consumptions) ->
                PaymentTypeInvoicesDTO(
                    paymentType = PaymentTypeDTO.valueOf(paymentType.name),
                    totalAmount = getTotalAmount(consumptions.map { it.second }),
                    customers = getCustomerInvoices(save, consumptions)
                )
            }
    }

    private fun getTotalAmount(consumptions: List<Consumption>): Double {
        return consumptions
            .map { getTotalAmount(it) }
            .sumOf { it }
            .toDouble()
    }

    private fun getTotalAmount(consumption: Consumption): BigDecimal {
        val product = productRepository.getProduct(consumption.productId)
        return product.price.multiply(consumption.units)
    }

    private fun getCustomerInvoices(save: Boolean, consumptions: List<Pair<Customer, Consumption>>): List<CustomerInvoicesDTO> {
        return consumptions
            .groupBy { it.first.id }
            .map { (customerCode, consumptionPairs) ->
                val customer = consumptionPairs.first().first
                val customerConsumptions = consumptionPairs.map { it.second }
                CustomerInvoicesDTO(
                    code = customerCode,
                    shortName = customer.getFirstAdult().shortName(),
                    totalAmount = getTotalAmount(customerConsumptions),
                    invoices = getInvoiceDtos(save, customer, customerConsumptions)
                )
            }
    }

    private fun getInvoiceDtos(save: Boolean, customer: Customer, consumptions: List<Consumption>): List<InvoiceDTO> {
        return consumptions
            .groupBy { it.yearMonth }
            .map { getInvoice(save, customer, it.key, it.value) }
            .map { getInvoiceDto(customer, it) }
    }

    private fun getInvoice(save: Boolean, customer: Customer, yearMonth: YearMonth, consumptions: List<Consumption>): Invoice {

        val invoice = Invoice(
            id = "??",
            customerId = customer.id,
            date = timeService.currentLocalDate,
            yearMonth = yearMonth,
            childrenCodes = consumptions.map { it.childCode }.distinct(),
            paymentType = customer.invoiceHolder.paymentType,
            subsidizedAmount = customer.invoiceHolder.subsidizedAmount,
            note = getNotes(consumptions),
            lines = getInvoiceLines(consumptions)
        )
        return if(save) invoiceService.saveInvoice(invoice, consumptions)
        else invoice
    }

    private fun getNotes(consumptions: List<Consumption>): String? {
        val n = consumptions.mapNotNull { it.note }
        return if(n.isEmpty()) null
        else n.joinToString(", ")
    }

    private fun getInvoiceLines(consumptions: List<Consumption>): List<InvoiceLine> {
        return consumptions
            .groupBy { it.childCode }
            .map { (childCode, consumptions) ->
                groupConsumptions(childCode, consumptions)
            }
            .map { getChildInvoiceLines(it.second) }
            .flatten()
    }

    private fun getChildInvoiceLines(consumptions: List<Consumption>): List<InvoiceLine> {
        return consumptions.map { getInvoiceLine(it) }
    }

    private fun getInvoiceLine(consumption: Consumption): InvoiceLine {
        val product = productRepository.getProduct(consumption.productId)
        return InvoiceLine(
            productId = consumption.productId,
            units = consumption.units,
            productPrice = product.price,
            productName = product.name,
            taxPercentage = product.taxPercentage,
            childCode = consumption.childCode
        )
    }

    override fun setInvoices(): List<PaymentTypeInvoicesDTO> {
        return invoices(true)
    }
}