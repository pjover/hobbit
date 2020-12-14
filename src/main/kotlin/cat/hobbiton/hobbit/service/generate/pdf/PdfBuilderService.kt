package cat.hobbiton.hobbit.service.generate.pdf

import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.Product
import org.springframework.core.io.Resource

interface PdfBuilderService {
    fun generate(invoice: Invoice, customer: Customer, products: Map<String, Product>): Resource
}