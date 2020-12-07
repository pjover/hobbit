package cat.hobbiton.hobbit.service.generate.bdd

import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.service.aux.TimeService
import cat.hobbiton.hobbit.service.generate.bdd.string.InvoicesToBddMapper
import org.springframework.stereotype.Service

@Service
class BddServiceImpl(
    private val invoicesToBddMapper: InvoicesToBddMapper,
    private val builder: BddBuilder,
    private val timeService: TimeService
) : BddService {

    override fun generate(invoices: List<Invoice>): String {
        val bdd = invoicesToBddMapper.map(timeService.currentLocalDateTime, invoices)
        val s = builder.build(bdd!!)
        return s
    }
}