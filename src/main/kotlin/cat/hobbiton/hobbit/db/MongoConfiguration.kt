package cat.hobbiton.hobbit.db

import cat.hobbiton.hobbit.util.Logging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import javax.annotation.PostConstruct

@Configuration
class MongoConfiguration(
        @Value("\${db.name}") private val dbName: String
) : AbstractMongoClientConfiguration() {

    private val logger by Logging()

    @PostConstruct
    private fun postConstruct() {
        logger.info("Working with MongoDB database '$dbName'")
    }

    override fun getDatabaseName() = dbName
}
