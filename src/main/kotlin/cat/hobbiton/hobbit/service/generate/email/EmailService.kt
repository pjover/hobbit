package cat.hobbiton.hobbit.service.generate.email

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO

interface EmailService {

    fun simulateEmails(yearMonth: String): PaymentTypeInvoicesDTO

    fun generateEmails(yearMonth: String): PaymentTypeInvoicesDTO
}