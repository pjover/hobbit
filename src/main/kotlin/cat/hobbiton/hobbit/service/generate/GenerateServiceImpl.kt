package cat.hobbiton.hobbit.service.generate

import cat.hobbiton.hobbit.api.model.PaymentTypeInvoicesDTO
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service

@Service
class GenerateServiceImpl : GenerateService {

    override fun generateSepa(): Resource {
        TODO("Not yet implemented")
    }

    override fun simulateSepa(): List<PaymentTypeInvoicesDTO> {
        TODO("Not yet implemented")
    }
}