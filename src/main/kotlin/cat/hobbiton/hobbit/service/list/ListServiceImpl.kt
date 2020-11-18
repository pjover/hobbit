package cat.hobbiton.hobbit.service.list

import cat.hobbiton.hobbit.api.model.*
import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.domain.extension.emailText
import cat.hobbiton.hobbit.domain.extension.getFirstAdult
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
                            group = group.text,
                            children = children
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

        return customerRepository.findAll()
                .filter { it.active }
                .sortedBy { it.id }
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

    override fun getEmailsList(group: GroupDTO): EmailsGroupDTO {
        return if (group == GroupDTO.ALL) getAllGroupsEmailsList()
        else getOneGroupEmailsList()
    }

    private fun getAllGroupsEmailsList(): EmailsGroupDTO {

        return EmailsGroupDTO(
                group = GroupDTO.ALL,
                emails = customerRepository.findAll()
                        .filter { it.active }
                        .sortedBy { it.id }
                        .map { it.invoiceHolder.emailText() }
        )
    }

    private fun getOneGroupEmailsList(): EmailsGroupDTO {
        TODO("Not yet implemented")
    }
}