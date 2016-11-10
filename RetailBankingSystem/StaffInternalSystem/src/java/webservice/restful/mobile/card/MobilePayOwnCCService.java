/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.dams.MobileAccountSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import entity.common.TransactionRecord;
import entity.dams.account.MobileAccount;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.primefaces.json.JSONObject;
import server.utilities.DateUtils;
import webservice.restful.mobile.ErrorDTO;
import webservice.restful.mobile.TransferDTO;

/**
 *
 * @author leiyang
 */
@Path("mobile_pay_own_cc")
public class MobilePayOwnCCService {

    @EJB
    private MobileAccountSessionBeanLocal mobileBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response mobileTransfer(
            @FormParam("mobileNumber") String mobileNumber,
            @FormParam("ccNumber") String ccNumber,
            @FormParam("amount") String amount
    ) {
        System.out.println("Received mobileNumber:" + mobileNumber);
        System.out.println("Received ccNumber:" + ccNumber);
        System.out.println("Received amount:" + amount);
        System.out.println("Received POST http mobile_pay_own_cc");
        String jsonString = null;

        String result = mobileBean.payCCBillFromMobileAccount(mobileNumber, ccNumber, new BigDecimal(amount));
        if (result.equals("SUCCESS")) {
            TransactionRecord record = mobileBean.latestTransactionFromMobileNumber(mobileNumber);
            TransferDTO t = new TransferDTO();
            t.setTransferAmount(record.getAmount().toString());
            t.setReferenceNumber(record.getReferenceNumber());
            t.setTransferType(record.getActionType().toString());
            t.setTransferDate(DateUtils.readableDate(record.getCreationDate()));
            t.setTransferAccount("CC Ending: " + ccNumber.substring(ccNumber.length() - 4, ccNumber.length()));
            jsonString = new JSONObject(t).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        } else {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError(result);
            jsonString = new JSONObject(err).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        }
    }
}
