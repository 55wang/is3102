/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.transfer;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.card.account.CreditCardAccount;
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
@Path("mbs_receive_cc_payment")
public class ReceiveCCPayment {
    
    @EJB
    private CardAcctSessionBeanLocal cardBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response netSettlement(
            @FormParam("ccNumber") String ccNumber,
            @FormParam("ccAmount") String ccAmount,
            @FormParam("referenceNumber") String referenceNumber
    ) {

        System.out.println("Received ccNumber:" + ccNumber);
        System.out.println("Received ccAmount:" + ccAmount);
        System.out.println("Received referenceNumber:" + referenceNumber);
        System.out.println("Received POST http mbs_receive_cc_payment");

        CreditCardAccount cca = cardBean.getCreditCardAccountByCardNumber(ccNumber);
        if (cca == null) {
            ErrorDTO err = new ErrorDTO();
            err.setCode(0);
            err.setError("Credit Card Account Not Found");
            return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
        } else {
            System.out.println("Sending back mbs_receive_cc_payment response");
            cardBean.payCreditCardAccountBillByCardNumber(ccNumber, new BigDecimal(ccAmount));
            ErrorDTO err = new ErrorDTO();
            err.setCode(0);
            err.setError("SUCCESS");
            return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
        }

    }

}
