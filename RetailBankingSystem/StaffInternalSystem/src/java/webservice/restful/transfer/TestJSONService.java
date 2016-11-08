/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.transfer;

import ejb.session.webservice.WebserviceSessionBeanLocal;
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
import webservice.restful.mobile.ErrorDTO;

/**
 *
 * @author qiuxiaqing
 */
@Path("test_json")
public class TestJSONService {
    
    @EJB
    private WebserviceSessionBeanLocal webserviceBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response netSettlement(
            TransactionSummaryDTO transactionSummary
    ) {
        System.out.println(transactionSummary.getTransactionSummary().get(0).getReferenceNumber());
        System.out.println("Sending back net_settlement response...");
        ErrorDTO err = new ErrorDTO();
        err.setCode(0);
        err.setError("SUCCESS");
        return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
    }
    
}
