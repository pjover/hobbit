package cat.hobbiton.hobbit.model

import org.springframework.data.annotation.Id

data class Customer(
        @Id val id: Int = 0,
        val children: List<Child>,
        val adults: List<Adult>,
        val invoiceHolder: InvoiceHolder,
        val note: String? = null,
        val language: Language = Language.CA,
        val active: Boolean = true
)
