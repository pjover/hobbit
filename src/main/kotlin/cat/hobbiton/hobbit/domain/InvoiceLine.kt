package cat.hobbiton.hobbit.domain

import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.math.BigDecimal

data class InvoiceLine(
        val productId: String,
        val productName: String,
        @Field(targetType = FieldType.DECIMAL128) val units: BigDecimal,
        @Field(targetType = FieldType.DECIMAL128) val productPrice: BigDecimal,
        @Field(targetType = FieldType.DECIMAL128) val taxPercentage: BigDecimal = BigDecimal.ZERO
)
