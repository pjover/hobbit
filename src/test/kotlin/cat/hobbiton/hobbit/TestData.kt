package cat.hobbiton.hobbit

import cat.hobbiton.hobbit.domain.Product
import java.math.BigDecimal

fun completeTestProduct() = Product("TST",
        "Test product",
        "Test",
        BigDecimal.valueOf(10.9)
)