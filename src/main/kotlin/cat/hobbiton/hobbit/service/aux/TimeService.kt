package cat.hobbiton.hobbit.service.aux

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

interface TimeService {
    val currentLocalDateTime: LocalDateTime
    val currentLocalDate: LocalDate
    val currentYearMonth: YearMonth
    val lastMonth: YearMonth
}
