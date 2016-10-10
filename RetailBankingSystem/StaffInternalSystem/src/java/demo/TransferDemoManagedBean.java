/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import server.utilities.GenerateAccountAndCCNumber;

/**
 *
 * @author leiyang
 */
@Named(value = "transferDemoManagedBean")
@ViewScoped
public class TransferDemoManagedBean implements Serializable {

    private final String SACH_TRANSFER_CLEARING = "https://localhost:8181/SACHSimulator/sach/sach_transfer_clearing";
    /**
     * Creates a new instance of TransferDemoManagedBean
     */
    public TransferDemoManagedBean() {
    }
    
    public void generateTransfer() {
        System.out.println("Generating transfer");
        Form form = new Form(); //bank info
        form.param("referenceNumber", GenerateAccountAndCCNumber.generateReferenceNumber());
        form.param("amount", "2000");
        form.param("bankCode", "002"); // other bank
        form.param("branchCode", "010");
        form.param("accountNumber", "123456789");
        form.param("toName", "Wang Zhe");
        form.param("fromName", "Lei Yang");
        form.param("myInitial", "Ly");
        

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(SACH_TRANSFER_CLEARING);

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
