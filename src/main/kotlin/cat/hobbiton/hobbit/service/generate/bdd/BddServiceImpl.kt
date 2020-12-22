package cat.hobbiton.hobbit.service.generate.bdd

import cat.hobbiton.hobbit.api.model.PaymentTypeDTO
import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.PaymentType
import cat.hobbiton.hobbit.model.extension.totalAmount
import cat.hobbiton.hobbit.service.aux.TimeService
import cat.hobbiton.hobbit.service.generate.getCustomerInvoicesDTOs
import cat.hobbiton.hobbit.util.resource.FileResource
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
        val invoices = getInvoices(yearMonth)
        return PaymentTypeInvoicesDTO(
            paymentType = PaymentTypeDTO.BANK_DIRECT_DEBIT,
            totalAmount = invoices.totalAmount(),
            customers = getCustomerInvoicesDTOs(invoices, customerRepository)
        )
    }

    private fun getInvoices(yearMonth: String?): List<Invoice> {
        val ym = if(yearMonth == null) timeService.currentYearMonth else YearMonth.parse(yearMonth)
        return invoiceRepository.findByPaymentTypeAndYearMonthAndSentToBank(
            PaymentType.BANK_DIRECT_DEBIT,
            ym,
            false)
    }


    override fun generateBDD(yearMonth: String): Resource {
        val invoices = getInvoices(yearMonth)
        val bdd = bddBuilderService.generate(invoices)
        updateInvoices(invoices)
        return FileResource(bdd.toByteArray(StandardCharsets.UTF_8), bbdFilename)
    }

    private fun updateInvoices(invoices: List<Invoice>) {
        invoiceRepository.saveAll(invoices.map { it.copy(sentToBank = true) })
    }
}