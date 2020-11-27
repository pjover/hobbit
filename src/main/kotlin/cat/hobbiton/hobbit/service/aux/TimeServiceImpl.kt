package cat.hobbiton.hobbit.service.aux

import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

@Service
class TimeServiceImpl : TimeService {

    override val currentLocalDateTime: LocalDateTime
        get() = LocalDateTime.now()

    override val currentLocalDate: LocalDate
        get() = LocalDate.now()

    override val currentYearMonth: YearMonth
        get() = YearMonth.now()
}
