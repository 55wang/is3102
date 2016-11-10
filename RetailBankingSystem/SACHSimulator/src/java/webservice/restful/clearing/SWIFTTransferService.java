/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.clearing;

import ejb.session.bean.SACHSessionBean;
import entity.PaymentTransfer;
import entity.SachSettlement;
import java.math.BigDecimal;
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

/**
 *
 * @author qiuxiaqing
 */
@Path("sach_swift_transfer")
public class SWIFTTransferService {

    @EJB
    private SACHSessionBean sachBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response receiveSettlement(
            @FormParam("referenceNumber") String referenceNumber,
            @FormParam("amount") String amount,
            @FormParam("delegatingBank") String delegatingBank,
            @FormParam("toName") String toName,
            @FormParam("fromBankCode") String fromBankCode,
            @FormParam("fromName") String fromName,
            @FormParam("myInitial") String myInitial,
            @FormParam("swiftMessage") String swiftMessage
    ) {
        System.out.println(".");
        System.out.println("[SACH]");
        System.out.println("Received  SWIFT Transfer Request from MBS:");
        System.out.println(".      Transaction Number:" + referenceNumber);
        System.out.println(".      Payment Amount: $" + amount);
        System.out.println(".      To Delegating Bank Code:" + delegatingBank);
        System.out.println(".      Receiver's Name:" + toName);
        System.out.println(".      From Bank Code:" + fromBankCode);
        System.out.println(".      Sender's Name:" + fromName);
        System.out.println(".      Sender's Initial:" + myInitial);
        System.out.println(".      " + swiftMessage);

        // makes payment to other bank
        // TODO: if needed, use a MAP to check net settlement to other banks
        PaymentTransfer pt = new PaymentTransfer();
        pt.setReferenceNumber(referenceNumber);
        pt.setAmount(new BigDecimal(amount));
        pt.setFromBankCode(fromBankCode);
        pt.setToBankCode(delegatingBank);
        pt.setFromName(fromName);
        pt.setMyInitial(myInitial);
        pt.setSettled(false);
        sachBean.persist(pt);
        System.out.println(".");
        System.out.println("[SACH]");
        System.out.println("Sending fund transfer to delegating bank through MEPS: " + delegatingBank);
        List<SachSettlement> bankAccounts = sachBean.getSettlements();

        System.out.println("Current net settlement:");
        for (SachSettlement s : bankAccounts) {
            if (s.getAmount().compareTo(BigDecimal.ZERO) == -1) {
                System.out.println(".       " + s.getToBankCode() + " " + s.getToBankName() + " to " + s.getFromBankCode() + " " + s.getFromBankName() + ": " + s.getAmount().toString());
            } else {
                System.out.println(".       " + s.getFromBankCode() + " " + s.getFromBankName() + " to " + s.getToBankCode() + " " + s.getToBankName() + ": " + s.getAmount().toString());

            }
        }
        System.out.println("Updating net settlement...");
        sachBean.updateNetSettlement(pt);

        List<SachSettlement> updatedbankAccounts = sachBean.getSettlements();

        System.out.println("Updated net settlement:");
        for (SachSettlement s : updatedbankAccounts) {
            if (s.getAmount().compareTo(BigDecimal.ZERO) == -1) {
                System.out.println(".       " + s.getToBankCode() + " " + s.getToBankName() + " to " + s.getFromBankCode() + " " + s.getFromBankName() + ": " + s.getAmount().toString());
            } else {
                System.out.println(".       " + s.getFromBankCode() + " " + s.getFromBankName() + " to " + s.getToBankCode() + " " + s.getToBankName() + ": " + s.getAmount().toString());

            }
        }

        try {
            Thread.sleep(500); //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        System.out.println(".");
        System.out.println("[SACH]");
        System.out.println("Sending SWIFT code to delegating bank..:" + delegatingBank);
        System.out.println(swiftMessage);

        //TODO send to delegating bank swift code if necessary 
        MessageDTO err = new MessageDTO();
        err.setCode(0);
        err.setMessage("SUCCESS");
        return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
    }
}
