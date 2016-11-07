/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.transfer;

import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.dams.MobileAccountSessionBeanLocal;
import entity.common.TransactionRecord;
import entity.customer.MainAccount;
import entity.dams.account.MobileAccount;
import java.math.BigDecimal;
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
import webservice.restful.mobile.ErrorDTO;

/**
 *
 * @author leiyang
 */
@Path("mobile_inter_account_transfer")
public class MobileInterAccountTransferService {

    @Context
    private UriInfo context;

    @EJB
    private TransferSessionBeanLocal transferBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private MobileAccountSessionBeanLocal mobileBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response interAccountTransfer(
            @FormParam("fromAccountNumber") String fromAccountNumber,
            @FormParam("toAccountNumber") String toAccountNumber,
            @FormParam("transferAmount") String transferAmount
    ) {
        System.out.println("Received fromAccountNumber:" + fromAccountNumber);
        System.out.println("Received toAccountNumber:" + toAccountNumber);
        System.out.println("Received transferAmount:" + transferAmount);
        System.out.println("Received POST http with url: /mobile_inter_account_transfer");

        String jsonString;
        BigDecimal amount = new BigDecimal(transferAmount);

        if (toAccountNumber.length() == 8) {
            System.out.println("Received mobile account with number:" + toAccountNumber);
            MobileAccount ma = mobileBean.getMobileAccountByMobileNumber(toAccountNumber);

            BigDecimal resultAmount = amount.add(ma.getBalance());
            if (resultAmount.compareTo(new BigDecimal(999)) > 0) {
                ErrorDTO err = new ErrorDTO();
                err.setCode(-2);
                err.setError("Transfer Failed! Exceed Wallet Limit!");
                jsonString = new JSONObject(err).toString();
                return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
            }
        }

        
        String result = transferBean.transferFromAccountToAccount(fromAccountNumber, toAccountNumber, amount);
        if (result.equals("SUCCESS")) {
            JSONObject jo = new JSONObject();
            TransactionRecord tr = depositBean.latestTransactionFromAccountNumber(fromAccountNumber);
            jo.put("referenceNumber", tr.getReferenceNumber());
            jsonString = jo.toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        } else {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("Transfer Failed!");
            jsonString = new JSONObject(err).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        }
    }
}
