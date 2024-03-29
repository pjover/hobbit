package cat.hobbiton.hobbit.api.controller

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.service.generate.GenerateService
import cat.hobbiton.hobbit.util.resource.getResponseHeaders
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
        method = [RequestMethod.GET]
    )
    fun simulateBDD(
        @RequestParam(
            value = "yearMonth",
            required = false
        ) yearMonth: String
    ): ResponseEntity<PaymentTypeInvoicesDTO> {
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
        method = [RequestMethod.POST]
    )
    fun generateBDD(
        @NotNull @RequestParam(
            value = "yearMonth",
            required = true
        ) yearMonth: String
    ): ResponseEntity<Resource> {
        val bdd = service.generateBDD(yearMonth)
        return ResponseEntity(
            bdd,
            bdd.getResponseHeaders(MediaType.APPLICATION_XML),
            HttpStatus.valueOf(200)
        )
    }

    @Operation(
        description = "Generates the PDF invoice files",
        operationId = "generatePDFs"
    )
    @RequestMapping(
        value = ["/generate/pdf"],
        produces = ["application/zip"],
        method = [RequestMethod.POST]
    )
    fun generatePDFs(
        @NotNull @RequestParam(value = "yearMonth", required = true) yearMonth: String,
        @NotNull @RequestParam(value = "notYetPrinted", required = true) notYetPrinted: Boolean
    ): ResponseEntity<Resource> {
        val zip = service.generatePDFs(yearMonth, notYetPrinted)
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
        method = [RequestMethod.POST]
    )
    fun generatePDF(@PathVariable("invoiceId") invoiceId: String): ResponseEntity<Resource> {
        val pdf = service.generatePDF(invoiceId)
        return ResponseEntity(
            pdf,
            pdf.getResponseHeaders(MediaType.APPLICATION_PDF),
            HttpStatus.valueOf(200)
        )
    }

    @Operation(
        description = "Return the pending invoices (not sent by email) that will be sent to the customers",
        operationId = "simulateEmails"
    )
    @RequestMapping(
        value = ["/generate/email"],
        produces = ["application/json"],
        method = [RequestMethod.GET]
    )
    fun simulateEmails(
        @RequestParam(
            value = "yearMonth",
            required = true
        ) yearMonth: String
    ): ResponseEntity<PaymentTypeInvoicesDTO> {
        return ResponseEntity(service.simulateEmails(yearMonth), HttpStatus.valueOf(200))
    }

    @Operation(
        description = "Sends the emails to the customers, for all pending invoices (not sent by email)",
        operationId = "generateEmails"
    )
    @RequestMapping(
        value = ["/generate/email"],
        produces = ["application/json"],
        method = [RequestMethod.POST]
    )
    fun generateEmails(
        @NotNull @RequestParam(
            value = "yearMonth",
            required = true
        ) yearMonth: String
    ): ResponseEntity<PaymentTypeInvoicesDTO> {
        return ResponseEntity(service.generateEmails(yearMonth), HttpStatus.valueOf(200))
    }

    @Operation(
        description = "Generates the month report spreadsheet with the invoices generated on the yearMonth",
        operationId = "generateMonthSpreadSheet"
    )
    @RequestMapping(
        value = ["/generate/monthSpreadSheet"],
        produces = ["application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"],
        method = [RequestMethod.POST]
    )
    fun generateMonthReport(
        @NotNull @RequestParam(
            value = "yearMonth",
            required = true
        ) yearMonth: String
    ): ResponseEntity<Resource> {
        val xlsx = service.generateMonthSpreadSheet(yearMonth)
        return ResponseEntity(
            xlsx,
            xlsx.getResponseHeaders(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")),
            HttpStatus.valueOf(200)
        )
    }

    @Operation(
        description = "Generates the year report spreadsheet with the invoices generated on the year",
        operationId = "generateYearSpreadSheet"
    )
    @RequestMapping(
        value = ["/generate/yearSpreadSheet"],
        produces = ["application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"],
        method = [RequestMethod.POST]
    )
    fun generateYearSpreadSheet(
        @NotNull @RequestParam(
            value = "year",
            required = true
        ) year: Int
    ): ResponseEntity<Resource> {
        val xlsx = service.generateYearSpreadSheet(year)
        return ResponseEntity(
            xlsx,
            xlsx.getResponseHeaders(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")),
            HttpStatus.valueOf(200)
        )
    }

    @Operation(
        description = "Generates an spreadsheet with the customer's relevant info",
        operationId = "generateCustomersSpreadSheet"
    )
    @RequestMapping(
        value = ["/generate/customersSpreadSheet"],
        produces = ["application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"],
        method = [RequestMethod.POST]
    )
    fun generateCustomersSpreadSheet(): ResponseEntity<Resource> {
        val xlsx = service.generateCustomersSpreadSheet()
        return ResponseEntity(
            xlsx,
            xlsx.getResponseHeaders(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")),
            HttpStatus.valueOf(200)
        )
    }

}
