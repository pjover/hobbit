package cat.hobbiton.hobbit.service.generate.pdf

import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.Product
import cat.hobbiton.hobbit.util.resource.FileResource

interface PdfBuilderService {
    fun generate(invoice: Invoice, customer: Customer, products: Map<String, Product>): FileResource
}