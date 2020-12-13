package cat.hobbiton.hobbit.service.generate.bdd

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
import cat.hobbiton.hobbit.service.aux.TimeService
import cat.hobbiton.hobbit.service.billing.getInvoiceDto
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.YearMonth

@Service
class BddServiceImpl(
    private val bddBuilderService: BddBuilderService,
    private val invoiceRepository: InvoiceRepository,
    private val customerRepository: CachedCustomerRepository,
    private val timeService: TimeService
) : BddService {

    override fun simulateBDD(yearMonth: String?): PaymentTypeInvoicesDTO {
        val invoices = getBddInvoices(yearMonth)
        return PaymentTypeInvoicesDTO(
            PaymentTypeDTO.BANK_DIRECT_DEBIT,
            invoices.totalAmount().toDouble(),
            getCustomerInvoicesDTOs(invoices)
        )
    }

    private fun getBddInvoices(yearMonth: String?): List<Invoice> {
        val ym = if(yearMonth == null) timeService.currentYearMonth else YearMonth.parse(yearMonth)
        return invoiceRepository.findByPaymentTypeAndYearMonthAndSentToBank(
            PaymentType.BANK_DIRECT_DEBIT,
            ym,
            false)
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

    override fun generateBDD(yearMonth: String): Resource {
        val invoices = getBddInvoices(yearMonth)
        val bdd = bddBuilderService.generate(invoices)
        updateBddInvoices(invoices)
        return InputStreamResource(bdd.byteInputStream(StandardCharsets.UTF_8))
    }

    private fun updateBddInvoices(invoices: List<Invoice>) {
        invoiceRepository.saveAll(invoices.map { it.copy(sentToBank = true) })
    }
}