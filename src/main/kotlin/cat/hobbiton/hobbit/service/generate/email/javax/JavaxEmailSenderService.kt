package cat.hobbiton.hobbit.service.generate.email.javax

import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.service.generate.email.EmailSenderService
import org.springframework.stereotype.Service

@Service
class JavaxEmailSenderService : EmailSenderService {

    override fun enqueue(invoice: Invoice, customer: Customer): String {
        TODO("Not yet implemented")
    }

    override fun send(batchCodes: List<String>) {
        TODO("Not yet implemented")
    }
}