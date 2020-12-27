package cat.hobbiton.hobbit.service.generate.bdd.string


import cat.hobbiton.hobbit.init.BusinessProperties
import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.Product
import cat.hobbiton.hobbit.model.extension.calculateControlCode
import cat.hobbiton.hobbit.model.extension.getSepaIndentifier
import cat.hobbiton.hobbit.model.extension.totalAmount
import cat.hobbiton.hobbit.service.aux.TimeService
import cat.hobbiton.hobbit.service.generate.getCustomer
import cat.hobbiton.hobbit.service.generate.getProduct
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Component
class InvoicesToBddMapperImpl(
    private val businessProperties: BusinessProperties,
    private val timeService: TimeService
) : InvoicesToBddMapper {

    companion object {
        private val messageIdentificationFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
        private val creationDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        private val numberFormatter = NumberFormat.getNumberInstance(Locale.ENGLISH)
        private val decimalFormatter = DecimalFormat("#.00")

        init {
            numberFormatter.isGroupingUsed = false
        }
    }

    private val addressLine1: String
        get() = businessProperties.addressLine1

    private val addressLine2: String
        get() = businessProperties.addressLine2

    override fun map(invoices: List<Invoice>, customers: Map<Int, Customer>, products: Map<String, Product>): Bdd {
        val dateTime = timeService.currentLocalDateTime
        return Bdd(
            messageIdentification = getMessageIdentification(dateTime = dateTime),
            creationDateTime = getCreationDateTime(dateTime),
            numberOfTransactions = getNumberOfTransactions(invoices),
            controlSum = getControlSum(invoices),
            name = businessProperties.businessName,
            identification = businessProperties.bddBusinessId,
            requestedCollectionDate = getRequestedCollectionDate(dateTime),
            country = businessProperties.bddCountry,
            addressLine1 = addressLine1,
            addressLine2 = addressLine2,
            iban = businessProperties.bddBusinessIban,
            bic = businessProperties.bddBankBic,
            details = getDetails(dateTime, invoices, customers, products))
    }

    private fun getMessageIdentification(dateTime: LocalDateTime): String {
        val datetime = messageIdentificationFormatter.format(dateTime)
        val suffix = calculateControlCode(businessProperties.bddBusinessPrefix, datetime)
        return "${businessProperties.bddBusinessPrefix}-$datetime-$suffix"
    }

    private fun getCreationDateTime(dateTime: LocalDateTime): String {
        return creationDateTimeFormatter.format(dateTime)
    }

    private fun getNumberOfTransactions(invoices: List<Invoice>): Int {
        return invoices.size
    }

    private fun getControlSum(invoices: List<Invoice>): String {
        var controlSum = BigDecimal.ZERO
        for(invoice in invoices) {
            controlSum = controlSum.add(invoice.totalAmount())
        }
        return decimalFormatter.format(controlSum)
    }

    private fun getRequestedCollectionDate(dateTime: LocalDateTime): String {
        return dateFormatter.format(dateTime)
    }

    private fun getDetails(dateTime: LocalDateTime, invoices: List<Invoice>, customers: Map<Int, Customer>, products: Map<String, Product>): List<BddDetail> {
        return invoices.map { mapInvoiceToDetail(dateTime, it, customers, products) }.toList()
    }

    private fun mapInvoiceToDetail(dateTime: LocalDateTime, invoice: Invoice, customers: Map<Int, Customer>, products: Map<String, Product>): BddDetail {
        val customer = customers.getCustomer(invoice.customerId)
        return BddDetail(endToEndIdentifier = getDetailEndToEndIdentifier(dateTime, invoice),
            instructedAmount = getDetailInstructedAmount(invoice),
            dateOfSignature = getDetailDateOfSignature(dateTime),
            name = getDetailName(customer),
            identification = getDetailIdentification(customer),
            iban = getDetailCustomerBankAccount(customer)!!,
            purposeCode = businessProperties.bddPurposeCode,
            remittanceInformation = getDetailRemittanceInformation(invoice, products),
            isBusiness = getDetailIsBusiness(customer))
    }

    private fun getDetailEndToEndIdentifier(dateTime: LocalDateTime, invoice: Invoice): String {
        return "${getMessageIdentification(dateTime)}.${invoice.id}"
    }

    private fun getDetailInstructedAmount(invoice: Invoice): String {
        return decimalFormatter.format(invoice.totalAmount())
    }

    private fun getDetailDateOfSignature(dateTime: LocalDateTime): String {
        return dateFormatter.format(dateTime)
    }

    private fun getDetailName(customer: Customer): String {
        return customer.invoiceHolder.name
    }

    private fun getDetailIdentification(customer: Customer): String {
        return customer.invoiceHolder.taxId.getSepaIndentifier(businessProperties.bddCountry, "000")
    }

    private fun getDetailCustomerBankAccount(customer: Customer): String? {
        return customer.invoiceHolder.bankAccount
    }

    private fun getDetailRemittanceInformation(invoice: Invoice, products: Map<String, Product>): String {
        return getInvoiceDescription(invoice, products)
    }

    private fun getDetailIsBusiness(customer: Customer): Boolean {
        return customer.invoiceHolder.isBusiness
    }

    private fun getInvoiceDescription(invoice: Invoice, products: Map<String, Product>): String {
        val maxLength = 140
        val invoiceDescription = getShortNameInvoiceDescription(invoice, products)
        return if(invoiceDescription.length > maxLength) {
            StringUtils.abbreviate(invoiceDescription, maxLength)
        } else {
            invoiceDescription
        }
    }

    private fun getShortNameInvoiceDescription(invoice: Invoice, products: Map<String, Product>): String {
        return invoice.lines.joinToString(", ") {
            numberFormatter.format(it.units) + "x" + products.getProduct(it.productId).shortName
        }
    }
}
