/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bean;

import entity.SettlementAccount;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
public class MEPSSessionBean {
    
    private final String SACH_INFORM_NET_SETTLEMENT = "https://localhost:8181/SACHSimulator/sach/sach_inform_settlement";
    private final String FAST_INFORM_NET_SETTLEMENT = "https://localhost:8181/FASTSimulator/fast/fast_inform_settlement";
    
    @PersistenceContext(unitName = "MEPSSimulatorPU")
    private EntityManager em;
    
    public SettlementAccount find(String bankCode) {
        return em.find(SettlementAccount.class, bankCode);
    }

    public SettlementAccount persist(SettlementAccount object) {
        em.persist(object);
        return object;
    }
    
    public SettlementAccount merge(SettlementAccount object) {
        em.merge(object);
        return object;
    }
    
    @Asynchronous
    public void informSACHSettlement(String fromBankCode, String toBankCode, String netSettlementAmount) {
        
        // send to mbs
        Form form = new Form(); //bank info
        form.param("netSettlementAmount", netSettlementAmount);
        form.param("fromBankCode", fromBankCode);
        form.param("toBankCode", toBankCode);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(SACH_INFORM_NET_SETTLEMENT);
        System.out.println("Inform sach about payment received from mbs");
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
    public void informFASTSettlement(String fromBankCode, String toBankCode, String netSettlementAmount, String referenceNumber) {
        
        // send to mbs
        Form form = new Form(); //bank info
        form.param("netSettlementAmount", netSettlementAmount);
        form.param("fromBankCode", fromBankCode);
        form.param("toBankCode", toBankCode);
        form.param("referenceNumber", referenceNumber);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(FAST_INFORM_NET_SETTLEMENT);
        System.out.println("Inform FAST about payment received from mbs");
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
