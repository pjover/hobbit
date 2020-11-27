package cat.hobbiton.hobbit.service.list

import cat.hobbiton.hobbit.api.model.*
import cat.hobbiton.hobbit.db.repository.CustomerRepository
import cat.hobbiton.hobbit.model.extension.emailText
import cat.hobbiton.hobbit.model.extension.getFirstAdult
import cat.hobbiton.hobbit.model.extension.shortName
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
        else getOneGroupEmailsList(group)
    }

    private fun getAllGroupsEmailsList(): EmailsGroupDTO {

        return EmailsGroupDTO(
                group = GroupDTO.ALL,
                emails = customerRepository.findAll()
                        .filter { it.active }
                        .map { it.invoiceHolder.emailText() }
                        .sorted()
        )
    }

    private fun getOneGroupEmailsList(group: GroupDTO): EmailsGroupDTO {

        return EmailsGroupDTO(
                group = group,
                emails = customerRepository.findAll()
                        .filter { it.active }
                        .filter {
                            it.children.any { child -> child.group.name == group.name }
                        }
                        .map { it.invoiceHolder.emailText() }
                        .sorted()
        )
    }
}