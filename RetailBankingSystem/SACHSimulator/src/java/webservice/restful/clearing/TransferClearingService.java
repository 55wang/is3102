/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.clearing;

import ejb.session.bean.SACHSessionBean;
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
            @FormParam("bankCode") String bankCode,
            @FormParam("branchCode") String branchCode,
            @FormParam("accountNumber") String accountNumber,
            @FormParam("toName") String toName,
            @FormParam("fromName") String fromName,
            @FormParam("myInitial") String myInitial
    ) {
        System.out.println("Received referenceNumber:" + referenceNumber);
        System.out.println("Received amount:" + amount);
        System.out.println("Received bankCode:" + bankCode);
        System.out.println("Received branchCode:" + branchCode);
        System.out.println("Received accountNumber:" + accountNumber);
        System.out.println("Received toName:" + toName);
        System.out.println("Received fromName:" + fromName);
        System.out.println("Received myInitial:" + myInitial);
        System.out.println("Received POST http SACH_transfer_clearing");
        System.out.println("SACH Verifies credit limits, adjusts accounts internally");
        // at this point, Clear and save all to db before give a end of day settlement amount
        PaymentTransfer pt = new PaymentTransfer();
        pt.setReferenceNumber(referenceNumber);
        pt.setAmount(new BigDecimal(amount));
        pt.setBankCode(bankCode);
        pt.setBranchCode(branchCode);
        pt.setAccountNumber(accountNumber);
        pt.setToName(toName);
        pt.setFromName(fromName);
        pt.setMyInitial(myInitial);
        sachBean.persist(pt);

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
