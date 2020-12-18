package cat.hobbiton.hobbit.init

import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.db.repository.SequenceRepository
import cat.hobbiton.hobbit.model.Sequence
import cat.hobbiton.hobbit.model.SequenceType
import cat.hobbiton.hobbit.model.extension.extractCounter
import cat.hobbiton.hobbit.util.Logging
import org.springframework.stereotype.Service

@Service
class SequenceCheckerImpl(
        private val customerRepository: CustomerRepository,
        private val invoiceRepository: InvoiceRepository,
        private val sequenceRepository: SequenceRepository
) : SequenceChecker {

    private val logger by Logging()

    override fun checkSequences() {

        val map = sequenceRepository.findAll().map { it.id to it }.toMap()
        checkCustomerSequence(map[SequenceType.CUSTOMER])
        checkSequence(map[SequenceType.STANDARD_INVOICE], SequenceType.STANDARD_INVOICE)
        checkSequence(map[SequenceType.SPECIAL_INVOICE], SequenceType.SPECIAL_INVOICE)
        checkSequence(map[SequenceType.RECTIFICATION_INVOICE], SequenceType.RECTIFICATION_INVOICE)
    }

    private fun checkCustomerSequence(sequence: Sequence?) {

        val customers = customerRepository.findAll()
        val lastCustomer =
                if (customers.isEmpty()) 1
                else customers.maxByOrNull { it.id }!!.id

        if (sequence == null) {
            val createdSequence = sequenceRepository.save(Sequence(SequenceType.CUSTOMER, lastCustomer))
            logger.info("ℹ️ Created sequence $createdSequence")
            return
        }

        if (lastCustomer != sequence.counter) {
            sequenceRepository.save(Sequence(SequenceType.CUSTOMER, lastCustomer))
            logger.warn("⚠️ Updated desyncronized sequence $sequence to $lastCustomer")
        }

        logger.info("✅ Checked sequence $sequence")
    }

    private fun checkSequence(sequence: Sequence?, sequenceType: SequenceType) {

        val invoices = invoiceRepository.findByIdStartingWith(sequenceType.prefix)

        if (invoices.isEmpty()) {
            val createdSequence = sequenceRepository.save(Sequence(sequenceType, 1))
            logger.info("ℹ️ Created sequence $createdSequence")
            return
        }

        val lastInvoice = invoices.map { it.id.extractCounter() }.maxOf { it }

        if (lastInvoice != sequence!!.counter) {
            sequenceRepository.save(Sequence(sequenceType, lastInvoice))
            logger.warn("⚠️ Updated desyncronized sequence $sequence to $lastInvoice")
        }

        logger.info("✅ Checked sequence $sequence")
    }
}
