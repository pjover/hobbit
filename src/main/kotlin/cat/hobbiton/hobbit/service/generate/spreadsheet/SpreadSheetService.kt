package cat.hobbiton.hobbit.service.generate.spreadsheet

import org.springframework.core.io.Resource

interface SpreadSheetService {

    fun generateMonthSpreadSheet(yearMonth: String): Resource

    fun generateYearSpreadSheet(year: Int): Resource

    fun generateCustomersSpreadSheet(): Resource
}