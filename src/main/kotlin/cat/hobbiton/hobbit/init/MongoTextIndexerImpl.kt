package cat.hobbiton.hobbit.init

import cat.hobbiton.hobbit.model.Customer
import cat.hobbiton.hobbit.util.Logging
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.index.TextIndexDefinition.TextIndexDefinitionBuilder

class MongoTextIndexerImpl(
        private val template: MongoTemplate
) : MongoTextIndexer {

    private val logger by Logging()

    override fun createIndexes() {

        try {
            createIndex()
        } catch (e: Exception) {
            deleteIndexes()
            createIndex()
        }
    }

    private fun createIndex() {
        template.indexOps(Customer::class.java).ensureIndex(
                TextIndexDefinitionBuilder()
                        .named("HobbitTextIndex")
                        .onField("adults.name")
                        .onField("adults.surname")
                        .onField("adults.secondSurname")
                        .onField("adults.taxId")
                        .onField("adults.email")
                        .onField("adults.mobilePhone")
                        .onField("children.name")
                        .onField("children.surname")
                        .onField("children.secondSurname")
                        .onField("children.taxId")
                        .onField("invoiceHolder.name")
                        .onField("invoiceHolder.taxId")
                        .onField("invoiceHolder.email")
                        .withLanguageOverride("en")
                        .withDefaultLanguage("en")
                        .build()
        )
        logger.info("✅ Created text indexes on MongoDB")
    }

    private fun deleteIndexes() {
        logger.info("⚠️ Deleting previous indexes")
        template.indexOps(Customer::class.java).dropAllIndexes()
    }
}