package cat.hobbiton.hobbit.service.generate.bdd.string

import cat.hobbiton.hobbit.model.Invoice

interface InvoicesToBddMapper {
    fun map(invoices: List<Invoice>): Bdd
}