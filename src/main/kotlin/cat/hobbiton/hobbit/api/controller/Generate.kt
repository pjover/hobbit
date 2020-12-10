package cat.hobbiton.hobbit.api.controller

import cat.hobbiton.hobbit.service.generate.GenerateService
import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.validation.annotation.Validated
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

@RestController
@Validated
@RequestMapping("\${api.base-path:}")
class GenerateController(@Autowired(required = true) val service: GenerateService) {


    @RequestMapping(
        value = ["/generate/bdd"],
        produces = ["application/xml"],
        method = [RequestMethod.POST])
    fun generateBDD(@RequestParam(value = "yearMonth", required = false) yearMonth: String?
    ): ResponseEntity<org.springframework.core.io.Resource> {

        val bdd = service.generateBDD(yearMonth)

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_XML
        headers.contentLength = bdd.contentLength()
        headers.setContentDispositionFormData("attachment", "bdd.q1x")

        return ResponseEntity(service.generateBDD(yearMonth), headers, HttpStatus.valueOf(200))
    }

    @RequestMapping(
        value = ["/generate/bdd"],
        produces = ["application/json"],
        method = [RequestMethod.GET])
    fun simulateBDD(@RequestParam(value = "yearMonth", required = false) yearMonth: String?
    ): ResponseEntity<PaymentTypeInvoicesDTO> {
        return ResponseEntity(service.simulateBDD(yearMonth), HttpStatus.valueOf(200))
    }
}
