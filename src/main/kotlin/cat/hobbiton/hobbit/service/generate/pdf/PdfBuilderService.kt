package cat.hobbiton.hobbit.service.generate.pdf

import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import org.springframework.core.io.Resource

interface PdfBuilderService {
    fun generate(invoice: Invoice, customer: Customer): Resource
}