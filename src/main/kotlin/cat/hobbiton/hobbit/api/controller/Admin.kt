package cat.hobbiton.hobbit.api.controller

import cat.hobbiton.hobbit.api.model.EntityTypeDTO
import cat.hobbiton.hobbit.service.admin.AdminService
import io.swagger.v3.oas.annotations.Operation
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
class AdminController(@Autowired(required = true) val service: AdminService) {


    @RequestMapping(
        value = ["/admin/modifyEntity/{entity}"],
        produces = ["application/json"],
        method = [RequestMethod.POST])
    fun modifyEntity(@PathVariable("entity") entity: EntityTypeDTO
    ): ResponseEntity<Int> {
        return ResponseEntity(service.modifyEntity(entity), HttpStatus.valueOf(200))
    }
}
