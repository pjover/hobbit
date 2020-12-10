package cat.hobbiton.hobbit.service.generate.bdd.string

import cat.hobbiton.hobbit.model.Invoice
import cat.hobbiton.hobbit.service.generate.bdd.BddService
import org.springframework.stereotype.Component

@Component
class StringBddServiceImpl(
    private val invoicesToBddMapper: InvoicesToBddMapper
) : BddService {

    override fun generate(invoices: List<Invoice>): String {
        val bdd = invoicesToBddMapper.map(invoices)
        return formatBdd(bdd)
    }

    private fun formatBdd(bdd: Bdd): String {
        return String.format(CONTENT,
            bdd.messageIdentification,
            bdd.creationDateTime,
            bdd.numberOfTransactions,
            bdd.controlSum,
            bdd.name,
            bdd.identification,
            bdd.messageIdentification,
            bdd.numberOfTransactions,
            bdd.controlSum,
            bdd.requestedCollectionDate,
            bdd.name,
            bdd.country,
            bdd.addressLine1,
            bdd.addressLine2,
            bdd.iban,
            bdd.bic,
            bdd.identification,
            getDetail(bdd.details)
        )
    }

    private fun getDetail(details: List<BddDetail>?): String {
        val xml = StringBuilder()
        for(detail in details!!) {
            xml.append(String.format(DETAIL_CONTENT,
                detail.endToEndIdentifier,
                detail.instructedAmount,
                detail.endToEndIdentifier,
                detail.dateOfSignature,
                detail.name,
                getDetailIdentification(detail),
                detail.iban,
                detail.purposeCode,
                detail.remittanceInformation))
        }
        return xml.toString()
    }

    private fun getDetailIdentification(detail: BddDetail): String {
        return String.format(
            if(detail.isBusiness) ORGANISATION_IDENTIFICATION else PRIVATE_IDENTIFICATION,
            detail.identification)
    }

    companion object {
        private const val CONTENT = "<?xml version=\"1.0\" encoding=\"utf-8\"?>%n" +
            "<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:pain.008.001.02\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">%n" +
            "<CstmrDrctDbtInitn>%n" +
            "    <GrpHdr>%n" +  // Header //
            "        <MsgId>%s</MsgId>%n" +  // Message Identification
            "        <CreDtTm>%s</CreDtTm>%n" +  // CreationDateTime
            "        <NbOfTxs>%d</NbOfTxs>%n" +  // NumberOfTransactions
            "        <CtrlSum>%s</CtrlSum>%n" +  // ControlSum
            "        <InitgPty>%n" +  // Init party //
            "            <Nm>%s</Nm>%n" +  // Name
            "            <Id> %n" +
            "                <OrgId>%n" +
            "                    <Othr>%n" +
            "                        <Id>%s</Id>%n" +  // Identification
            "                    </Othr>%n" +
            "                </OrgId>%n" +
            "            </Id>%n " +
            "        </InitgPty>%n" +
            "    </GrpHdr>%n" +
            "    <PmtInf>%n" +  // Payment info //
            "        <PmtInfId>%s-1</PmtInfId>%n" +  // MessageIdentification
            "        <PmtMtd>DD</PmtMtd>%n" +
            "        <NbOfTxs>%d</NbOfTxs>%n" +  // NumberOfTransactions
            "        <CtrlSum>%s</CtrlSum>%n" +  // ControlSum
            "        <PmtTpInf>%n" +  // Información del tipo de pago
            "            <SvcLvl>%n" +
            "                <Cd>SEPA</Cd>%n" +
            "            </SvcLvl>%n" +
            "            <LclInstrm>%n" +
            "                <Cd>CORE</Cd>%n" +
            "            </LclInstrm>%n" +
            "            <SeqTp>OOFF</SeqTp>%n" +
            "        </PmtTpInf>%n" +
            "        <ReqdColltnDt>%s</ReqdColltnDt>%n" +  // RequestedCollectionDate
            "        <Cdtr>%n" +  // Acreedor //
            "            <Nm>%s</Nm>%n" +  // Name
            "            <PstlAdr>%n" +
            "                <Ctry>%s</Ctry>%n" +  // País del acreedor
            "                <AdrLine>%s</AdrLine>%n" +  // Dirección postal en texto libre 1 del acreedor
            "                <AdrLine>%s</AdrLine>%n" +  // Dirección postal en texto libre 2 del acreedor
            "            </PstlAdr>%n" +
            "        </Cdtr>%n" +
            "        <CdtrAcct>%n" +  // Cuenta del acreedor //
            "            <Id>%n" +
            "                <IBAN>%s</IBAN>%n" +  // IBAN de la cuenta del acreedor
            "            </Id>%n" +
            "        </CdtrAcct>%n" +
            "        <CdtrAgt>%n" +  // Entidad del acreedor //
            "            <FinInstnId>%n" +
            "                <BIC>%s</BIC>%n" +  // BIC de la entidad del acreedor (Código asignado a cada entidad de crédito por la autoridad de registro ISO 9362)
            "            </FinInstnId>%n" +
            "        </CdtrAgt>%n" +
            "        <ChrgBr>SLEV</ChrgBr>%n" +  // Cláusula de gastos
            "        <CdtrSchmeId>%n" +  // Identificación del acreedor //
            "            <Id>%n" +
            "                <PrvtId>%n" +
            "                    <Othr>%n" +
            "                        <Id>%s</Id>%n" +  // Identificación del acreedor
            "                        <SchmeNm>%n" +
            "                            <Prtry>SEPA</Prtry>%n" +
            "                        </SchmeNm>%n" +
            "                    </Othr>%n" +
            "                </PrvtId>%n" +
            "            </Id>%n" +
            "        </CdtrSchmeId>%n" +
            "%s" +  // DrctDbtTxInf: Información de la operación de adeudo directo //
            "    </PmtInf>%n" +
            "</CstmrDrctDbtInitn>%n" +
            "</Document>%n"
        private const val DETAIL_CONTENT = "        <DrctDbtTxInf>%n" +
            "            <PmtId>%n" +
            "                <EndToEndId>%s</EndToEndId>%n" +
            "            </PmtId>%n" +
            "            <InstdAmt Ccy=\"EUR\">%s</InstdAmt>%n" +
            "            <DrctDbtTx>%n" +
            "                <MndtRltdInf>%n" +
            "                    <MndtId>%s</MndtId>%n" +
            "                    <DtOfSgntr>%s</DtOfSgntr>%n" +
            "                </MndtRltdInf>%n" +
            "            </DrctDbtTx>%n" +
            "            <DbtrAgt>%n" +
            "                <FinInstnId>%n" +
            "                    <Othr>%n" +
            "                        <Id>NOTPROVIDED</Id>%n" +
            "                    </Othr>%n" +
            "                </FinInstnId>%n" +
            "            </DbtrAgt>%n" +
            "            <Dbtr>%n" +
            "                <Nm>%s</Nm>%n" +
            "                <Id>%n" +
            "%s" +
            "                </Id>%n" +
            "            </Dbtr>%n" +
            "            <DbtrAcct>%n" +
            "                <Id>%n" +
            "                    <IBAN>%s</IBAN>%n" +
            "                </Id>%n" +
            "            </DbtrAcct>%n" +
            "            <Purp>%n" +
            "                <Cd>%s</Cd>%n" +
            "            </Purp>%n" +
            "            <RmtInf>%n" +
            "                <Ustrd>%s</Ustrd>%n" +
            "            </RmtInf>%n" +
            "        </DrctDbtTxInf>%n"
        private const val ORGANISATION_IDENTIFICATION = "                    <OrgId>%n" +
            "                        <Othr>%n" +
            "                            <Id>%s</Id>%n" +
            "                            <SchmeNm>%n" +
            "                                <Prtry>SEPA</Prtry>%n" +
            "                            </SchmeNm>%n" +
            "                        </Othr>%n" +
            "                    </OrgId>%n"
        private const val PRIVATE_IDENTIFICATION = "                    <PrvtId>%n" +
            "                        <Othr>%n" +
            "                            <Id>%s</Id>%n" +
            "                            <SchmeNm>%n" +
            "                                <Prtry>SEPA</Prtry>%n" +
            "                            </SchmeNm>%n" +
            "                        </Othr>%n" +
            "                    </PrvtId>%n"
    }
}
