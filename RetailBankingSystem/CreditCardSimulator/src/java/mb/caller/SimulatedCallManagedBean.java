/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mb.caller;

import ejb.session.card.CardTransactionSessionBeanLocal;
import entity.VisaCardTransaction;
import init.CardEntityBuilderBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.smartcardio.CardChannel;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import utils.NfcDevice;

/**
 *
 * @author leiyang
 */
@Named(value = "simulatedCallManagedBean")
@ViewScoped
public class SimulatedCallManagedBean implements Serializable {
    
    @EJB
    CardEntityBuilderBean cardEntityBuilderBean;
    @EJB
    CardTransactionSessionBeanLocal cardTransactionSessionBean;

    private String name = "Credit Card Simulator - Send Card Transaction";
    private String transactionAmount;
    private String creditCardNumber;
    private String referenceNum;
    private String requestBankCode;
    private final String AUTHORIZATION_PATH = "https://localhost:8181/StaffInternalSystem/rest/credit_card_authorization";
    private final String CLEARING_PATH = "https://localhost:8181/StaffInternalSystem/rest/credit_card_clearing";
    private final String SETTLEMENT_PATH = "https://localhost:8181/StaffInternalSystem/rest/credit_card_settlement";

    private List<VisaCardTransaction> vcts;
    
    /**
     * Creates a new instance of SimulatedCallManagedBean
     */
    public SimulatedCallManagedBean() {
    }
    

    @PostConstruct
    public void init() {
        System.out.println("SimulatedCallManagedBean @PostConstruct");
        cardEntityBuilderBean.init();
        setVcts(cardTransactionSessionBean.getListVisaCardTransactions());
    }

    public void sendEODSettlement() {
        //get transaction id list
        List<Long> listIds = cardTransactionSessionBean.getListVisaCardTransactionIdsByStatus(Boolean.FALSE);
        System.out.println(listIds);
        //get netAmount
        Double netAmount;
        try {
            netAmount = cardTransactionSessionBean.calculateNetSettlement();
        } catch (Exception ex) {
            netAmount = 0.0;
        }

        System.out.println(netAmount);

        Form form = new Form(); //bank info
        form.param("swiftCode", "DBSSSGSG");
        form.param("bankCode", "7171"); //iso 13616
        form.param("branchCode", "001");
        form.param("netAmount", Double.toString(netAmount));
        form.param("visaIds", listIds.toString());

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getSETTLEMENT_PATH());

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        System.out.println(jsonString);

        if (jsonString.getString("SETTLEMENT").equals("SUCCESS")) {
            for (Long Id : listIds) {
                cardTransactionSessionBean.updateVisaCardTransactionSettledStatusById(Id);
            }
            System.out.println("SETTLEMENT COMPLETED");
        } else if (jsonString.getString("SETTLEMENT").equals("FAIL")) {
            System.out.println("SETTLEMENT FAIL");
        }

