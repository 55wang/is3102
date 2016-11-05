/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.transfer;

import ejb.session.bean.FASTSessionBean;
import entity.PaymentTransfer;
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
            @FormParam("toBranchCode") String toBranchCode,
            @FormParam("accountNumber") String accountNumber,
            @FormParam("toName") String toName,
            @FormParam("fromName") String fromName,
            @FormParam("myInitial") String myInitial
    ) {
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
        PaymentTransfer pt = new PaymentTransfer();
        pt.setReferenceNumber(referenceNumber);
        pt.setAmount(new BigDecimal(amount));
        pt.setFromBankCode(fromBankCode);
        pt.setToBankCode(toBankCode);
        pt.setToBranchCode(toBranchCode);
        pt.setAccountNumber(accountNumber);
        pt.setToName(toName);
        pt.setFromName(fromName);
        pt.setMyInitial(myInitial);
        pt.setSettled(false);
        fastBean.persist(pt);

        System.out.println("Sending Net Settlement Amount to MEPS...");
        fastBean.sendMEPS(pt);

        System.out.println("Sending back fast_transfer_clearing response");
        MessageDTO message = new MessageDTO();
        message.setCode(0);
        message.setMessage("SUCCESS");
        return Response.ok(new JSONObject(message).toString(), MediaType.APPLICATION_JSON).build();
    }
}
