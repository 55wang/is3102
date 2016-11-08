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
 * @author leiyang
 */
@Path("sach_transfer_clearing")
public class TransferClearingService {

    @EJB
    private SACHSessionBean sachBean;

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
        System.out.println(".");
        System.out.println("[SACH]");
        System.out.println("Received IBG Payment Instruction from MBS:");
        System.out.println(".      Transaction Number:" + referenceNumber);
        System.out.println(".      Payment Amount: $" + amount);
        System.out.println(".      To Bank Code:" + toBankCode);
        System.out.println(".      To Branch Code:" + toBranchCode);
        System.out.println(".      To Bank Account:" + accountNumber);
        System.out.println(".      Receiver's Name:" + toName);
        System.out.println(".      From Bank Code:" + fromBankCode);
        System.out.println(".      Sender's Name:" + fromName);
        System.out.println(".      Sender's Initial:" + myInitial);
        System.out.println("Received POST http SACH_transfer_clearing");
        // at this point, Clear and save all to db before give a end of day settlement amount
        PaymentTransfer pt = new PaymentTransfer();
        pt.setReferenceNumber(referenceNumber);
        pt.setAmount(new BigDecimal(amount));
        pt.setToBankCode(toBankCode);
        pt.setToBranchCode(toBranchCode);
        pt.setFromBankCode(fromBankCode);
        pt.setAccountNumber(accountNumber);
        pt.setToName(toName);
        pt.setFromName(fromName);
        pt.setMyInitial(myInitial);
        pt.setSettled(false);
        sachBean.persist(pt);
        List<SachSettlement> bankAccounts = sachBean.getSettlements();

        System.out.println("Current net settlement:");
        for (SachSettlement s : bankAccounts) {
            if (s.getAmount().compareTo(BigDecimal.ZERO) == -1) {
                System.out.println(".       " + s.getToBankCode() + " " + s.getToBankName() + " to " + s.getFromBankCode() + " " + s.getFromBankName() + ": " + s.getAmount().setScale(4).toString());
            } else {
                System.out.println(".       " + s.getFromBankCode() + " " + s.getFromBankName() + " to " + s.getToBankCode() + " " + s.getToBankName() + ": " + s.getAmount().setScale(4).toString());

            }
        }

        System.out.println("Updating net settlement...");
        sachBean.updateNetSettlement(pt);

        List<SachSettlement> updatedbankAccounts = sachBean.getSettlements();

        System.out.println("Updated net settlement:");
        for (SachSettlement s : updatedbankAccounts) {
            if (s.getAmount().compareTo(BigDecimal.ZERO) == -1) {
                System.out.println(".       " + s.getToBankCode() + " " + s.getToBankName() + " to " + s.getFromBankCode() + " " + s.getFromBankName() + ": " + s.getAmount().setScale(4).toString());
            } else {
                System.out.println(".       " + s.getFromBankCode() + " " + s.getFromBankName() + " to " + s.getToBankCode() + " " + s.getToBankName() + ": " + s.getAmount().setScale(4).toString());

            }
        }

        System.out.println("Sending back SACH_transfer_clearing response");
        MessageDTO message = new MessageDTO();
        message.setCode(0);
        message.setMessage("SUCCESS");
        return Response.ok(new JSONObject(message).toString(), MediaType.APPLICATION_JSON).build();
    }
}
