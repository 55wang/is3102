/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.transfer;

import ejb.session.bill.BillSessionBeanLocal;
import ejb.session.card.CardTransactionSessionBeanLocal;
import ejb.session.webservice.WebserviceSessionBeanLocal;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
    private BillSessionBeanLocal billSessionBean;

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

        billSessionBean.updateTransactionStatusSettled(mbsTransactions);
        System.out.println("Transactions status updated...");

        System.out.println("Date: " + sentDate);

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

    private CardTransactionSessionBeanLocal lookupCardTransactionSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (CardTransactionSessionBeanLocal) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/CardTransactionSessionBean!ejb.session.card.CardTransactionSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private BillSessionBeanLocal lookupBillSessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (BillSessionBeanLocal) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/BillSessionBean!ejb.session.bill.BillSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
