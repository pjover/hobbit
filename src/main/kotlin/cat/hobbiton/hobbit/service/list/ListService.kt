package cat.hobbiton.hobbit.service.list

import cat.hobbiton.hobbit.api.model.ChildrenGroupDTO
import cat.hobbiton.hobbit.api.model.CustomerListDTO
import cat.hobbiton.hobbit.api.model.EmailsGroupDTO
interface ListService {

	fun getChildrenList(): List<ChildrenGroupDTO>

	fun getCustomersList(): List<CustomerListDTO>

	fun getEmailsList(group: String): List<EmailsGroupDTO>
}
