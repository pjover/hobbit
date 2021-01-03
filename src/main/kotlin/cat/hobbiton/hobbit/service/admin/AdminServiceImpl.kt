package cat.hobbiton.hobbit.service.admin

import cat.hobbiton.hobbit.api.model.EntityTypeDTO
import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.db.repository.ProductRepository
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.Product
import cat.hobbiton.hobbit.util.Logging
import org.springframework.stereotype.Service

@Service
class AdminServiceImpl(
    private val customerRepository: CustomerRepository,
    private val invoiceRepository: InvoiceRepository,
    private val productRepository: ProductRepository
) : AdminService {

    private val logger by Logging()

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
        val customers = customerRepository.findAll()
        val updatedCustomers = customers.map { updateCustomer(it) }
        customerRepository.saveAll(updatedCustomers)
        logger.warn("✅ updated ${updatedCustomers.size} customers ‼️️")
        return updatedCustomers.size
    }

    private fun updateCustomer(customer: Customer): Customer {
        logger.info("Updating customer $customer")
        val updatedCustomer = customer.copy()
        logger.info("Updated customer $updatedCustomer")
        return updatedCustomer
    }

    private fun modifyInvoice(): Int {
        val invoices = invoiceRepository.findAll()
        val updatedInvoices = invoices.map { updateInvoice(it) }
        invoiceRepository.saveAll(updatedInvoices)
        logger.warn("✅ updated ${updatedInvoices.size} invoices ‼️️")
        return updatedInvoices.size
    }

    private fun updateInvoice(invoice: Invoice): Invoice {
        logger.info("Updating invoice $invoice")
        val updatedInvoice = invoice.copy()
        logger.info("Updated invoice $updatedInvoice")
        return updatedInvoice
    }

    private fun modifyProduct(): Int {
        val products = productRepository.findAll()
        val updatedProducts = products.map { updateProduct(it) }
        productRepository.saveAll(updatedProducts)
        logger.warn("✅ updated ${updatedProducts.size} products ‼️️")
        return updatedProducts.size
    }

    private fun updateProduct(product: Product): Product {
        logger.info("Updating product $product")
        val updatedProduct = product.copy()
        logger.info("Updated product $updatedProduct")
        return updatedProduct
    }

    private fun modifySequence(): Int {
        return 0
    }
}