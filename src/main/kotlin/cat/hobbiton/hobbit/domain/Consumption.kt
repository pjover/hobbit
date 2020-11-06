package cat.hobbiton.hobbit.domain

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

private const val SHORT_ID_LENGTH = 8

data class Consumption(
        @Id val id: String = RandomStringUtils.randomAlphanumeric(SHORT_ID_LENGTH),
        var childrenCode: Int,
        val productId: String,
        @Field(targetType = FieldType.DECIMAL128) val units: BigDecimal,
        var yearMonth: YearMonth = YearMonth.from(LocalDate.now()),
        val note: String? = null
)