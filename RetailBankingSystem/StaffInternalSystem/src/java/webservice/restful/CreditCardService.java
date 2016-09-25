/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful;

import ejb.session.card.CardAcctSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.primefaces.json.JSONObject;
import server.utilities.PincodeGenerationUtils;

/**
 *
 * @author leiyang
 */
@Path("credit_card_authorization")
public class CreditCardService {
    
    @Context
    private UriInfo context;

    @EJB
    private CardAcctSessionBeanLocal ccBean;
    
    public CreditCardService() {
        System.out.println("CreditCardService");
    }

    // check authorization, check creditcard, check valiation
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authorizeCC (
            @FormParam("ccNumber") String ccNumber,
            @FormParam("ccAmount") String ccAmount,
            @FormParam("ccTcode") String ccTcode,
            @FormParam("ccDescription") String ccDescription
            ) {
        // get value from form
        System.out.println("Getting String list with cc number: " + ccNumber);
        // return value
        Boolean authorized = true;
        // validate
        // if fail, code = -1;
        String code = "-1";
        // after it is done
        CreditCardDTO c = new CreditCardDTO();
        c.setAmount(ccAmount);
        c.setDescription(ccDescription);
        c.setCreditCardNumber(ccNumber);
        c.settCode(ccTcode);
        
        CreditCardAccount thisAccount = ccBean.getCardByCardNumber(ccNumber);
        if (thisAccount != null) {
            thisAccount = ccBean.validateCreditCardDailyTransactionLimit(thisAccount, Double.parseDouble(ccAmount));
        }
        if (thisAccount == null) {
            authorized = false;
            c.setErrorMessage("Exceed daily transaction limit!");
        } else {
            thisAccount = ccBean.validateCreditCardMonthlyTransactionLimit(thisAccount, Double.parseDouble(ccAmount));
        }
        
        if (thisAccount == null) {
            authorized = false;
            c.setErrorMessage("Exceed monthly transaction limit!");
        }
        
        if (authorized) {
            code = PincodeGenerationUtils.generateRandom(true, 8);
            c.setaCode(code);
        }
        
        String jsonString = new JSONObject(c).toString();
        return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON) 
    public JsonArray getStringList(@QueryParam("accountNumber") String accountNumber) {
        System.out.println("Getting String list with account number:" + accountNumber);
        JsonArrayBuilder arrayBld = Json.createArrayBuilder();
        List<String> strList = new ArrayList<>();
        strList.add("test 1");
        strList.add("test 2");
        strList.add("test 3");
        strList.add("test 4");
        strList.add("test 5");
        for (String str : strList) {
            arrayBld.add(str);
        }
        
        return arrayBld.build();
    }
}
