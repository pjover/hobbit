package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import org.springframework.core.io.Resource

interface SpreadsheetService {

    fun simulateMonthReport(yearMonth: String): List<PaymentTypeInvoicesDTO>

    fun generateMonthReport(yearMonth: String): Resource
}