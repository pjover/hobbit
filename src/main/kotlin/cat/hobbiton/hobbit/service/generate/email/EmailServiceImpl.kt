package cat.hobbiton.hobbit.service.generate.email

import cat.hobbiton.hobbit.api.model.PaymentTypeDTO
import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.db.repository.CachedCustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.extension.totalAmount
import cat.hobbiton.hobbit.service.generate.getCustomerInvoicesDTOs
import cat.hobbiton.hobbit.util.error.NotFoundException
import org.springframework.stereotype.Service
import java.time.YearMonth

@Service
class EmailServiceImpl(
    private val invoiceRepository: InvoiceRepository,
    private val customerRepository: CachedCustomerRepository,
    private val emailSenderService: EmailSenderService
) : EmailService {

    override fun simulateEmails(yearMonth: String): PaymentTypeInvoicesDTO {
        return getPaymentTypeInvoicesDTO(getInvoices(yearMonth))
    }

    private fun getInvoices(yearMonth: String): List<Invoice> {
        val invoices = invoiceRepository.findByEmailedAndYearMonth(emailed = false, yearMonth = YearMonth.parse(yearMonth))
        if(invoices.isEmpty()) throw NotFoundException(ErrorMessages.ERROR_EMAILS_TO_SEND_NOT_FOUND)
        return invoices
    }

    private fun getPaymentTypeInvoicesDTO(invoices: List<Invoice>): PaymentTypeInvoicesDTO {
        return PaymentTypeInvoicesDTO(
            paymentType = PaymentTypeDTO.BANK_DIRECT_DEBIT,
            totalAmount = invoices.totalAmount(),
            customers = getCustomerInvoicesDTOs(invoices, customerRepository)
        )
    }

    override fun generateEmails(yearMonth: String): PaymentTypeInvoicesDTO {
        val invoices = getInvoices(yearMonth)
        val batchCodes = invoices
            .map {
                val customer = customerRepository.getCustomer(it.customerId)
                emailSenderService.enqueue(invoice = it, customer)
            }
        emailSenderService.send(batchCodes)
        updateInvoices(invoices)
        return getPaymentTypeInvoicesDTO(invoices)
    }

    private fun updateInvoices(invoices: List<Invoice>) {
        invoiceRepository.saveAll(invoices.map { it.copy(printed = true) })
    }
}