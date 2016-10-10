/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.clearing;

import ejb.session.bean.SACHSessionBean;
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
            @FormParam("netSettlementAmount") String netSettlementAmount
    ) {
        System.out.println("Received netSettlementAmount:" + netSettlementAmount);
        System.out.println("Received POST http sach_inform_settlement");
        
        // makes payment to other bank
        System.out.println("Paying Settlement");
        sachBean.sendMEPS(netSettlementAmount);
        
        System.out.println("Sending back sach_inform_settlement response");
        MessageDTO err = new MessageDTO();
        err.setCode(0);
        err.setMessage("SUCCESS");
        return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
    }
    
    
}
