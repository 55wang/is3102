/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.clearing;

import ejb.session.bean.SACHSessionBean;
import entity.BillTransfer;
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
@Path("sach_billing_clearing")
public class BillingClearingService {

    @EJB
    private SACHSessionBean sachBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response transferClearing(
            @FormParam("referenceNumber") String referenceNumber,
            @FormParam("amount") String amount,
            @FormParam("partnerBankCode") String partnerBankCode,
            @FormParam("partnerBankAccount") String partnerBankAccount,
            @FormParam("shortCode") String shortCode,
            @FormParam("fromBankCode") String fromBankCode,
            @FormParam("organizationName") String organizationName,
            @FormParam("billReferenceNumber") String billReferenceNumber
    ) {
        System.out.println(".");
        System.out.println("[SACH]");
        System.out.println("Received payment instruction from MBS:");
        System.out.println(".      Received referenceNumber:" + referenceNumber);
        System.out.println(".      Received amount:" + amount);
        System.out.println(".      Received partnerBankCode:" + partnerBankCode);
        System.out.println(".      Received partnerBankAccount:" + partnerBankAccount);
        System.out.println(".      Received shortCode:" + shortCode);
        System.out.println(".      Received fromBankCode:" + fromBankCode);
        System.out.println(".      Received organizationName:" + organizationName);
        System.out.println(".      Received billReferenceNumber:" + billReferenceNumber);
        System.out.println(".      Received POST http SACH_billing_clearing");
        // at this point, Clear and save all to db before give a end of day settlement amount
        BillTransfer bt = new BillTransfer();
        Boolean hasPrevious = false;
        try {
            bt = sachBean.findBillTransferByReferenceNumber(referenceNumber);
            hasPrevious = true;
        } catch (Exception e) {
            System.out.println("No Previous BillTransfer found");
        }

        if (bt == null) {
            bt = new BillTransfer();
        }

        bt.setReferenceNumber(referenceNumber);
        bt.setAmount(new BigDecimal(amount));
        bt.setPartnerBankCode(partnerBankCode);
        bt.setPartnerBankAccount(partnerBankAccount);
        bt.setShortCode(shortCode);
        bt.setFromBankCode(fromBankCode);
        bt.setOrganizationName(organizationName);
        bt.setBillReferenceNumber(billReferenceNumber);
        bt.setSettled(false);
        if (hasPrevious) {
            sachBean.merge(bt);
        } else {
            sachBean.persist(bt);
        }

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
        sachBean.updateNetSettlementAddBill(bt);

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
