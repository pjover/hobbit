package cat.hobbiton.hobbit.service.generate.bdd.string

import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.Product

interface InvoicesToBddMapper {
    fun map(invoices: List<Invoice>, customers: Map<Int, Customer>, products: Map<String, Product>): Bdd
}