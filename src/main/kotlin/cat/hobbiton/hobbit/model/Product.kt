package cat.hobbiton.hobbit.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.math.BigDecimal

data class Product(
        @Id val id: String,
        val name: String,
        val shortName: String,
        @Field(targetType = FieldType.DECIMAL128) val price: BigDecimal,
        @Field(targetType = FieldType.DECIMAL128) val taxPercentage: BigDecimal = BigDecimal.ZERO
)