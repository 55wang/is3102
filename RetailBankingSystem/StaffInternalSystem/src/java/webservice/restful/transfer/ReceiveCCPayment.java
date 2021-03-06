/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.transfer;

import ejb.session.bill.BillSessionBeanLocal;
import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.bill.BillFundTransferRecord;
import entity.card.account.CreditCardAccount;
import entity.dams.account.DepositAccount;
import java.math.BigDecimal;
import java.util.Date;
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
@Path("mbs_receive_cc_payment")
public class ReceiveCCPayment {

    @EJB
    private BillSessionBeanLocal billSessionBean;
    @EJB
    private CardAcctSessionBeanLocal cardBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response netSettlement(
            @FormParam("referenceNumber") String referenceNumber,
            @FormParam("partnerBankCode") String toBankCode,
            @FormParam("fromBankCode") String fromBankCode,
            @FormParam("ccNumber") String ccNumber,
            @FormParam("ccAmount") String ccAmount
    ) {
        System.out.println(".");
        System.out.println("[MBS]:");
        System.out.println("Received payment instruction from SACH...");
        System.out.println(".      Received partnerBankCode:" + toBankCode);
        System.out.println(".      Received fromBankCode:" + fromBankCode);
        System.out.println(".      Received ccNumber:" + ccNumber);
        System.out.println(".      Received ccAmount:" + ccAmount);

        System.out.println("Received POST http mbs_receive_cc_payment");

        CreditCardAccount cca = cardBean.getCreditCardAccountByCardNumber(ccNumber);

        BillFundTransferRecord bft = new BillFundTransferRecord();
        bft.setReferenceNumber(referenceNumber);
        bft.setBillReferenceNumber(ccNumber);
        bft.setToBankCode("001");
        bft.setFromBankCode(fromBankCode);
        bft.setAmount(new BigDecimal(ccAmount));
        bft.setSettled(Boolean.FALSE);
        bft.setCreationDate(new Date());

        billSessionBean.createBillFundTransferRecord(bft);

        if (cca == null) {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("Credit Card Account Not Found");
            return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
        } else {
            System.out.println("Sending back mbs_receive_cc_payment response");
            System.out.println("Current credit card outstanding amount: " + cardBean.getCreditCardAccountByCardNumber(ccNumber).getOutstandingAmount());
            System.out.println("Updating balance...");
            cardBean.payCreditCardAccountBillByCardNumber(ccNumber, new BigDecimal(ccAmount));
            System.out.println("Updated credit card outstanding amount: " + cardBean.getCreditCardAccountByCardNumber(ccNumber).getOutstandingAmount());
            ErrorDTO err = new ErrorDTO();
            err.setCode(0);
            err.setError("SUCCESS");
            return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
        }

    }

}
