package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.model.PaymentType
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.YearMonth

interface InvoiceRepository : MongoRepository<Invoice, String> {

    fun findByYearMonth(yearMonth: YearMonth): List<Invoice>

    fun findByYearMonthYear(year: Int): List<Invoice>

    fun findByPrintedAndYearMonth(printed: Boolean, yearMonth: YearMonth): List<Invoice>

    fun findByEmailedAndYearMonth(emailed: Boolean, yearMonth: YearMonth): List<Invoice>

    fun findByPaymentTypeAndYearMonthAndSentToBank(paymentType: PaymentType, yearMonth: YearMonth, sentToBank: Boolean): List<Invoice>

    fun findByIdStartingWith(idPrefix: String): List<Invoice>
}
