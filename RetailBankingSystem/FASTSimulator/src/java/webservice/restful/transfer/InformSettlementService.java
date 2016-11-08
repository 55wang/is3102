/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.transfer;

import ejb.session.bean.FASTSessionBean;
import entity.FastTransfer;
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
@Path("fast_inform_settlement")
public class InformSettlementService {
    @EJB
    private FASTSessionBean fastBean;
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response receiveSettlement(
            @FormParam("netSettlementAmount") String netSettlementAmount,
            @FormParam("referenceNumber") String referenceNumber,
            @FormParam("fromBankCode") String fromBankCode,
            @FormParam("toBankCode") String toBankCode
    ) {
        System.out.println("Received netSettlementAmount:" + netSettlementAmount);
        System.out.println("Received referenceNumber:" + referenceNumber);
        System.out.println("Received fromBankCode:" + fromBankCode);
        System.out.println("Received toBankCode:" + toBankCode);
        System.out.println("Received POST http fast_inform_settlement");
        
        // makes payment to other bank
        System.out.println("Paying Settlement to other bank" + toBankCode);
//        fastBean.sendMEPS(netSettlementAmount, toBankCode, referenceNumber);
        
        System.out.println("Sending back fast_inform_settlement response");
        MessageDTO err = new MessageDTO();
        err.setCode(0);
        err.setMessage("SUCCESS");
        return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
    }
}
