package cat.hobbiton.hobbit.api.controller

import cat.hobbiton.hobbit.api.model.CustomerListDTO
import cat.hobbiton.hobbit.service.search.SearchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.NotNull

@RestController
@Validated
@RequestMapping("\${api.base-path:}")
class SearchController(@Autowired(required = true) val service: SearchService) {


    @RequestMapping(
            value = ["/search/customer"],
            produces = ["application/json"],
            method = [RequestMethod.GET])
    fun searchCustomer(@NotNull @RequestParam(value = "text", required = true) text: String
    ): ResponseEntity<List<CustomerListDTO>> {
        return ResponseEntity(service.searchCustomer(text), HttpStatus.valueOf(200))
    }
}
