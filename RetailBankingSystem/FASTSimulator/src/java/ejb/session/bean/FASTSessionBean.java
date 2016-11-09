/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bean;

import entity.FastSettlement;
import entity.FastTransfer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public class FASTSessionBean implements Serializable {

    private final String MEPS_FAST_SETTLEMENT = "https://localhost:8181/MEPSSimulator/meps/meps_fast_settlement";

    @PersistenceContext(unitName = "FASTSimulatorPU")
    private EntityManager em;

    public void persist(FastTransfer object) {
        em.persist(object);
    }

    public void merge(FastTransfer object) {
        em.merge(object);
    }

    public void persistSettlement(FastSettlement object) {
        em.persist(object);
    }

    public FastSettlement findSettlement(String code) {
        return em.find(FastSettlement.class, code);
    }

    public FastTransfer findPaymentTransferByReferenceNumber(String referenceNumber) {
        return em.find(FastTransfer.class, referenceNumber);
    }

    @Asynchronous
    public void sendMEPS(FastTransfer tr) {
//        List<FastSettlement> settlements = getSettlements(tr);

        // send to MEPS+
        Form form = new Form(); //bank info
        form.param("referenceNumber", tr.getReferenceNumber());
        form.param("amount", tr.getAmount().toString());
        form.param("toBankCode", tr.getToBankCode()); // other bank
        form.param("toBankName", tr.getToBankName()); // other bank
        form.param("fromBankCode", "001");
        form.param("fromBankName", tr.getFromBankName()); // other bank

        System.out.println(".");
        System.out.println("[SACH]:");
        System.out.println("Sending Net Settlement Amount to MEPS...");

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MEPS_FAST_SETTLEMENT);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        tr.setSettled(Boolean.TRUE);
        merge(tr);

        if (jsonString.getString("message").equals("SUCCESS")) {
            System.out.println("");
//            FastTransfer pt = findPaymentTransferByReferenceNumber(referenceNumber);
//            pt.setSettled(Boolean.TRUE);
//            merge(pt);
        } else {
            System.out.println("FAIL");
        }
    }

//    private List<FastSettlement> getSettlements(FastTransfer tr) {
//        Query q = em.createQuery("SELECT fs FROM FastSettlement fs");
//        List<FastSettlement> settlements = new ArrayList<FastSettlement>();
//        settlements = q.getResultList();
//        if (settlements.isEmpty()) {
//            System.out.println("Entity builder failed");
//        }
//        for (FastSettlement s : settlements) {
//            s.setAmount(BigDecimal.ZERO);
//            if (tr.getFromBankCode().equals(s.getBankCode())) {
//                s.setAmount(tr.getAmount().negate());
//            }
//            if (tr.getToBankCode().equals(s.getBankCode())) {
//                s.setAmount(tr.getAmount());
//            }
//        }
//        return settlements;
//    }
}
