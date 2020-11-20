package cat.hobbiton.hobbit.db.repository

import cat.hobbiton.hobbit.model.Consumption
import org.springframework.data.mongodb.repository.MongoRepository

interface ConsumptionRepository : MongoRepository<Consumption, String>
