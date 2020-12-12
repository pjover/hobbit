package cat.hobbiton.hobbit.service.admin

import cat.hobbiton.hobbit.api.model.EntityTypeDTO

interface AdminService {

	fun modifyEntity(entity: EntityTypeDTO): Int
}
