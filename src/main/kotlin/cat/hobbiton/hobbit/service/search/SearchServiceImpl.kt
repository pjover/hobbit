package cat.hobbiton.hobbit.service.search

import cat.hobbiton.hobbit.api.model.ChildListDTO
import cat.hobbiton.hobbit.api.model.CustomerListDTO
import cat.hobbiton.hobbit.domain.Customer
import cat.hobbiton.hobbit.domain.extension.getFirstAdult
import cat.hobbiton.hobbit.domain.extension.shortName
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.data.mongodb.core.query.TextQuery
import org.springframework.stereotype.Service

@Service
class SearchServiceImpl(
        private val template: MongoTemplate
) : SearchService {

    override fun searchCustomer(text: String): List<CustomerListDTO> {

        val criteria = TextCriteria.forDefaultLanguage().matching(text)
        val query = TextQuery.queryText(criteria).sortByScore()
        val customers = template.find(query, Customer::class.java)

        return customers
                .map { customer ->
                    CustomerListDTO(
                            code = customer.id,
                            shortName = customer.getFirstAdult().shortName(),
                            children = customer.children
                                    .filter { it.active }
                                    .sortedBy { it.code }
                                    .map {
                                        ChildListDTO(
                                                code = it.code,
                                                shortName = it.shortName()
                                        )
                                    }
                    )
                }
    }
}