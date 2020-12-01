package cat.hobbiton.hobbit.api

import cat.hobbiton.hobbit.service.admin.AdminService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.validation.annotation.Validated
import org.springframework.beans.factory.annotation.Autowired

@RestController
@Validated
@RequestMapping("\${api.base-path:}")
class AdminController(@Autowired(required = true) val service: AdminService) {


    @RequestMapping(
        value = ["/admin/modifyEntity/{entity}"],
        produces = ["application/json"],
        method = [RequestMethod.POST])
    fun modifyEntity(@PathVariable("entity") entity: kotlin.String
    ): ResponseEntity<kotlin.Int> {
        return ResponseEntity(service.modifyEntity(entity), HttpStatus.valueOf(200))
    }
}
