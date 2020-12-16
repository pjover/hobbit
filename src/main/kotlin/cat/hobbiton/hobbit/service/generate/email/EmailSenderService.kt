package cat.hobbiton.hobbit.service.generate.email

import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice

interface EmailSenderService {
    fun enqueue(invoice: Invoice, customer: Customer): String
    fun send(batchCode: String)
}