/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.clearing;

import ejb.session.bean.SACHSessionBean;
import entity.BillTransfer;
import entity.PaymentTransfer;
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
@Path("sach_inform_settlement")
public class InformSettlementService {
    
    @EJB
    private SACHSessionBean sachBean;
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response receiveSettlement(
            @FormParam("netSettlementAmount") String netSettlementAmount,
            @FormParam("fromBankCode") String fromBankCode,
            @FormParam("toBankCode") String toBankCode
    ) {
        System.out.println("Received netSettlementAmount:" + netSettlementAmount);
        System.out.println("Received POST http sach_inform_settlement");
        
        // makes payment to other bank
        // TODO: if needed, use a MAP to check net settlement to other banks
        System.out.println("Paying Settlement to other bank" + toBankCode);
        sachBean.sendMEPS(netSettlementAmount, toBankCode);
        
        System.out.println("Sending back sach_inform_settlement response");
        List<PaymentTransfer> paymentTransfers = sachBean.findAllPaymentTransferForBankCode("001");
        for (PaymentTransfer pt : paymentTransfers) {
            pt.setSettled(Boolean.TRUE);
            System.out.println(pt.toString());
            sachBean.merge(pt);
        }
        List<BillTransfer> billTransfers = sachBean.findAllBillTransferForBankCode("001");
        for (BillTransfer bt : billTransfers) {
            bt.setSettled(Boolean.TRUE);
            System.out.println(bt.toString());
            sachBean.merge(bt);
        }
        MessageDTO err = new MessageDTO();
        err.setCode(0);
        err.setMessage("SUCCESS");
        return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
    }
}
