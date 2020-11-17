package cat.hobbiton.hobbit.service.list

import cat.hobbiton.hobbit.api.model.ChildListDTO
import cat.hobbiton.hobbit.api.model.ChildrenListDTO
import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.domain.extension.shortName
import org.springframework.stereotype.Service

@Service
class ListServiceImpl(
        private val customerRepository: CustomerRepository
) : ListService {

    override fun getChildrenList(): ChildrenListDTO {

        return ChildrenListDTO(
                customerRepository.findAll()
                        .flatMap { it.children }
                        .map { ChildListDTO(code = it.code, shortName = it.shortName()) }
        )
    }
}