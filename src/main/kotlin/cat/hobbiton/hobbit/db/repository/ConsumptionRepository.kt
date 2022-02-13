package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.model.Consumption
import org.springframework.data.mongodb.repository.MongoRepository

interface ConsumptionRepository : MongoRepository<Consumption, String> {
    fun findByInvoiceId(invoiceId: String = "NONE"): List<Consumption>
    fun findByChildCodeAndInvoiceId(childCode: Int, invoiceId: String = "NONE"): List<Consumption>
}
