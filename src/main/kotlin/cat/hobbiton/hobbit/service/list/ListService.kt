package cat.hobbiton.hobbit.service.list

import cat.hobbiton.hobbit.api.model.ChildrenGroupDTO
interface ListService {

	fun getChildrenList(): List<ChildrenGroupDTO>
}
