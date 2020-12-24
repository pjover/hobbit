package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import org.springframework.core.io.Resource

interface SpreadSheetService {

    fun simulateMonthSpreadSheet(yearMonth: String): List<PaymentTypeInvoicesDTO>

    fun generateMonthSpreadSheet(yearMonth: String): Resource
}