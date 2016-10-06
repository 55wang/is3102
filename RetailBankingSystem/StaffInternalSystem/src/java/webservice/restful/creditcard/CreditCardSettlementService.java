/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.creditcard;

import com.google.gson.Gson;
import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.card.CardTransactionSessionBeanLocal;
import entity.card.account.CardTransaction;
import entity.card.account.CreditCardAccount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.primefaces.json.JSONObject;
import server.utilities.EnumUtils;

/**
 *
 * @author wang
 */
@Path("credit_card_settlement")
public class CreditCardSettlementService {

    @Context
    private UriInfo context;

    @EJB
    private CardTransactionSessionBeanLocal cardTransactionSessionBean;
    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response settlementCC(
            @FormParam("swiftCode") String swiftCode,
            @FormParam("bankCode") String bankCode,
            @FormParam("branchCode") String branchCode,
            @FormParam("netAmount") String netAmount,
            @FormParam("visaIds") String visaIds
    ) {
        System.out.println("settlementCC(): " +netAmount);
        Boolean result = transferFundsToPayee();

        if (result) {
            Gson gson = new Gson();
            String[] temp = gson.fromJson(visaIds, String[].class);
            ArrayList<String> visaIdsList = new ArrayList(Arrays.asList(temp));
            settledCreditCardTransaction(visaIdsList);

            JSONObject json = new JSONObject();
            json.put("SETTLEMENT", "SUCCESS");
            String jsonString = json.toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();

        } else {
            JSONObject json = new JSONObject();
            json.put("SETTLEMENT", "FAIL");
            String jsonString = json.toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        }
        /*
         CreditCardDTO c = new CreditCardDTO();
         String jsonString = new JSONObject(c).toString();
         return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
         */
    }

    private Boolean transferFundsToPayee() {
        //transfer $ to another bank for credit card payment.
        return true;
    }

    private void settledCreditCardTransaction(List<String> visaCardIds) {

        for (String id : visaCardIds) {
            CardTransaction ct = cardTransactionSessionBean.getCardTransactionByVisaId(id);
            if (ct.getCardTransactionStatus() == EnumUtils.CardTransactionStatus.PENDINGTRANSACTION) {
                ct.setCardTransactionStatus(EnumUtils.CardTransactionStatus.SETTLEDTRANSACTION);
                cardTransactionSessionBean.updateCardTransaction(ct);
            }
        }
    }

    private Boolean creditTransaction(CreditCardDTO c) {
        CardTransaction ct = new CardTransaction();
        ct.setUpdateDate(new Date());
        ct.setCardTransactionStatus(EnumUtils.CardTransactionStatus.PENDINGTRANSACTION);
        ct.setAmount(Double.parseDouble(c.getAmount()));
        ct.setIsCredit(true);
        ct.setTransactionCode(c.getTransactionCode());
        ct.setTransactionDescription(c.getDescription());
        ct.setVisaId(c.getVisaId());
        CreditCardAccount cca = cardAcctSessionBean.getCreditCardAccountByCardNumber(c.getCreditCardNumber());
        ct = cardTransactionSessionBean.createCardAccountTransaction(cca, ct);
        return ct != null;
    }
}
