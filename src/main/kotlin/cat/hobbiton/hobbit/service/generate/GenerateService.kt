package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
interface GenerateService {

    fun generateBDD(yearMonth: String?): org.springframework.core.io.Resource

    fun simulateBDD(yearMonth: String?): PaymentTypeInvoicesDTO
}
