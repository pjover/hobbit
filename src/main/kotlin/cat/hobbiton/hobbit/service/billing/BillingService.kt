package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO

interface BillingService {

	fun getInvoices(): List<PaymentTypeInvoicesDTO>

	fun setInvoices(): List<PaymentTypeInvoicesDTO>
}
