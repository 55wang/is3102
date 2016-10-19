/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.transfer;

import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.dams.account.DepositAccount;
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
 * @author leiyang
 */
@Path("mbs_receive_transfer_payment")
public class ReceiveTransferPayment {

    @EJB
    private CustomerDepositSessionBeanLocal depositBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response netSettlement(
            @FormParam("referenceNumber") String referenceNumber,
            @FormParam("amount") String amount,
            @FormParam("accountNumber") String accountNumber,
            @FormParam("toName") String toName,
            @FormParam("fromName") String fromName,
            @FormParam("myInitial") String myInitial
    ) {

        System.out.println("Received referenceNumber:" + referenceNumber);
        System.out.println("Received amount:" + amount);
        System.out.println("Received accountNumber:" + accountNumber);
        System.out.println("Received toName:" + toName);
        System.out.println("Received fromName:" + fromName);
        System.out.println("Received myInitial:" + myInitial);
        System.out.println("Received POST http mbs_receive_transfer_payment");

        DepositAccount da = depositBean.getAccountFromId(accountNumber);
        if (da == null) {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("Account Not Found");
            return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
        } else {
            System.out.println("Sending back mbs_receive_transfer_payment response");
            depositBean.transferToAccount(da, new BigDecimal(amount));
            ErrorDTO err = new ErrorDTO();
            err.setCode(0);
            err.setError("SUCCESS");
            return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
        }
    }
}
