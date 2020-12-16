package cat.hobbiton.hobbit.service.generate.email

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl : EmailService {
    override fun simulateEmails(yearMonth: String): PaymentTypeInvoicesDTO {
        TODO("Not yet implemented")
    }

    override fun generateEmails(yearMonth: String): PaymentTypeInvoicesDTO {
        TODO("Not yet implemented")
    }
}