        /*CreditCardDTO cc = new CreditCardDTO(jsonString);

         if (cc.getAuthorizationCode() == null || cc.getAuthorizationCode().isEmpty()) {
         System.out.println(cc.getMessage());
         }
         */
    }

    public void sendMEPSSettlement() {
        cardTransactionSessionBean.sendMEPSNetSettlement();
    }

    public void sendFailedDailyAuhorization() {
        String ccNum = readCardCCNumber();

        if (ccNum != null) {

            Form form = new Form();
            form.param("ccNumber", ccNum);
            form.param("ccAmount", "600"); // 500 is daily limit and 1000 for monthly limist, current out standing is 800
            form.param("ccTcode", "MDS");
            form.param("ccDescription", "Test failed daily authorization");

            System.out.println("Calling bank to check credit card transaction");
            System.out.println("Credit Card number " + ccNum);
            System.out.println("Requesting amount 600");
            // Start calling
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(getAUTHORIZATION_PATH());

            // This is the response
            JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
            CreditCardDTO cc = new CreditCardDTO(jsonString);

            if (cc.getAuthorizationCode() == null || cc.getAuthorizationCode().isEmpty()) {
                System.out.println(cc.getMessage());
            }
        } else {
            System.out.println("Fail to read Credit Card card values");
        }
    }

    public void sendFailedMonthlyAuhorization() {
        String ccNum = readCardCCNumber();

        if (ccNum != null) {
            Form form = new Form();
            form.param("ccNumber", ccNum);
            form.param("ccAmount", "1400"); // 500 is daily limit and 1000 for monthly limist, current out standing is 800
            form.param("ccTcode", "MDS");
            form.param("ccDescription", "Test failed monthly authorization");

            System.out.println("Calling bank to check credit card transaction");
            System.out.println("Credit Card number " + ccNum);
            System.out.println("Requesting amount 1400");

            // Start calling
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(getAUTHORIZATION_PATH());

            // This is the response
            JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
            CreditCardDTO cc = new CreditCardDTO(jsonString);

            if (cc.getAuthorizationCode() == null || cc.getAuthorizationCode().isEmpty()) {
                System.out.println(cc.getMessage());
            }
        } else {
            System.out.println("Fail to read Credit Card card values");
        }
    }

    public void sendSuccessAuthorization() {
        cardTransactionSessionBean.sendSuccessAuthorization(getTransactionAmount(), getCreditCardNumber(), getReferenceNum(), getRequestBankCode());
    }

    private void sendSuccessClearing(String returnCode, String aCode) {
        String ccNum = readCardCCNumber();

        if (ccNum != null) {

            Form form = new Form();
            form.param("token", returnCode);
            form.param("aCode", aCode);
            form.param("ccNumber", ccNum);
            form.param("ccAmount", getTransactionAmount()); // 500 is daily limit and 1000 for monthly limist, current out standing is 800
            form.param("ccTcode", "MDS");
            form.param("ccDescription", "Test Success Clearing");

            System.out.println("Calling bank to check credit card transaction");
            System.out.println("Credit Card number " + ccNum);
            System.out.println("Requesting amount " + getTransactionAmount());
            System.out.println("Requesting with token:" + returnCode);

            // Start calling
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(getCLEARING_PATH());

            // This is the response
            JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
            CreditCardDTO cc = new CreditCardDTO(jsonString);
            System.out.println(cc.getMessage());
        } else {
            System.out.println("Fail to read Credit Card card values");
        }
    }

    //
    public void initiating() {
        System.out.println("Calling web services");
        testGetMethod();
    }

    private void testGetMethod() {
        List<String> allString = new ArrayList<>();
        Client client = ClientBuilder.newClient();

        WebTarget target = client.target(getAUTHORIZATION_PATH() + "?accountNumber=123456789");// Mapped by @QueryParam("accountNumber") 

        // @Get request
        JsonArray response = target.request(MediaType.APPLICATION_JSON).get(JsonArray.class);
        for (JsonValue str : response) {
            allString.add(((JsonString) str).getString());
        }
        System.out.println(allString);
    }

    private void testPostMethod() {
        List<String> allString = new ArrayList<>();
        // This will be get from @FormParam("accountNumber")
        Form form = new Form();
        form.param("accountNumber", "987654321");

        // Start calling
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(getAUTHORIZATION_PATH());

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        System.out.println(jsonString);
        System.out.println("name: " + jsonString.getString("name"));
        System.out.println("creditCardNumber: " + jsonString.getString("creditCardNumber"));
        System.out.println("amount: " + jsonString.getString("amount"));

        // Do necessary process
    }

    /**
     * @return the transactionAmount
     */
    public String getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * @param transactionAmount the transactionAmount to set
     */
    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String readCardCCNumber() {
        System.out.println("*** Start nfc device ***");
        CardChannel channel = NfcDevice.initializeDevice();
        try {
            return NfcDevice.readCard(channel);
        } catch (Exception ex) {
            System.out.println("readCardCCNumber Error");
            System.out.println(ex);
            return null;
        }
    }

    public List<VisaCardTransaction> getVcts() {
        return vcts;
    }

    public void setVcts(List<VisaCardTransaction> vcts) {
        this.vcts = vcts;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getReferenceNum() {
        return referenceNum;
    }

    public void setReferenceNum(String referenceNum) {
        this.referenceNum = referenceNum;
    }

    /**
     * @return the requestBankCode
     */
    public String getRequestBankCode() {
        return requestBankCode;
    }

    /**
     * @param requestBankCode the requestBankCode to set
     */
    public void setRequestBankCode(String requestBankCode) {
        this.requestBankCode = requestBankCode;
    }

    /**
     * @return the AUTHORIZATION_PATH
     */
    public String getAUTHORIZATION_PATH() {
        return AUTHORIZATION_PATH;
    }

    /**
     * @return the CLEARING_PATH
     */
    public String getCLEARING_PATH() {
        return CLEARING_PATH;
    }

    /**
     * @return the SETTLEMENT_PATH
     */
    public String getSETTLEMENT_PATH() {
        return SETTLEMENT_PATH;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
