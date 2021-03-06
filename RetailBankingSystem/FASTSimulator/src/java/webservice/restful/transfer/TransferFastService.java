/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.transfer;

import ejb.session.bean.FASTSessionBean;
import entity.FastTransfer;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.primefaces.json.JSONObject;

/**
 *
 * @author leiyang
 */
@Path("fast_transfer_clearing")
public class TransferFastService {

    @EJB
    private FASTSessionBean fastBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response transferClearing(
            @FormParam("referenceNumber") String referenceNumber,
            @FormParam("amount") String amount,
            @FormParam("toBankCode") String toBankCode,
            @FormParam("fromBankCode") String fromBankCode,
            @FormParam("fromBankName") String fromBankName,
            @FormParam("toBranchCode") String toBranchCode,
            @FormParam("toBankName") String toBankName,
            @FormParam("accountNumber") String accountNumber,
            @FormParam("toName") String toName,
            @FormParam("fromName") String fromName,
            @FormParam("myInitial") String myInitial
    ) {
        System.out.println(".");
        System.out.println("[SACH]");
        System.out.println("Received Payment Instruction from MBS:");
        System.out.println(".      Transaction Number:" + referenceNumber);
        System.out.println(".      Payment Amount: $" + amount);
        System.out.println(".      To Bank Code:" + toBankCode);
        System.out.println(".      To Branch Code:" + toBranchCode);
        System.out.println(".      To Bank Account:" + accountNumber);
        System.out.println(".      Receiver's Name:" + toName);
        System.out.println(".      From Bank Code:" + fromBankCode);
        System.out.println(".      Sender's Name:" + fromName);
        System.out.println(".      Sender's Initial:" + myInitial);
        System.out.println("Received POST http fast_transfer_clearing");
        // at this point, Clear and save all to db before give a end of day settlement amount
        FastTransfer pt = new FastTransfer();
        pt.setReferenceNumber(referenceNumber);
        pt.setAmount(new BigDecimal(amount));
        pt.setFromBankCode(fromBankCode);
        pt.setFromBankName(fromBankName);
        pt.setToBankCode(toBankCode);
        pt.setToBankName(toBankName);
        pt.setToBranchCode(toBranchCode);
        pt.setAccountNumber(accountNumber);
        pt.setToName(toName);
        pt.setFromName(fromName);
        pt.setMyInitial(myInitial);
        pt.setSettled(Boolean.FALSE);
        fastBean.persist(pt);

        fastBean.sendMEPS(pt);

        try {
            Thread.sleep(500);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        System.out.println(".");
        System.out.println("[SACH]:");
        System.out.println("Sending to receiving bank the payment instructions...");
        System.out.println("Received successful response from receiving bank");
        //TODO send to new receiving bank if necesssary

        System.out.println(".");
        System.out.println("[SACH]:");
        System.out.println("Sending to originating bank response...");
        System.out.println("Sending back fast_transfer_clearing response");
        MessageDTO message = new MessageDTO();
        message.setCode(0);
        message.setMessage("SUCCESS");
        return Response.ok(new JSONObject(message).toString(), MediaType.APPLICATION_JSON).build();
    }
}
