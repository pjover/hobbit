package cat.hobbiton.hobbit.service.generate.spreadsheet.poi

import cat.hobbiton.hobbit.init.FormattingProperties
import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.service.generate.spreadsheet.*
import cat.hobbiton.hobbit.util.error.AppException
import cat.hobbiton.hobbit.util.resource.FileResource
import org.apache.poi.ss.util.CellReference
import org.apache.poi.xssf.usermodel.*
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

@Service
class PoiSpreadSheetBuilderService(
    private val formattingProperties: FormattingProperties
) : SpreadSheetBuilderService {

    private lateinit var workbook: XSSFWorkbook
    private lateinit var sheet: XSSFSheet
    private lateinit var dateStyle: XSSFCellStyle
    private lateinit var currencyStyle: XSSFCellStyle
    private lateinit var data: SpreadSheet

    override fun generate(spreadSheet: SpreadSheet): FileResource {
        init(spreadSheet)
        titleRow()
        dataRows()
        formatSheet()
        return getFileResource()
    }

    private fun init(spreadSheet: SpreadSheet) {
        validate(spreadSheet)
        this.data = spreadSheet
        this.workbook = XSSFWorkbook()
        this.sheet = workbook.createSheet(spreadSheet.title)
        this.dateStyle = loadStyle(formattingProperties.spreadSheetDateFormat)
        this.currencyStyle = loadStyle(formattingProperties.spreadSheetCurrencyFormat)
        initTable()
    }

    private fun validate(spreadSheet: SpreadSheet) {
        if(spreadSheet.filename.isEmpty()) throw AppException(ErrorMessages.ERROR_SPREAD_SHEET_BLANK_FILENAME)
        if(spreadSheet.title.isEmpty()) throw AppException(ErrorMessages.ERROR_SPREAD_SHEET_BLANK_TITLE)
        if(spreadSheet.headers.isEmpty()) throw AppException(ErrorMessages.ERROR_SPREAD_SHEET_NO_HEADERS)
        if(spreadSheet.lines.isEmpty()) throw AppException(ErrorMessages.ERROR_SPREAD_SHEET_NO_DATA)

        val maxCols = spreadSheet.lines.maxByOrNull { it.size }!!.size
        val minCols = spreadSheet.lines.minByOrNull { it.size }!!.size
        val columnCount = spreadSheet.headers.size
        if(maxCols != columnCount || minCols != columnCount) {
            throw AppException(ErrorMessages.ERROR_SPREAD_SHEET_NO_MATCH_COLUMN_NUMBER)
        }
    }

    private fun loadTableStyle(style: XSSFTableStyleInfo, styleName: String) {
        style.name = styleName
        style.isShowColumnStripes = false
        style.isShowRowStripes = true
        style.setFirstColumn(false)
        style.setLastColumn(false)
    }

    private fun loadStyle(format: String): XSSFCellStyle {
        val style = workbook.createCellStyle()
        style.dataFormat = workbook.creationHelper.createDataFormat().getFormat(format)
        return style
    }

    private fun initTable() {

        val table = sheet.createTable(workbook.creationHelper.createAreaReference(
            CellReference(0, 0),
            CellReference(dataLinesCount(), columnCount() - 1)))

        table.name = filenameWithoutExtension()
        table.displayName = table.name

        table.ctTable.addNewTableStyleInfo()
        table.ctTable.tableStyleInfo.name = "TableStyleMedium4" // TODO from configuration
        table.ctTable.addNewAutoFilter()

        loadTableStyle(table.style as XSSFTableStyleInfo, table.ctTable.tableStyleInfo.name)
    }

    private fun columnCount() = data.headers.size

    private fun dataLinesCount() = data.lines.size

    private fun filenameWithoutExtension() = File(data.filename).nameWithoutExtension

    private fun titleRow() {
        val row = sheet.createRow(0)
        for(i in 0 until columnCount()) {
            val cell = row.createCell(i)
            cell.setCellValue(data.headers[i])
        }
    }

    private fun dataRows() {
        for(i in data.lines.indices) dataRow(i + 1, data.lines[i])
    }

    private fun dataRow(rowNumber: Int, line: List<Cell>) {
        val row = sheet.createRow(rowNumber)
        for(i in line.indices) {
            dataCell(row, i, line[i])
        }
    }

    private fun dataCell(row: XSSFRow, colNumber: Int, dataCell: Cell) {
        val cell = row.createCell(colNumber)
        when(dataCell) {
            is TextCell -> textCell(cell, dataCell.cellValue)
            is DateCell -> dateCell(cell, dataCell.cellValue)
            is IntCell -> numberCell(cell, dataCell.cellValue)
            is DecimalCell -> numberCell(cell, dataCell.cellValue)
            is CurrencyCell -> currencyCell(cell, dataCell.cellValue)
        }
    }

    private fun textCell(cell: XSSFCell, cellValue: String) {
        cell.setCellValue(cellValue)
    }

    private fun dateCell(cell: XSSFCell, cellValue: Date) {
        cell.setCellValue(cellValue)
        cell.cellStyle = dateStyle
    }

    private fun numberCell(cell: XSSFCell, cellValue: Double) {
        cell.setCellValue(cellValue)
    }

    private fun currencyCell(cell: XSSFCell, cellValue: Double) {
        cell.setCellValue(cellValue)
        cell.cellStyle = currencyStyle
    }

    private fun formatSheet() {
        for(i in 0 until columnCount()) {
            sheet.autoSizeColumn(i)
        }
    }

    private fun getFileResource(): FileResource {
        ByteArrayOutputStream().use {
            workbook.write(it)
            workbook.close()
            return FileResource(it.toByteArray(), data.filename)
        }
    }
}
