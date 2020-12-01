package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.extension.formattedText
import cat.hobbiton.hobbit.util.AppException
import org.springframework.stereotype.Service

@Service
class InvoiceServiceImpl(
    private val invoiceRepository: InvoiceRepository,
    private val sequenceService: SequenceService
) : InvoiceService {

    override fun saveInvoice(invoice: Invoice): Invoice {
        val sequence = sequenceService.increment(invoice.paymentType.sequenceType)
        return try {
            invoiceRepository.save(invoice.copy(id = sequence.formattedText()))
        } catch(t: Throwable) {
            sequenceService.decrement(invoice.paymentType.sequenceType)
            throw AppException(ErrorMessages.ERROR_SAVING_INVOICE, t.message ?: sequence.formattedText())
        }
    }
}