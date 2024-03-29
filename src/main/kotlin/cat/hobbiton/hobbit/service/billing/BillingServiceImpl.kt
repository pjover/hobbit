package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.api.model.CustomerInvoicesDTO
import cat.hobbiton.hobbit.api.model.InvoiceDTO
import cat.hobbiton.hobbit.api.model.PaymentTypeDTO
import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.CachedProductRepository
import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.model.*
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
        val consumptions = consumptionRepository.findByInvoiceId()
        val normalConsumptions = consumptions.filter { !it.isRectification }
        val rectificationConsumptions = consumptions.filter { it.isRectification }
        return normalInvoices(save, normalConsumptions) + rectificationInvoices(save, rectificationConsumptions)
    }

    fun normalInvoices(save: Boolean, consumptions: List<Consumption>): List<PaymentTypeInvoicesDTO> {
        return consumptions
            .map { Pair(customerRepository.getCustomerByChildCode(it.childCode), it) }
            .groupBy { it.first.invoiceHolder.paymentType }
            .map { (paymentType, consumptions) ->
                val customers = getCustomerInvoices(save, consumptions)
                PaymentTypeInvoicesDTO(
                    paymentType = PaymentTypeDTO.valueOf(paymentType.name),
                    totalAmount = getTotalAmount(consumptions.map { it.second }),
                    numberOfInvoices = customers.flatMap { it.invoices }.count(),
                    customers = customers
                )
            }
    }

    fun rectificationInvoices(save: Boolean, consumptions: List<Consumption>): List<PaymentTypeInvoicesDTO> {
        if(consumptions.isEmpty()) return emptyList()
        val consumptionsWhithUsers = consumptions
            .map { Pair(customerRepository.getCustomerByChildCode(it.childCode), it) }
        val customers = getCustomerInvoices(save, consumptionsWhithUsers)
        return listOf(
            PaymentTypeInvoicesDTO(
                paymentType = PaymentTypeDTO.RECTIFICATION,
                totalAmount = getTotalAmount(consumptions),
                numberOfInvoices = customers.flatMap { it.invoices }.count(),
                customers = customers
            )
        )
    }

    private fun getTotalAmount(consumptions: List<Consumption>): BigDecimal {
        return consumptions
            .map { getTotalAmount(it) }
            .sumOf { it }
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

        val paymentType = if(consumptions.first().isRectification) PaymentType.RECTIFICATION else customer.invoiceHolder.paymentType

        val invoice = Invoice(
            id = "??",
            customerId = customer.id,
            date = timeService.currentLocalDate,
            yearMonth = yearMonth,
            childrenCodes = consumptions.map { it.childCode }.distinct(),
            paymentType = paymentType,
            note = getNotes(consumptions),
            lines = getInvoiceLines(consumptions)
        )
        return if(save) invoiceService.saveInvoice(invoice, consumptions)
        else invoice
    }

    private fun getNotes(consumptions: List<Consumption>): String? {
        val n = consumptions.mapNotNull { it.note }.filter{ it.isNotBlank() }
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
        return consumptions
            .map { getInvoiceLine(it) }
            .filter { it.units != BigDecimal.ZERO }
    }

    private fun getInvoiceLine(consumption: Consumption): InvoiceLine {
        val product = productRepository.getProduct(consumption.productId)
        return InvoiceLine(
            productId = consumption.productId,
            units = consumption.units,
            productPrice = product.price,
            taxPercentage = product.taxPercentage,
            childCode = consumption.childCode
        )
    }

    override fun setInvoices(): List<PaymentTypeInvoicesDTO> {
        return invoices(true)
    }
}