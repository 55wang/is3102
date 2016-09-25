/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import utils.OTPUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "simulatedCallManagedBean")
@ViewScoped
public class SimulatedCallManagedBean implements Serializable {

    /**
     * Creates a new instance of SimulatedCallManagedBean
     */
    public SimulatedCallManagedBean() {
    }
<<<<<<< HEAD

    private final String AUTHORIZATION_PATH = "https://localhost:8181/StaffInternalSystem/rest/credit_card_authorization";
    private final String CLEARING_PATH = "https://localhost:8181/StaffInternalSystem/rest/credit_card_clearing";

=======
    
    private final String path = "https://localhost:8181/StaffInternalSystem/rest/credit_card";
    
>>>>>>> 89bc98d31340f64ec07553aea9fd977c409b8466
    @PostConstruct
    public void init() {
        System.out.println("SimulatedCallManagedBean");
    }
    
    public void sendFailedDailyAuhorization() {
        Form form = new Form();
        form.param("ccNumber", "4545454545454545");
        form.param("ccAmount", "600"); // 500 is daily limit and 1000 for monthly limist, current out standing is 800
        form.param("ccTcode", "MDS");
        form.param("ccDescription", "Test failed daily authorization");
        
        System.out.println("Calling bank to check credit card transaction");
        System.out.println("Credit Card number 4545454545454545");
        System.out.println("Requesting amount 600");
        // Start calling
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(AUTHORIZATION_PATH);
        
        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        CreditCardDTO cc = new CreditCardDTO(jsonString); 
        
        if (cc.getAuthorizationCode()== null || cc.getAuthorizationCode().isEmpty()) {
            System.out.println(cc.getMessage());
        }
    }
    
    public void sendFailedMonthlyAuhorization() {
        Form form = new Form();
        form.param("ccNumber", "4545454545454545");
        form.param("ccAmount", "1400"); // 500 is daily limit and 1000 for monthly limist, current out standing is 800
        form.param("ccTcode", "MDS");
        form.param("ccDescription", "Test failed monthly authorization");
        
        System.out.println("Calling bank to check credit card transaction");
        System.out.println("Credit Card number 4545454545454545");
        System.out.println("Requesting amount 1400");
        
        // Start calling
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(AUTHORIZATION_PATH);
        
        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        CreditCardDTO cc = new CreditCardDTO(jsonString); 
        
        if (cc.getAuthorizationCode() == null || cc.getAuthorizationCode().isEmpty()) {
            System.out.println(cc.getMessage());
        }
    }
    
    public void sendSuccessAuthorization() {
        Form form = new Form();
        form.param("ccNumber", "4545454545454545");
        form.param("ccAmount", "100"); // 500 is daily limit and 1000 for monthly limist, current out standing is 800
        form.param("ccTcode", "MDS");
        form.param("ccDescription", "Test Success authorization");
        
        System.out.println("Calling bank to check credit card transaction");
        System.out.println("Credit Card number 4545454545454545");
        System.out.println("Requesting amount 100");
        
        // Start calling
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(AUTHORIZATION_PATH);
        
        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        CreditCardDTO cc = new CreditCardDTO(jsonString); 
        
        if (cc.getAuthorizationCode() != null && !cc.getAuthorizationCode().isEmpty()) {
            // check code validation
            System.out.println("Getting Authorizaed response with code: " + cc.getAuthorizationCode());
            String returnCode = OTPUtils.generateSingleToken(Integer.parseInt(cc.getAuthorizationCode()));
            // call for clearing
            sendSuccessClearing(returnCode, cc.getAuthorizationCode());
        }
    }
    
    private void sendSuccessClearing(String returnCode, String aCode) {
        Form form = new Form();
        form.param("token", returnCode);
        form.param("aCode", aCode);
        form.param("ccNumber", "4545454545454545");
        form.param("ccAmount", "400"); // 500 is daily limit and 1000 for monthly limist, current out standing is 800
        form.param("ccTcode", "MDS");
        form.param("ccDescription", "Test failed monthly authorization");
        
        System.out.println("Calling bank to check credit card transaction");
        System.out.println("Credit Card number 4545454545454545");
        System.out.println("Requesting amount 400");
        System.out.println("Requesting with token:" + returnCode);
        
        // Start calling
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(CLEARING_PATH);
        
        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        CreditCardDTO cc = new CreditCardDTO(jsonString); 
        System.out.println(cc.getMessage());
    }
            //
    public void initiating() {
        System.out.println("Calling web services");
        testGetMethod();
    }

    private void testGetMethod() {
        List<String> allString = new ArrayList<>();
        Client client = ClientBuilder.newClient();

        WebTarget target = client.target(AUTHORIZATION_PATH + "?accountNumber=123456789");// Mapped by @QueryParam("accountNumber") 

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
        WebTarget target = client.target(AUTHORIZATION_PATH);
        
        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        System.out.println(jsonString);
        System.out.println("name: " + jsonString.getString("name"));
        System.out.println("creditCardNumber: " + jsonString.getString("creditCardNumber"));
        System.out.println("amount: " + jsonString.getString("amount"));

        // Do necessary process
    }
}
