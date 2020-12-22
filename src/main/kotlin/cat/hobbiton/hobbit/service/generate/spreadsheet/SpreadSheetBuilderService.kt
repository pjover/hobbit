package cat.hobbiton.hobbit.service.generate.spreadsheet

import cat.hobbiton.hobbit.util.resource.FileResource

interface SpreadSheetBuilderService {
    fun generate(spreadSheetCells: SpreadSheetCells): FileResource
}