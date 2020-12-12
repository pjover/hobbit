package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.model.Consumption
import cat.hobbiton.hobbit.model.Invoice

interface InvoiceService {
    fun saveInvoice(invoice: Invoice, consumptions: List<Consumption>): Invoice
}