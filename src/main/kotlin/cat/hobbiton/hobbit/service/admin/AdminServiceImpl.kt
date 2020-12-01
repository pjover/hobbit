package cat.hobbiton.hobbit.service.admin

import cat.hobbiton.hobbit.db.repository.*
import cat.hobbiton.hobbit.model.Invoice
import org.springframework.stereotype.Service

@Service
class AdminServiceImpl(
    private val invoiceRepository: InvoiceRepository,
) : AdminService {

    override fun modifyEntity(entity: String): Int {
        return when(entity) {
            "Consumption" -> modifyConsumption()
            "Customer" -> modifyCustomer()
            "Invoice" -> modifyInvoice()
            "Product" -> modifyProduct()
            "Sequence" -> modifySequence()
            else -> 0
        }
    }

    private fun modifyConsumption(): Int {
        return 0
    }

    private fun modifyCustomer(): Int {
        return 0
    }

    private fun modifyInvoice(): Int {
        return 0
    }

    private fun modifyProduct(): Int {
        return 0
    }

    private fun modifySequence(): Int {
        return 0
    }
}