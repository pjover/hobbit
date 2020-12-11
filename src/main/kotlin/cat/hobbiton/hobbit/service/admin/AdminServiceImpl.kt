package cat.hobbiton.hobbit.service.admin

import cat.hobbiton.hobbit.api.model.EntityTypeDTO
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import org.springframework.stereotype.Service

@Service
class AdminServiceImpl(
    private val invoiceRepository: InvoiceRepository,
) : AdminService {

    override fun modifyEntity(entity: EntityTypeDTO): Int {
        return when(entity) {
            EntityTypeDTO.Consumption -> modifyConsumption()
            EntityTypeDTO.Customer -> modifyCustomer()
            EntityTypeDTO.Invoice -> modifyInvoice()
            EntityTypeDTO.Product -> modifyProduct()
            EntityTypeDTO.Sequence -> modifySequence()
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