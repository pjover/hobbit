package cat.hobbiton.hobbit.service.generate.bdd

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import org.springframework.core.io.Resource

interface BddService {

	fun simulateBDD(yearMonth: String?): PaymentTypeInvoicesDTO

	fun generateBDD(yearMonth: String): Resource
}
