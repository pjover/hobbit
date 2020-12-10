package cat.hobbiton.hobbit.service.generate.bdd.string

data class BddDetail(
    val endToEndIdentifier: String,
    val instructedAmount: String,
    val dateOfSignature: String,
    val name: String,
    val identification: String,
    val iban: String,
    val purposeCode: String,
    val remittanceInformation: String,
    val isBusiness: Boolean)
