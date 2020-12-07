package cat.hobbiton.hobbit.service.generate.bdd.string

data class Bdd(
    val messageIdentification: String? = null,
    val creationDateTime: String? = null,
    val numberOfTransactions: Int? = null,
    val controlSum: String? = null,
    val name: String? = null,
    val identification: String? = null,
    val requestedCollectionDate: String? = null,
    val country: String? = null,
    val addressLine1: String? = null,
    val addressLine2: String? = null,
    val iban: String? = null,
    val bic: String? = null,
    val details: List<BddDetail>? = null
)
