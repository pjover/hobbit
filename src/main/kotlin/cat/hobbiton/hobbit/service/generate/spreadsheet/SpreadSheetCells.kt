package cat.hobbiton.hobbit.service.generate.spreadsheet

import java.math.BigDecimal
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

data class SpreadSheetCells(val title: String, val headers: List<String>, val lines: List<List<Cell>>)

interface Cell {
    val content: Any
    val cellValue: Any
}

data class TextCell(override val content: String) : Cell {
    override val cellValue = content
}

data class DateCell(override val content: LocalDate) : Cell {
    override val cellValue: Date = Date.from(content.atStartOfDay(ZoneId.systemDefault()).toInstant())
}

data class IntCell(override val content: Int) : Cell {
    override val cellValue = content.toDouble()
}

data class DecimalCell(override val content: BigDecimal) : Cell {
    override val cellValue = content.toDouble()
}

data class CurrencyCell(override val content: BigDecimal) : Cell {
    override val cellValue = content.toDouble()
}
