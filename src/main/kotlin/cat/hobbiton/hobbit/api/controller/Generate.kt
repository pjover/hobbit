package cat.hobbiton.hobbit.api.controller

import cat.hobbiton.hobbit.service.generate.GenerateService
import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.validation.annotation.Validated
import org.springframework.beans.factory.annotation.Autowired

import kotlin.collections.List

@RestController
@Validated
@RequestMapping("\${api.base-path:}")
class GenerateController(@Autowired(required = true) val service: GenerateService) {


    @RequestMapping(
        value = ["/generate/sepa"],
        produces = ["application/xml"],
        method = [RequestMethod.POST])
    fun generateSepa(): ResponseEntity<org.springframework.core.io.Resource> {
        return ResponseEntity(service.generateSepa(), HttpStatus.valueOf(200))
    }


    @RequestMapping(
        value = ["/generate/sepa"],
        produces = ["application/json"],
        method = [RequestMethod.GET])
    fun simulateSepa(): ResponseEntity<List<PaymentTypeInvoicesDTO>> {
        return ResponseEntity(service.simulateSepa(), HttpStatus.valueOf(200))
    }
}
