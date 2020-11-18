package cat.hobbiton.hobbit.api.controller

import cat.hobbiton.hobbit.api.model.ChildrenGroupDTO
import cat.hobbiton.hobbit.api.model.CustomerListDTO
import cat.hobbiton.hobbit.api.model.EmailsGroupDTO
import cat.hobbiton.hobbit.api.model.GroupDTO
import cat.hobbiton.hobbit.service.list.ListService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
@RequestMapping("\${api.base-path:}")
class ListController(@Autowired(required = true) val service: ListService) {


    @RequestMapping(
            value = ["/lists/children"],
            produces = ["application/json"],
            method = [RequestMethod.GET])
    fun getChildrenList(): ResponseEntity<List<ChildrenGroupDTO>> {
        return ResponseEntity(service.getChildrenList(), HttpStatus.valueOf(200))
    }


    @RequestMapping(
            value = ["/lists/customers"],
            produces = ["application/json"],
            method = [RequestMethod.GET])
    fun getCustomersList(): ResponseEntity<List<CustomerListDTO>> {
        return ResponseEntity(service.getCustomersList(), HttpStatus.valueOf(200))
    }


    @RequestMapping(
            value = ["/lists/emails/{group}"],
            produces = ["application/json"],
            method = [RequestMethod.GET])
    fun getEmailsList(@PathVariable("group") group: GroupDTO
    ): ResponseEntity<EmailsGroupDTO> {
        return ResponseEntity(service.getEmailsList(group), HttpStatus.valueOf(200))
    }
}
