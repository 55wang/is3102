/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.token.TokenSecurityLocal;
import entity.card.account.CardTransaction;
import entity.card.account.CreditCardAccount;
import java.util.Date;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.primefaces.json.JSONObject;
import server.utilities.EnumUtils;

/**
 *
 * @author leiyang
 */
@Path("credit_card_clearing")
public class CreditCardClearingService {
    
    @Context
    private UriInfo context;

    @EJB
    private CardAcctSessionBeanLocal ccBean;
    @EJB
    private TokenSecurityLocal tokenBean;
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response clearCC (
            @FormParam("token") String token,
            @FormParam("ccNumber") String ccNumber,
            @FormParam("ccAmount") String ccAmount,
            @FormParam("ccTcode") String ccTcode,
            @FormParam("aCode") String aCode,
            @FormParam("ccDescription") String ccDescription
            ) {
        
        Boolean authorized = tokenBean.verifyTokens(token, ccNumber, Integer.valueOf(aCode));
        System.out.println("Authorized = " + authorized);
        
        CreditCardDTO c = new CreditCardDTO();
        c.setAmount(ccAmount);
        c.setDescription(ccDescription);
        c.setCreditCardNumber(ccNumber);
        c.setTransactionCode(ccTcode);
        c.setAuthorizationCode("-2");
        if (authorized && creditTransaction(c)) {
            c.setMessage("Transaction success!");
        } else {
            c.setMessage("Transaction failed!");
        }
        String jsonString = new JSONObject(c).toString();
        return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
    }
    
    private Boolean creditTransaction(CreditCardDTO c) {
        CardTransaction ct = new CardTransaction();
        ct.setUpdateDate(new Date());
        ct.setCardTransactionStatus(EnumUtils.CardTransactionStatus.PENDINGTRANSACTION);
        ct.setAmount(Double.parseDouble(c.getAmount()));
        ct.setIsCredit(true);
        ct.setTransactionCode(c.getTransactionCode());
        ct.setTransactionDescription(c.getDescription());
        ct = ccBean.createCardAccountTransaction(c.getCreditCardNumber(), ct);
        return ct != null;
    } 
}
