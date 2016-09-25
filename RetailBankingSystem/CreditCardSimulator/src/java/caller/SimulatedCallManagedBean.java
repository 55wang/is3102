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
    
    private final String path = "https://localhost:8181/StaffInternalSystem/rest/credit_card";
    
    @PostConstruct
    public void init() {
        System.out.println("SimulatedCallManagedBean");
    }
    
    public void sendFailedDailyAuhorization() {
        Form form = new Form();
        form.param("ccNumber", "4545454545454545");
        form.param("ccAmount", "3000");
        form.param("ccTcode", "MDS");
        form.param("ccDescription", "Test failed authorization");
        
        // Start calling
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(path);
        
        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);

        
//        private String creditCardNumber;
//    private String amount;
//    private String description;
//    private String errorMessage;
//    private String tCode;
//    private String aCode;
    }
    
    public void sendFailedMonthlyAuhorization() {
        
    }
    
    public void sendSuccessAuhorization() {
        
    }

            
            //
    public void initiating() {
        System.out.println("Calling web services");
        testGetMethod();
    }

    private void testGetMethod() {
        List<String> allString = new ArrayList<>();
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(path);
        // @Get request
        JsonArray response = target.request(MediaType.APPLICATION_JSON).get(JsonArray.class);
        for (JsonValue str : response) {
            allString.add(((JsonString) str).getString());
        }
        System.out.println(allString);
    }
}
