package cat.hobbiton.hobbit.api.controller

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.service.generate.GenerateService
import cat.hobbiton.hobbit.util.getResponseHeaders
import io.swagger.v3.oas.annotations.Operation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.NotNull

@RestController
@Validated
@RequestMapping("\${api.base-path:}")
class GenerateController(@Autowired(required = true) val service: GenerateService) {


    @Operation(
        description = "Simulates generating the bank direct debit (BDD) file (SEPA XML compatible)" +
            " for the pending invoices (not generated previously)",
        operationId = "simulateBDD"
    )
    @RequestMapping(
        value = ["/generate/bdd"],
        produces = ["application/json"],
        method = [RequestMethod.GET])
    fun simulateBDD(@RequestParam(value = "yearMonth", required = false) yearMonth: String?): ResponseEntity<PaymentTypeInvoicesDTO> {
        return ResponseEntity(service.simulateBDD(yearMonth), HttpStatus.valueOf(200))
    }


    @Operation(
        description = "Generates the bank direct debit (BDD) file (SEPA XML compatible)" +
            " for the pending invoices (not generated previously)",
        operationId = "generateBDD"
    )
    @RequestMapping(
        value = ["/generate/bdd"],
        produces = ["application/xml"],
        method = [RequestMethod.POST])
    fun generateBDD(@NotNull @RequestParam(value = "yearMonth", required = true) yearMonth: String): ResponseEntity<Resource> {

        val bdd = service.generateBDD(yearMonth)
        return ResponseEntity(
            bdd,
            bdd.getResponseHeaders(MediaType.APPLICATION_XML),
            HttpStatus.valueOf(200)
        )
    }

    @Operation(
        description = "Return the pending invoices (no printed to PDF) that will generate the invoice PDF file",
        operationId = "simulatePDFs"
    )
    @RequestMapping(
        value = ["/generate/pdf"],
        produces = ["application/json"],
        method = [RequestMethod.GET])
    fun simulatePDFs(@NotNull @RequestParam(value = "yearMonth", required = true) yearMonth: String): ResponseEntity<List<PaymentTypeInvoicesDTO>> {
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
    fun generatePDFs(@NotNull @RequestParam(value = "yearMonth", required = true) yearMonth: String): ResponseEntity<Resource> {

        val zip = service.generatePDFs(yearMonth)
        return ResponseEntity(
            zip,
            zip.getResponseHeaders(MediaType.parseMediaType("application/zip")),
            HttpStatus.valueOf(200)
        )
    }

    @Operation(
        description = "Generates the PDF invoice file",
        operationId = "generatePDF"
    )
    @RequestMapping(
        value = ["/generate/pdf/{invoiceId}"],
        produces = ["application/pdf"],
        method = [RequestMethod.POST])
    fun generatePDF(@PathVariable("invoiceId") invoiceId: String): ResponseEntity<Resource> {

        val pdf = service.generatePDF(invoiceId)
        return ResponseEntity(
            pdf,
            pdf.getResponseHeaders(MediaType.APPLICATION_PDF),
            HttpStatus.valueOf(200)
        )
    }
}
