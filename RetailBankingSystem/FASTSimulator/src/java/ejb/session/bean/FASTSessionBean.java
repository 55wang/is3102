/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bean;

import entity.PaymentTransfer;
import java.util.List;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
public class FASTSessionBean {
    
    private final String MBS_NET_SETTLEMENT_PATH = "https://localhost:8181/StaffInternalSystem/rest/net_settlement";
    private final String MEPS_SETTLEMENT = "https://localhost:8181/MEPSSimulator/meps/meps_settlement";
    
    @PersistenceContext(unitName = "FASTSimulatorPU")
    private EntityManager em;

    public void persist(PaymentTransfer object) {
        em.persist(object);
    }
    
    public void merge(PaymentTransfer object) {
        em.merge(object);
    }
    
    public PaymentTransfer findPaymentTransferByReferenceNumber(String referenceNumber) {
        return em.find(PaymentTransfer.class, referenceNumber);
    }
    
    @Asynchronous
    public void sendMEPS(String netSettlementAmount, String toBankCode, String referenceNumber) {
        // send to MEPS+
        Form form = new Form(); //bank info
        form.param("fromBankCode", "111");// FAST is 111
        form.param("toBankCode", toBankCode); // Other is 002
        form.param("netSettlementAmount", netSettlementAmount);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MEPS_SETTLEMENT);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        System.out.println(jsonString);
        
        if (jsonString.getString("message").equals("SUCCESS")) {
            System.out.println("FAST Transfer to other bank B with details");
            PaymentTransfer pt = findPaymentTransferByReferenceNumber(referenceNumber);
            pt.setSettled(Boolean.TRUE);
            merge(pt);
        } else {
            System.out.println("FAIL");
        }
    }
    
    @Asynchronous
    public void sendMBSNetSettlement(String netSettlementAmount, String fromBankCode, String toBankCode, String referenceNumber) {
        
        // send to mbs
        Form form = new Form(); //bank info
        form.param("netSettlementAmount", netSettlementAmount);
        form.param("fromBankCode", fromBankCode);
        form.param("toBankCode", toBankCode);
        form.param("agencyCode", "111");
        form.param("referenceNumber", referenceNumber);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MBS_NET_SETTLEMENT_PATH);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        System.out.println(jsonString);
        
        if (jsonString != null && jsonString.getString("error").equals("SUCCESS")) {
            System.out.println("Request received");
        } else {
            System.out.println("FAIL");
        }
    }
}