package cat.hobbiton.hobbit.api.controller

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.service.generate.GenerateService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
@RequestMapping("\${api.base-path:}")
class GenerateController(@Autowired(required = true) val service: GenerateService) {


    @Operation(
        description = "Generates the bank direct debit (BDD) file (SEPA XML compatible)" +
            " for the pending invoices (not generated previously)",
        operationId = "generateBDD"
    )
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

    @Operation(
        description = "Simulates generating the bank direct debit (BDD) file (SEPA XML compatible)" +
            " for the pending invoices (not generated previously)",
        operationId = "simulateBDD"
    )
    @RequestMapping(
        value = ["/generate/bdd"],
        produces = ["application/json"],
        method = [RequestMethod.GET])
    fun simulateBDD(@RequestParam(value = "yearMonth", required = false) yearMonth: String?
    ): ResponseEntity<PaymentTypeInvoicesDTO> {
        return ResponseEntity(service.simulateBDD(yearMonth), HttpStatus.valueOf(200))
    }

    @Operation(
        description = "Return the pending invoices (no printed to PDF) that will generate the invoice PDF file",
        operationId = "simulatePDFs"
    )
    @RequestMapping(
        value = ["/generate/pdf"],
        produces = ["application/json"],
        method = [RequestMethod.GET])
    fun simulatePDFs(@RequestParam(value = "yearMonth", required = false) yearMonth: kotlin.String?
    ): ResponseEntity<PaymentTypeInvoicesDTO> {
        return ResponseEntity(service.simulatePDFs(yearMonth), HttpStatus.valueOf(200))
    }

    @Operation(
        description = "Generates the PDF invoice files for the pending invoices (no printed to PDF)",
        operationId = "generatePDFs"
    )
    @RequestMapping(
        value = ["/generate/pdf"],
        produces = ["application/zip"],
        method = [RequestMethod.POST])
    fun generatePDFs(@RequestParam(value = "yearMonth", required = false) yearMonth: kotlin.String?
    ): ResponseEntity<org.springframework.core.io.Resource> {
        return ResponseEntity(service.generatePDFs(yearMonth), HttpStatus.valueOf(200))
    }

    @Operation(
        description = "Generates the PDF invoice file",
        operationId = "generatePDF"
    )
    @RequestMapping(
        value = ["/generate/pdf/{invoiceId}"],
        produces = ["application/pdf"],
        method = [RequestMethod.POST])
    fun generatePDF(@PathVariable("invoiceId") invoiceId: kotlin.String
    ): ResponseEntity<org.springframework.core.io.Resource> {
        return ResponseEntity(service.generatePDF(invoiceId), HttpStatus.valueOf(200))
    }
}
