package cat.hobbiton.hobbit.model

import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.math.BigDecimal

data class InvoiceLine(
    val productId: String,
    val productName: String? = null,
    @Field(targetType = FieldType.DECIMAL128) val units: BigDecimal,
    @Field(targetType = FieldType.DECIMAL128) val productPrice: BigDecimal,
    @Field(targetType = FieldType.DECIMAL128) val taxPercentage: BigDecimal = BigDecimal.ZERO,
    val childCode: Int?
)
