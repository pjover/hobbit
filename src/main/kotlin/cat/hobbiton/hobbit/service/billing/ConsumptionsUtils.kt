package cat.hobbiton.hobbit.service.billing

import cat.hobbiton.hobbit.model.Consumption

fun groupConsumptions(childCode: Int, consumptions: List<Consumption>): Pair<Int, List<Consumption>> {
    return Pair(
        childCode,
        consumptions
            .groupBy { it.productId }
            .map { (productId, it) ->
                Consumption(
                    childCode = childCode,
                    productId = productId,
                    units = it.sumOf { it.units },
                    yearMonth = it.first().yearMonth,
                    note = it.map { it.note }.joinToString(separator = ", ")
                )
            }
    )
}