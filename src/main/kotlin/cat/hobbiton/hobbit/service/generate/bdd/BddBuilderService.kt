package cat.hobbiton.hobbit.service.generate.bdd

import cat.hobbiton.hobbit.model.Invoice

interface BddBuilderService {
    fun generate(invoices: List<Invoice>): String
}
