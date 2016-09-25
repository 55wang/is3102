/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful;

import SMSMessaging.SendTextMessage;
import ejb.session.card.CardAcctSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.apache.commons.lang.StringUtils;
import org.primefaces.json.JSONObject;
import server.utilities.PincodeGenerationUtils;

/**
 *
 * @author leiyang
 */
@Path("credit_card_authorization")
public class CreditCardAuthorizationService {

    @Context
    private UriInfo context;

    @EJB
    private CardAcctSessionBeanLocal ccBean;

    public CreditCardAuthorizationService() {
        System.out.println("CreditCardService");
    }

    private Boolean checkAbnormalAction(String ccAmount) {
        System.out.println(ccAmount);
        Double amount = Double.parseDouble(ccAmount);
        System.out.println(amount > 1000);
        return amount > 1000;
    }

    // check authorization, check creditcard, check valiation
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authorizeCC(
            @FormParam("ccNumber") String ccNumber,
            @FormParam("ccAmount") String ccAmount,
            @FormParam("ccTcode") String ccTcode,
            @FormParam("ccDescription") String ccDescription
    ) {

        // get value from form
        System.out.println("Authorizing with cc number: " + ccNumber);
        System.out.println("Authorizing with ccAmount: " + ccAmount);
        System.out.println("Authorizing with ccTcode: " + ccTcode);
        System.out.println("Authorizing with ccDescription: " + ccDescription);
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
        c.setTransactionCode(ccTcode);

        if (!checkAbnormalAction(ccAmount)) {
            System.out.println("Retrieving card: " + ccNumber);
            CreditCardAccount thisAccount = null;
            try {
                thisAccount = ccBean.getCardByCardNumber(ccNumber);
            } catch (Exception e) {
                System.out.println("No account retrieved");
            }

            System.out.println(thisAccount);
            if (thisAccount != null) {
                System.out.println("Validateing daily transaction limit");
                try {
                    thisAccount = ccBean.validateCreditCardDailyTransactionLimit(thisAccount, Double.parseDouble(ccAmount));
                } catch (Exception e) {
                    System.out.println("Exceed daily transaction");
                }
            }
            if (thisAccount == null) {
                authorized = false;
                c.setMessage("Exceed daily transaction limit!");
            } else {
                try {
                    thisAccount = ccBean.validateCreditCardMonthlyTransactionLimit(thisAccount, Double.parseDouble(ccAmount));
                } catch (Exception e) {
                    System.out.println("Exceed monthly transaction");
                }
                if (thisAccount == null) {
                    c.setMessage("Exceed monthly transaction limit!");
                }
            }

            if (thisAccount == null) {
                authorized = false;
            }

            if (authorized) {
                code = PincodeGenerationUtils.generateRandom(true, 8);
                c.setMessage("Authorized");
            }
            c.setAuthorizationCode(code);
        } else {
            c.setAuthorizationCode("-2");
            c.setMessage("Not Authorized!");
            // TODO: Send Notification message

            CreditCardAccount cca = ccBean.getCardByCardNumber(ccNumber);
            String phoneNumber = cca.getMainAccount().getCustomer().getPhone();
            System.out.println("######## "+phoneNumber+" #######");
            Calendar currentDate = Calendar.getInstance();
            SimpleDateFormat dateOnly = new SimpleDateFormat("dd/MM/yyyy");

            String lastFourDigit = StringUtils.substring(ccNumber, ccNumber.length() - 4);
            System.out.println("print last 4 digit: " + lastFourDigit);
            String msg = "Card Transaction of SGD " + ccAmount + " was performed on your MBS account ending with "
                    + lastFourDigit + " on " + dateOnly.format(currentDate.getTime()) + ". If unauthorised, pls call "
                    + "1800 222 2313.";
            SendTextMessage.sendText(phoneNumber, msg);

        }

        System.out.println("Sending back result with single code: " + c.getAuthorizationCode());

        String jsonString = new JSONObject(c).toString();
        System.out.println(jsonString);
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
