/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.transfer;

import ejb.session.webservice.WebserviceSessionBeanLocal;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.primefaces.json.JSONObject;
import webservice.restful.mobile.ErrorDTO;

/**
 *
 * @author leiyang
 */
@Path("net_settlement")
public class NetSettlementService {

    @EJB
    private WebserviceSessionBeanLocal webserviceBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response netSettlement(
            TransactionSummaryDTO transactionSummary
    ) {

        String citiToBankCode = transactionSummary.getCitiToBankCode();
        String citiToBankName = transactionSummary.getCitiToBankName();
        String citiSettlementAmount = transactionSummary.getCitiSettlementAmount();
        String ocbcToBankCode = transactionSummary.getOcbcToBankCode();
        String ocbcToBankName = transactionSummary.getOcbcToBankName();
        String ocbcSettlementAmount = transactionSummary.getOcbcSettlementAmount();
        String sentDate = transactionSummary.getDate();
        List<String> mbsTransactions = new ArrayList<String>();
        for (TransactionDTO dto : transactionSummary.getTransactionSummary()) {
            mbsTransactions.add(dto.getReferenceNumber());
        }
        System.out.println(".");
        System.out.println("[MBS]");
        System.out.println("Received Net Settlement Broadcast from MEPS:");

        if (new BigDecimal(citiSettlementAmount).compareTo(BigDecimal.ZERO) == -1) {
            System.out.println(".       Net Settlement from " + citiToBankCode + " " + citiToBankName + ": " + new BigDecimal(citiSettlementAmount).abs().setScale(4).toString());
        } else if (new BigDecimal(citiSettlementAmount).compareTo(BigDecimal.ZERO) == 1) {
            System.out.println(".       Net Settlement to " + citiToBankCode + " " + citiToBankName + ": " + new BigDecimal(citiSettlementAmount).abs().setScale(4).toString());
        } else {
        }
        if (new BigDecimal(ocbcSettlementAmount).compareTo(BigDecimal.ZERO) == -1) {
            System.out.println(".       Net Settlement from " + ocbcToBankCode + " " + ocbcToBankName + ": " + new BigDecimal(ocbcSettlementAmount).abs().setScale(4).toString());
        } else if (new BigDecimal(ocbcSettlementAmount).compareTo(BigDecimal.ZERO) == 1) {
            System.out.println(".       Net Settlement to " + ocbcToBankCode + " " + ocbcToBankName + ": " + new BigDecimal(ocbcSettlementAmount).abs().setScale(4).toString());
        } else {
        }
        System.out.println("Settled Transactions:");
        for (String tr : mbsTransactions) {
            System.out.println(".       ID: " + tr);
        }

        System.out.println("Date: " + sentDate);
        System.out.println("Received POST http net_settlement");

        // requesting to MEPS
        // only one case, when it is true
        // TODO: Failed cases
//        if (!referenceNumber.equals("")) {
//            webserviceBean.payFASTSettlement(netSettlementAmount, fromBankCode, toBankCode, agencyCode, referenceNumber);
//        } else {
//            webserviceBean.paySACHSettlement(netSettlementAmount, fromBankCode, toBankCode, agencyCode);
//        }
        System.out.println(".");
        System.out.println("Sending back net_settlement response...");
        ErrorDTO err = new ErrorDTO();
        err.setCode(0);
        err.setError("SUCCESS");
        return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
    }

}
