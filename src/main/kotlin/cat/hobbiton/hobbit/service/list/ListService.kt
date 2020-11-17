package cat.hobbiton.hobbit.service.list

import cat.hobbiton.hobbit.api.model.ChildrenListDTO

interface ListService {

    fun getChildrenList(): ChildrenListDTO
}
