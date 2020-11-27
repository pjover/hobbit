package cat.hobbiton.hobbit.service.search

import cat.hobbiton.hobbit.api.model.CustomerListDTO

interface SearchService {

	fun searchCustomer(text: String): List<CustomerListDTO>
}
