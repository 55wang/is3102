/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile;

import ejb.session.dams.MobileAccountSessionBeanLocal;
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

/**
 *
 * @author leiyang
 */
@Path("transfer")
public class MobileTransferService {

    @EJB
    private MobileAccountSessionBeanLocal mobileBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response mobileTransfer(
            @FormParam("fromAccount") String fromAccount,
            @FormParam("toAccount") String toAccount,
            @FormParam("amount") String amount
    ) {
        System.out.println("Received fromAccount:" + fromAccount);
        System.out.println("Received toAccount:" + toAccount);
        System.out.println("Received amount:" + amount);
        System.out.println("Received POST http transfer");
        String jsonString = null;
        MobileAccount fromMobileAccount = mobileBean.getMobileAccountByMobileNumber(fromAccount);
        MobileAccount toMobileAccount = mobileBean.getMobileAccountByMobileNumber(toAccount);
        // request to set up mobile password
        if (toMobileAccount != null) {
            BigDecimal actualAmount = new BigDecimal(amount);
            if (fromMobileAccount.getBalance().compareTo(actualAmount) < 0) {
                ErrorDTO err = new ErrorDTO();
                err.setCode(-2);
                err.setError("Not enough balance! Please top up!");
                jsonString = new JSONObject(err).toString();
                return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
            } else {
                String result = mobileBean.transferFromAccountToAccount(fromAccount, toAccount, actualAmount);
                if (result.equals("SUCCESS")) {
                    TransactionRecord record = mobileBean.latestTransactionFromMobileAccount(fromMobileAccount);
                    TransferDTO t = new TransferDTO();
                    t.setTransferAmount(record.getAmount().setScale(2, RoundingMode.UP).toString());
                    t.setReferenceNumber(record.getReferenceNumber());
                    t.setTransferType(record.getActionType().toString());
                    t.setTransferDate(DateUtils.readableDate(record.getCreationDate()));
                    t.setTransferAccount(toAccount);
                    jsonString = new JSONObject(t).toString();
                    return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
                } else {
                    ErrorDTO err = new ErrorDTO();
                    err.setCode(-2);
                    err.setError(result);
                    jsonString = new JSONObject(err).toString();
                    return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
                }
            }
        } else {
            // error
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("No such user found!");
            jsonString = new JSONObject(err).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        }
    }
}
