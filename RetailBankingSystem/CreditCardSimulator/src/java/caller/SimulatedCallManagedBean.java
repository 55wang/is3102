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
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
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
