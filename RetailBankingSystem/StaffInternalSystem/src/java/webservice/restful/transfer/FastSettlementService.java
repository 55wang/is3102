/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.transfer;

import ejb.session.bill.BillSessionBeanLocal;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import webservice.restful.mobile.ErrorDTO;

/**
 *
 * @author leiyang
 */
@Path("fast_settlement")
public class FastSettlementService {

    @EJB
    private BillSessionBeanLocal billSessionBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response netSettlement(
            @FormParam("toBankCode") String toBankCode,
            @FormParam("toBankName") String toBankName,
            @FormParam("amount") String netSettlementAmount,
            @FormParam("referenceNumber") String referenceNumber
    ) {

        List<String> mbsTransactions = new ArrayList<>();
        mbsTransactions.add(referenceNumber);

        System.out.println(".");
        System.out.println("[MBS]");
        System.out.println("Received Net Settlement Broadcast from MEPS:");

        System.out.println(".       Net Settlement to " + toBankCode + " " + toBankName + ": " + new BigDecimal(netSettlementAmount).setScale(4).toString());

        System.out.println("Settled Transactions:");
        for (String tr : mbsTransactions) {
            System.out.println(".       ID: " + tr);
        }

        billSessionBean.updateTransactionStatusSettled(mbsTransactions);
        System.out.println("Transactions status updated...");


        // requesting to MEPS

        System.out.println(".");
        System.out.println("Sending back net_settlement response...");
        ErrorDTO err = new ErrorDTO();
        err.setCode(0);
        err.setError("SUCCESS");
        return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
    }


}
