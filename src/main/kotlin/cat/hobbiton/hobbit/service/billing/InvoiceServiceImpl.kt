package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.model.Consumption
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.Sequence
import cat.hobbiton.hobbit.model.SequenceType
import cat.hobbiton.hobbit.model.extension.formattedText
import cat.hobbiton.hobbit.util.error.AppException
import org.springframework.stereotype.Service

@Service
class InvoiceServiceImpl(
    private val invoiceRepository: InvoiceRepository,
    private val sequenceService: SequenceService,
    private val consumptionRepository: ConsumptionRepository
) : InvoiceService {

    override fun saveInvoice(invoice: Invoice, consumptions: List<Consumption>): Invoice {
        var updatedSequence: Sequence? = null
        var updatedConsumptions: List<Consumption>? = null
        try {
            updatedSequence = sequenceService.increment(invoice.paymentType.sequenceType)
            updatedConsumptions = consumptionRepository.saveAll(consumptions.map { it.copy(invoiceId = updatedSequence.formattedText()) })
            return invoiceRepository.save(invoice.copy(id = updatedSequence.formattedText()))
        } catch(t: Throwable) {
            restore(updatedConsumptions, updatedSequence, invoice.paymentType.sequenceType, consumptions)
            throw AppException(t, ErrorMessages.ERROR_SAVING_INVOICE, invoice.paymentType.sequenceType)
        }
    }

    private fun restore(updatedConsumptions: List<Consumption>?, updatedSequence: Sequence?,
                        sequenceType: SequenceType, consumptions: List<Consumption>) {
        if(updatedSequence != null) sequenceService.decrement(sequenceType)
        if(updatedConsumptions != null) consumptionRepository.saveAll(consumptions)
    }
}