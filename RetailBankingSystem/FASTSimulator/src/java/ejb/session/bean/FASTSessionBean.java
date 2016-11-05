/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bean;

import entity.FastSettlement;
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

    public void persistSettlement(FastSettlement object) {
        em.persist(object);
    }
    
    public FastSettlement findSettlement(String code) {
        return em.find(FastSettlement.class, code);
    }
    

    public PaymentTransfer findPaymentTransferByReferenceNumber(String referenceNumber) {
        return em.find(PaymentTransfer.class, referenceNumber);
    }
    
    @Asynchronous
    public void sendMEPS(PaymentTransfer tr) {
        List<FastSettlement> settlements = getSettlements(tr);

        // send to MEPS+
        Form form = new Form(); //bank info
        form.param("mbsCode", settlements.get(0).getBankCode());
        form.param("mbsSettlementAmount", settlements.get(0).getAmount().toString());
        form.param("mbsName", settlements.get(0).getName());
        form.param("citiCode", settlements.get(1).getBankCode());
        form.param("citiSettlementAmount", settlements.get(1).getAmount().toString());
        form.param("citiName", settlements.get(1).getName());
        form.param("ocbcCode", settlements.get(2).getBankCode());
        form.param("ocbcSettlementAmount", settlements.get(2).getAmount().toString());
        form.param("ocbcName", settlements.get(2).getName());
        
        System.out.println();
        System.out.println("Sending Net Settlement Amount to MEPs…");
        
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MEPS_SETTLEMENT);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        System.out.println(jsonString);

        if (jsonString.getString("message").equals("SUCCESS")) {
            System.out.println("FAST Transfer to other bank B with details");
//            PaymentTransfer pt = findPaymentTransferByReferenceNumber(referenceNumber);
//            pt.setSettled(Boolean.TRUE);
//            merge(pt);
        } else {
            System.out.println("FAIL");
        }
    }

    private List<FastSettlement> getSettlements(PaymentTransfer tr) {
        Query q = em.createQuery("SELECT fs FROM FastSettlement fs");
        List<FastSettlement> settlements = q.getResultList();
        if (settlements.isEmpty()) {
            System.out.println("Entity builder failed");
        }
        for (FastSettlement s : settlements) {
            if (tr.getFromBankCode().equals(s.getBankCode())) {
                s.setAmount(tr.getAmount().negate());
            }
            if (tr.getToBankCode().equals(s.getBankCode())) {
                s.setAmount(tr.getAmount());
            }
        }
        return settlements;
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
