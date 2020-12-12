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
import cat.hobbiton.hobbit.service.aux.TimeService
import cat.hobbiton.hobbit.service.billing.getInvoiceDto
import cat.hobbiton.hobbit.service.generate.bdd.BddService
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.time.YearMonth

@Service
class GenerateServiceImpl(
    private val bddService: BddService,
    private val invoiceRepository: InvoiceRepository,
    private val customerRepository: CachedCustomerRepository,
    private val timeService: TimeService
) : GenerateService {

    override fun generateBDD(yearMonth: String?): Resource {
        val invoices = getInvoices(yearMonth)
        val bdd = bddService.generate(invoices)
        updateInvoices(invoices)
        return InputStreamResource(bdd.byteInputStream(StandardCharsets.UTF_8))
    }

    private fun getInvoices(yearMonth: String?): List<Invoice> {
        val ym = if(yearMonth == null) timeService.currentYearMonth else YearMonth.parse(yearMonth)
        return invoiceRepository.findByPaymentTypeAndYearMonthAndSentToBank(
            PaymentType.BANK_DIRECT_DEBIT,
            ym,
            false)
    }

    private fun updateInvoices(invoices: List<Invoice>) {
        invoiceRepository.saveAll(invoices.map { it.copy(sentToBank = true) })
    }

    override fun simulateBDD(yearMonth: String?): PaymentTypeInvoicesDTO {
        val invoices = getInvoices(yearMonth)
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