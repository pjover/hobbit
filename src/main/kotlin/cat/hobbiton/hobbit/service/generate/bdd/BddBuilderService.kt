package cat.hobbiton.hobbit.service.generate.bdd

import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.Product

interface BddBuilderService {
    fun generate(invoices: List<Invoice>, customers: Map<Int, Customer>, products: Map<String, Product>): String
}
