package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
class SpreadsheetServiceImpl : SpreadsheetService {

    override fun simulateMonthReport(yearMonth: String): List<PaymentTypeInvoicesDTO> {
        TODO("Not yet implemented")
    }

    override fun generateMonthReport(yearMonth: String): Resource {
        TODO("Not yet implemented")
    }
}