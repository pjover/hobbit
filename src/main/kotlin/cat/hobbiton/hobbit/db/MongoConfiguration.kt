package cat.hobbiton.hobbit.db

import cat.hobbiton.hobbit.db.converter.YearMonthReadConverter
import cat.hobbiton.hobbit.db.converter.YearMonthWriteConverter
import cat.hobbiton.hobbit.util.Logging
import com.mongodb.MongoClientSettings
import com.mongodb.ServerAddress
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import javax.annotation.PostConstruct

@Configuration
class MongoConfiguration(
    @Value("\${db.name}") private val dbName: String,
    @Value("\${db.host}") private val dbHost: String,
    @Value("\${db.port}") private val dbPort: Int
) : AbstractMongoClientConfiguration() {

    private val logger by Logging()

    @PostConstruct
    private fun postConstruct() {
        logger.info("Working with '$dbHost:$dbPort' MongoDB server on database '$dbName'")
    }

    override fun getDatabaseName() = dbName

    override fun configureClientSettings(builder: MongoClientSettings.Builder) {
        builder
            .applyToClusterSettings { settings ->
                settings.hosts(listOf(ServerAddress(dbHost, dbPort)))
            }
    }

    override fun configureConverters(adapter: MongoCustomConversions.MongoConverterConfigurationAdapter) {
        adapter.registerConverter(YearMonthReadConverter())
        adapter.registerConverter(YearMonthWriteConverter())
    }
}
