package cat.hobbiton.hobbit.service.list

import cat.hobbiton.hobbit.api.model.ChildListDTO
import cat.hobbiton.hobbit.api.model.ChildrenGroupDTO
import cat.hobbiton.hobbit.api.model.CustomerListDTO
import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.domain.extension.shortName
import org.springframework.stereotype.Service

@Service
class ListServiceImpl(
        private val customerRepository: CustomerRepository
) : ListService {

    override fun getChildrenList(): List<ChildrenGroupDTO> {

        return customerRepository.findAll()
                .filter { it.active }
                .flatMap { it.children }
                .filter { it.active }
                .groupBy { it.group }
                .toSortedMap()
                .map { (group, children) ->
                    ChildrenGroupDTO(
                            group.text,
                            children
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

    override fun getCustomersList(): List<CustomerListDTO> {
        TODO("Not yet implemented")
    }
}