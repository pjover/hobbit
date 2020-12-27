package cat.hobbiton.hobbit.model

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
    var subsidizedAmount: BigDecimal? = null,
    val note: String? = null,
    var emailed: Boolean = false,
    var printed: Boolean = false,
    var sentToBank: Boolean = false
)
