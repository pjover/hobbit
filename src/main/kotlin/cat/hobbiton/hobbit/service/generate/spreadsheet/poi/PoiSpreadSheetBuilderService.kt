package cat.hobbiton.hobbit.service.generate.spreadsheet.poi

import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.service.generate.spreadsheet.SpreadSheet
import cat.hobbiton.hobbit.service.generate.spreadsheet.SpreadSheetBuilderService
import cat.hobbiton.hobbit.util.error.AppException
import cat.hobbiton.hobbit.util.resource.FileResource
import org.springframework.stereotype.Service

@Service
class PoiSpreadSheetBuilderService : SpreadSheetBuilderService {

    override fun generate(spreadSheet: SpreadSheet): FileResource {
        validate(spreadSheet)
        return FileResource(ByteArray(33), spreadSheet.filename)
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
}