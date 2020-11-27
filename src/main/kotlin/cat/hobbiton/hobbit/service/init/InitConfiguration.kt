package cat.hobbiton.hobbit.service.init

import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.db.repository.InvoiceRepository
import cat.hobbiton.hobbit.db.repository.SequenceRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
class InitConfiguration {

    @Bean(initMethod = "checkSequences")
    fun sequenceChecker(
            customerRepository: CustomerRepository,
            invoiceRepository: InvoiceRepository,
            sequenceRepository: SequenceRepository): SequenceChecker {

        return SequenceCheckerImpl(customerRepository, invoiceRepository, sequenceRepository)
    }

    @Bean(initMethod = "createIndexes")
    fun mongoTextIndexer(template: MongoTemplate): MongoTextIndexer {

        return MongoTextIndexerImpl(template)
    }
}