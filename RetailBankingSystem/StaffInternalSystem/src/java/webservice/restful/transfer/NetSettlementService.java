/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.transfer;

import javax.ejb.EJB;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.primefaces.json.JSONObject;
import webservice.restful.mobile.ErrorDTO;

/**
 *
 * @author leiyang
 */
@Path("net_settlement")
public class NetSettlementService {
    
    @EJB
    private SettlementSessionBean settleBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response netSettlement(
            @FormParam("netSettlementAmount") String netSettlementAmount
    ) {
        System.out.println("Received netSettlementAmount:" + netSettlementAmount);
        System.out.println("Received POST http net_settlement");

        // requesting to MEPS
        // only one case, when it is true
        // TODO: Failed cases
        settleBean.paySettlement(netSettlementAmount);
        
        System.out.println("Sending back net_settlement response");
        ErrorDTO err = new ErrorDTO();
        err.setCode(0);
        err.setError("SUCCESS");
        return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
    }
    
    
}
