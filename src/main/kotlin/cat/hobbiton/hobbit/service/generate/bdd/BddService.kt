package cat.hobbiton.hobbit.service.generate.bdd

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
interface BddService {

	fun generateBDD(yearMonth: kotlin.String?): org.springframework.core.io.Resource

	fun simulateBDD(yearMonth: kotlin.String?): PaymentTypeInvoicesDTO
}
