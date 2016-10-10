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
public class SACHSessionBean {
    
    private final String MEPS_SETTLEMENT = "https://localhost:8181/MEPSSimulator/meps/meps_settlement";
    
    @PersistenceContext(unitName = "BillPaymentSimulatorPU")
    private EntityManager em;

    public PaymentTransfer persist(PaymentTransfer object) {
        em.persist(object);
        return object;
    }
    
    public PaymentTransfer merge(PaymentTransfer object) {
        em.merge(object);
        return object;
    }
    
    // TODO: Find by date range
    public List<PaymentTransfer> findAll() {
        Query q = em.createQuery("SELECT pt FROM PaymentTransfer pt");
        return q.getResultList();
    }
    
    @Asynchronous
    public void sendMEPS(String netSettlementAmount) {
        // send to MEPS+
        Form form = new Form(); //bank info
        form.param("fromBankCode", "000");// SACH is 000
        form.param("toBankCode", "002"); // Other is 002
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
