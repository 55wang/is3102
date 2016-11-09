/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.transfer;

import ejb.session.bill.BillSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.bill.BillFundTransferRecord;
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
@Path("mbs_receive_transfer_payment")
public class ReceiveTransferPayment {

    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private BillSessionBeanLocal billSessionBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response netSettlement(
            @FormParam("referenceNumber") String referenceNumber,
            @FormParam("amount") String amount,
            @FormParam("toBankCode") String toBankCode,
            @FormParam("toBankAccount") String accountNumber,
            @FormParam("toName") String toName,
            @FormParam("fromCode") String fromCode,
            @FormParam("fromName") String fromName,
            @FormParam("myInitial") String myInitial
    ) {
        System.out.println(".");
        System.out.println("[MBS]:");
        System.out.println(".      Received payment instruction from SACH...");
        System.out.println(".      Received Reference Number:" + referenceNumber);
        System.out.println(".      Received Amount:" + amount);
        System.out.println(".      Received Account Number:" + accountNumber);
        System.out.println(".      Received To Name:" + toName);
        System.out.println(".      Received From Code:" + fromCode);
        System.out.println(".      Received From Name:" + fromName);
        System.out.println(".      Received my Initial:" + myInitial);
        System.out.println(".      Received POST http mbs_receive_transfer_payment");

        DepositAccount da = depositBean.getAccountFromId(accountNumber);
        if (da == null) {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("Account Not Found");
            return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
        } else {

            BillFundTransferRecord bft = new BillFundTransferRecord();
            bft.setReferenceNumber(referenceNumber);
            bft.setToBankAccount(accountNumber);
            bft.setToBankCode("001");
            bft.setFromBankCode(fromCode);
            bft.setCreationDate(new Date());
            bft.setAmount(new BigDecimal(amount));
            bft.setSettled(Boolean.FALSE);
            billSessionBean.createBillFundTransferRecord(bft);

            System.out.println("Sending back mbs_receive_transfer_payment response");
            System.out.println("Current bank account balance: " + da.getBalance());
            System.out.println("Updating balance...");
            depositBean.transferToAccount(da, new BigDecimal(amount));
            System.out.println("Updated bank account balance: " + da.getBalance());

            ErrorDTO err = new ErrorDTO();
            err.setCode(0);
            err.setError("SUCCESS");
            return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
        }
    }
}
