/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.creditcard;

import SMSMessaging.SendTextMessage;
import ejb.session.bill.BillSessionBeanLocal;
import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.card.CardTransactionSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBean;
import ejb.session.common.EmailServiceSessionBeanLocal;
import entity.bill.BillFundTransferRecord;
import entity.card.account.CreditCardAccount;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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

    BillSessionBeanLocal billSessionBean = lookupBillSessionBeanLocal();

    @Context
    private UriInfo context;

    @EJB
    private CardAcctSessionBeanLocal ccBean;
    @EJB
    private CardTransactionSessionBeanLocal cardTransactionSessionBean;
    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;

    public CreditCardAuthorizationService() {
        System.out.println("CreditCardService");
    }

    private Boolean checkAbnormalAction(String ccAmount) {
        System.out.println("Checking abnormal transaction...");

        Double amount = Double.parseDouble(ccAmount);
        return amount > 100000;
    }

    // check authorization, check creditcard, check valiation
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authorizeCC(
            @FormParam("ccNumber") String ccNumber,
            @FormParam("ccAmount") String ccAmount,
            @FormParam("referenceNum") String referenceNum,
            @FormParam("fromBankCode") String fromBankCode,
            @FormParam("ccTcode") String ccTcode,
            @FormParam("ccDescription") String ccDescription
    ) {

        // get value from form
        System.out.println(". ");
        System.out.println("[MBS]");
        System.out.println("Received transaction request from VISA:");

        System.out.println(".        Reference Num: " + referenceNum);
        System.out.println(".        Credit Card number: " + ccNumber);
        System.out.println(".        Transaction Amount: " + ccAmount);
        System.out.println(".        Fund to Bank Code: " + fromBankCode);
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
        boolean validDailyStatus = false;
        boolean validMonthlyStatus = false;
        boolean validCreditLimit = false;

        if (!checkAbnormalAction(ccAmount)) {
            CreditCardAccount thisAccount = null;
            try {
                thisAccount = ccBean.getCreditCardAccountByCardNumber(ccNumber);
            } catch (Exception e) {
                System.out.println("No account retrieved");
            }

            if (thisAccount != null) {
                System.out.println("Validating daily transaction limit...");

                try {
                    validDailyStatus = cardTransactionSessionBean.validateCreditCardDailyTransactionLimit(thisAccount, Double.parseDouble(ccAmount));
                } catch (Exception e) {
                    System.out.println("Exceed daily transaction limit!");
                }
                if (validDailyStatus == false) {
                    System.out.println("Exceed daily transaction limit!");
                    authorized = false;
                    c.setMessage("Exceed daily transaction limit!");
                }

                try {
                    validMonthlyStatus = cardTransactionSessionBean.validateCreditCardMonthlyTransactionLimit(thisAccount, Double.parseDouble(ccAmount));
                } catch (Exception e) {
                    System.out.println("Exceed monthly transaction limit!");
                }
                if (validMonthlyStatus == false) {
                    System.out.println("Exceed monthly transaction limit!");
                    authorized = false;
                    c.setMessage("Exceed monthly transaction limit!");
                }

                try {
                    validCreditLimit = cardTransactionSessionBean.validateCreditLimit(thisAccount, Double.parseDouble(ccAmount));
                } catch (Exception e) {
                    System.out.println("Exceed credit limit!");
                }
                if (validCreditLimit == false) {
                    System.out.println("Exceed credit limit!");
                    authorized = false;
                    c.setMessage("Exceed credit limit!");
                }
            } else {
                authorized = false;
                System.out.println("No account retrieved");
                c.setMessage("No account retrieved!");
            }

            if (authorized) {
                code = PincodeGenerationUtils.generateRandom(true, 8);
                c.setMessage("Authorized");

                BillFundTransferRecord bft = new BillFundTransferRecord();
                bft.setReferenceNumber(referenceNum);
                bft.setBillReferenceNumber(ccNumber);
                bft.setFromBankCode("001");
                bft.setToBankCode(fromBankCode);
                bft.setAmount(new BigDecimal(ccAmount));
                bft.setSettled(Boolean.FALSE);
                bft.setCreationDate(new Date());

                billSessionBean.createBillFundTransferRecord(bft);

            }
            c.setAuthorizationCode(code);
        } else {
            c.setAuthorizationCode("-2");
            c.setMessage("Not Authorized!");
            // TODO: Send Notification message

            CreditCardAccount cca = ccBean.getCreditCardAccountByCardNumber(ccNumber);
            String phoneNumber = cca.getMainAccount().getCustomer().getPhone();
            System.out.println("######## " + phoneNumber + " #######");
            Calendar currentDate = Calendar.getInstance();
            SimpleDateFormat dateOnly = new SimpleDateFormat("dd/MM/yyyy");

            String lastFourDigit = StringUtils.substring(ccNumber, ccNumber.length() - 4);
            System.out.println("print last 4 digit: " + lastFourDigit);
            String msg = "Card Transaction of SGD " + ccAmount + " was performed on your MBS account ending with "
                    + lastFourDigit + " on " + dateOnly.format(currentDate.getTime()) + ". If unauthorised, pls call "
                    + "1800 222 2313.";
            emailServiceSessionBean.sendEmailUnauthorised(cca.getMainAccount().getCustomer().getEmail(), msg);
//            SendNEXONMessage.sendText(phoneNumber, msg);
//            SendTextMessage.sendText(phoneNumber, msg);

        }

        System.out.println("Sending back result with single code: " + c.getAuthorizationCode());

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

    private BillSessionBeanLocal lookupBillSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (BillSessionBeanLocal) c.lookup("java:global/RetailBankingSystem/RetailBankingSystem-ejb/BillSessionBean!ejb.session.bill.BillSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
