/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.clearing;

import ejb.session.bean.SACHSessionBean;
import entity.BillTransfer;
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
            @FormParam("shortCode") String shortCode,
            @FormParam("organizationName") String organizationName,
            @FormParam("billReferenceNumber") String billReferenceNumber
    ) {
        System.out.println("Received referenceNumber:" + referenceNumber);
        System.out.println("Received amount:" + amount);
        System.out.println("Received partnerBankCode:" + partnerBankCode);
        System.out.println("Received shortCode:" + shortCode);
        System.out.println("Received organizationName:" + organizationName);
        System.out.println("Received billReferenceNumber:" + billReferenceNumber);
        System.out.println("Received POST http SACH_billing_clearing");
        System.out.println("SACH Verifies credit limits, adjusts accounts internally");
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
        bt.setShortCode(shortCode);
        bt.setOrganizationName(organizationName);
        bt.setBillReferenceNumber(billReferenceNumber);
        bt.setSettled(false);
        if (hasPrevious) {
            sachBean.merge(bt);
        } else {
            sachBean.persist(bt);
        }
        

        System.out.println("At 4:30, SACH tells MBS how much to pay via MEPS");
        System.out.println("By 5:30, MBS must pay");
        System.out.println("MEPS Moves $$ into SACH Account");
        System.out.println("By 5.45, SACH Makes payment to other bank");
        System.out.println("MEPS debits SACH account, credit other bank account");
        System.out.println("SACH advises other bank account of credit amount");
        System.out.println("Sending back SACH_transfer_clearing response");
        
        MessageDTO message = new MessageDTO();
        message.setCode(0);
        message.setMessage("SUCCESS");
        return Response.ok(new JSONObject(message).toString(), MediaType.APPLICATION_JSON).build();
    }
}
