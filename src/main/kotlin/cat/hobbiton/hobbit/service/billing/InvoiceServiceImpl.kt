package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.db.repository.ConsumptionRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.model.Consumption
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.extension.formattedText
import cat.hobbiton.hobbit.util.AppException
import org.springframework.stereotype.Service

@Service
class InvoiceServiceImpl(
    private val invoiceRepository: InvoiceRepository,
    private val sequenceService: SequenceService,
    private val consumptionRepository: ConsumptionRepository
) : InvoiceService {

    override fun saveInvoice(invoice: Invoice, consumptions: List<Consumption>): Invoice {
        try {
            return save(invoice, consumptions)
        } catch(t: Throwable) {
            unSave(invoice, consumptions)
            throw AppException(ErrorMessages.ERROR_SAVING_INVOICE, invoice.paymentType.sequenceType)
        }
    }

    private fun save(invoice: Invoice, consumptions: List<Consumption>): Invoice {
        val sequence = sequenceService.increment(invoice.paymentType.sequenceType)
        val updatedInvoice = invoice.copy(id = sequence.formattedText())
        val updatedConsumptions = consumptions.map { it.copy(invoiceId = updatedInvoice.id) }
        consumptionRepository.saveAll(updatedConsumptions)
        return invoiceRepository.save(invoice)
    }

    private fun unSave(invoice: Invoice, consumptions: List<Consumption>) {
        sequenceService.decrement(invoice.paymentType.sequenceType)
        consumptionRepository.saveAll(consumptions)
        invoiceRepository.save(invoice)
    }
}