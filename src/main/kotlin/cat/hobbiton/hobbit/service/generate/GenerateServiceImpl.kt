package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import cat.hobbiton.hobbit.service.generate.bdd.BddService
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
class GenerateServiceImpl(
    private val bddService: BddService
) : GenerateService {

    override fun generateBDD(yearMonth: String?): Resource {
        return bddService.generateBDD(yearMonth)
    }

    override fun simulateBDD(yearMonth: String?): PaymentTypeInvoicesDTO {
        return bddService.simulateBDD(yearMonth)
    }

    override fun simulatePDFs(yearMonth: String?): PaymentTypeInvoicesDTO {
        TODO("not implemented")
    }


    override fun generatePDFs(yearMonth: String?): Resource {
        TODO("not implemented")
    }

    override fun generatePDF(invoiceId: String): Resource {
        TODO("not implemented")
    }
}