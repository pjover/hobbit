package cat.hobbiton.hobbit.db.converter

import org.springframework.core.convert.converter.Converter
import java.time.YearMonth

class YearMonthReadConverter : Converter<String, YearMonth> {

    override fun convert(source: String): YearMonth = YearMonth.parse(source)
}
