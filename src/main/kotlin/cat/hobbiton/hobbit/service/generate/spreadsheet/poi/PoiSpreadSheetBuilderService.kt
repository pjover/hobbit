package cat.hobbiton.hobbit.service.generate.spreadsheet.poi

import cat.hobbiton.hobbit.messages.ErrorMessages
import cat.hobbiton.hobbit.service.generate.spreadsheet.SpreadSheetBuilderService
import cat.hobbiton.hobbit.service.generate.spreadsheet.SpreadSheetCells
import cat.hobbiton.hobbit.service.generate.spreadsheet.monthSpreadSheetFilename
import cat.hobbiton.hobbit.util.error.AppException
import cat.hobbiton.hobbit.util.resource.FileResource
import org.springframework.stereotype.Service

@Service
class PoiSpreadSheetBuilderService : SpreadSheetBuilderService {

    override fun generate(data: SpreadSheetCells): FileResource {
        validate(data)
        return FileResource(ByteArray(33), monthSpreadSheetFilename)
    }

    private fun validate(data: SpreadSheetCells) {
        if(data.headers.isEmpty()) throw AppException(ErrorMessages.ERROR_SPREAD_SHEET_NO_HEADERS)
        if(data.lines.isEmpty()) throw AppException(ErrorMessages.ERROR_SPREAD_SHEET_NO_DATA)

        val maxCols = data.lines.maxByOrNull { it.size }!!.size
        val minCols = data.lines.minByOrNull { it.size }!!.size
        val columnCount = data.headers.size
        if(maxCols != columnCount || minCols != columnCount) {
            throw AppException(ErrorMessages.ERROR_SPREAD_SHEET_NO_MATCH_COLUMN_NUMBER)
        }
    }
}