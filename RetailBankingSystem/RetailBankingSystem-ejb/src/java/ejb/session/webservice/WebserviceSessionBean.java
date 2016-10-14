/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.webservice;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.json.JsonObject;
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
@Stateless
public class WebserviceSessionBean implements WebserviceSessionBeanLocal {
    
    private final String MEPS_SETTLEMENT = "https://localhost:8181/MEPSSimulator/meps/meps_settlement";

    @Asynchronous
    @Override
    public void paySACHSettlement(String netSettlementAmount) {
        System.out.println("Paying Settlement");
        // send to MEPS+
        Form form = new Form(); //bank info
        form.param("fromBankCode", "001");// MBS is 001
        form.param("toBankCode", "000"); // SACH is 000
        form.param("netSettlementAmount", netSettlementAmount);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MEPS_SETTLEMENT);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        System.out.println(jsonString);
        
        if (jsonString.getString("message").equals("SUCCESS")) {
            System.out.println("Request received");
        } else {
            System.out.println("FAIL");
        }
    }
    
    @Asynchronous
    @Override
    public void payFASTSettlement(String netSettlementAmount) {
        System.out.println("Paying Settlement");
        // send to MEPS+
        Form form = new Form(); //bank info
        form.param("fromBankCode", "001");// MBS is 001
        form.param("toBankCode", "111"); // SACH is 000
        form.param("netSettlementAmount", netSettlementAmount);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MEPS_SETTLEMENT);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        System.out.println(jsonString);
        
        if (jsonString.getString("message").equals("SUCCESS")) {
            System.out.println("Request received");
        } else {
            System.out.println("FAIL");
        }
    }
}
