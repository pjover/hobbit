package cat.hobbiton.hobbit.domain

import org.springframework.data.annotation.Id
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

data class Invoice(
        @Id val id: String,
        val customerId: Int,
        val date: LocalDate,
        var yearMonth: YearMonth = YearMonth.from(date),
        var childrenCodes: List<Int>,
        val lines: List<InvoiceLine>,
        var paymentType: PaymentType,
        var subsidizedAmount: BigDecimal = BigDecimal.ZERO,
        val note: String? = null,
        var emailed: Boolean = false,
        var printed: Boolean = false
)
