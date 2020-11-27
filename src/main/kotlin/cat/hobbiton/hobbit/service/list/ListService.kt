package cat.hobbiton.hobbit.service.list

import cat.hobbiton.hobbit.api.model.ChildrenGroupDTO
import cat.hobbiton.hobbit.api.model.CustomerListDTO
import cat.hobbiton.hobbit.api.model.EmailsGroupDTO
import cat.hobbiton.hobbit.api.model.GroupDTO

interface ListService {

	fun getChildrenList(): List<ChildrenGroupDTO>

	fun getCustomersList(): List<CustomerListDTO>

	fun getEmailsList(group: GroupDTO): EmailsGroupDTO
}
