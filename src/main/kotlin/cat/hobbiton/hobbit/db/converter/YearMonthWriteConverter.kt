package cat.hobbiton.hobbit.db.converter

import org.springframework.core.convert.converter.Converter
import java.time.YearMonth

class YearMonthWriteConverter : Converter<YearMonth, String> {

    override fun convert(source: YearMonth) = "${source.year}-${source.monthValue.toString().padStart(2, '0')}"
}
