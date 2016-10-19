/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bean;

import entity.BillTransfer;
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
    private final String MBS_NET_SETTLEMENT_PATH = "https://localhost:8181/StaffInternalSystem/rest/net_settlement";
    private final String SACH_TRANSFER_PAYMENT = "https://localhost:8181/StaffInternalSystem/rest/mbs_receive_transfer_payment";
    private final String SACH_CC_PAYMENT = "https://localhost:8181/StaffInternalSystem/rest/mbs_receive_cc_payment";
    
    @PersistenceContext(unitName = "BillPaymentSimulatorPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }
    
    public void merge(Object object) {
        em.merge(object);
    }
    
    // TODO: Find by date range
    public List<PaymentTransfer> findAllPaymentTransferForBankCode(String bankCode) {
        Query q = em.createQuery("SELECT pt FROM PaymentTransfer pt WHERE pt.fromBankCode =:bankCode OR pt.toBankCode =:bankCode AND pt.settled = :settled");
        q.setParameter("bankCode", bankCode);
        q.setParameter("settled", false);
        return q.getResultList();
    }
    
    public List<PaymentTransfer> findAllPaymentTransferFromBankCode(String fromBankCode) {
        Query q = em.createQuery("SELECT pt FROM PaymentTransfer pt WHERE pt.fromBankCode = :fromBankCode AND pt.settled = :settled");
        q.setParameter("fromBankCode", fromBankCode);
        q.setParameter("settled", false);
        return q.getResultList();
    }
    
    public List<PaymentTransfer> findAllPaymentTransferToBankCode(String toBankCode) {
        Query q = em.createQuery("SELECT pt FROM PaymentTransfer pt WHERE pt.toBankCode = :toBankCode AND pt.settled = :settled");
        q.setParameter("settled", false);
        q.setParameter("toBankCode", toBankCode);
        return q.getResultList();
    }
    
    public List<PaymentTransfer> findAllPaymentTransferFromBankCodeToBankCode(String fromBankCode, String toBankCode) {
        Query q = em.createQuery("SELECT pt FROM PaymentTransfer pt WHERE pt.fromBankCode = :fromBankCode AND pt.toBankCode = :toBankCode AND pt.settled = :settled");
        q.setParameter("settled", false);
        q.setParameter("fromBankCode", fromBankCode);
        q.setParameter("toBankCode", toBankCode);
        return q.getResultList();
    }
    
    public List<BillTransfer> findAllBillTransferForBankCode(String bankCode) {
        Query q = em.createQuery("SELECT bt FROM BillTransfer bt WHERE bt.fromBankCode =:bankCode OR bt.partnerBankCode =:bankCode AND bt.settled = :settled");
        q.setParameter("settled", false);
        q.setParameter("bankCode", bankCode);
        return q.getResultList();
    }
    
    public List<BillTransfer> findAllBillTransferFromBankCode(String fromBankCode) {
        Query q = em.createQuery("SELECT bt FROM BillTransfer bt WHERE bt.fromBankCode = :fromBankCode AND bt.settled = :settled");
        q.setParameter("settled", false);
        q.setParameter("fromBankCode", fromBankCode);
        
        return q.getResultList();
    }
    
    public List<BillTransfer> findAllBillTransferToBankCode(String toBankCode) {
        Query q = em.createQuery("SELECT bt FROM BillTransfer bt WHERE bt.partnerBankCode = :toBankCode AND bt.settled = :settled");
        q.setParameter("settled", false);
        q.setParameter("toBankCode", toBankCode);
        return q.getResultList();
    }
    
    public List<BillTransfer> findAllBillTransferFromBankCodeToBankCode(String fromBankCode, String toBankCode) {
        Query q = em.createQuery("SELECT bt FROM BillTransfer bt WHERE bt.fromBankCode = :fromBankCode AND bt.partnerBankCode = :toBankCode AND bt.settled = :settled");
        q.setParameter("settled", false);
        q.setParameter("fromBankCode", fromBankCode);
        q.setParameter("toBankCode", toBankCode);
        return q.getResultList();
    }
    
    @Asynchronous
    public void sendMEPS(String netSettlementAmount, String toBankCode) {
        // send to MEPS+
        Form form = new Form(); //bank info
        form.param("fromBankCode", "000");// SACH is 000
        form.param("toBankCode", toBankCode); // Other is 002
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
    public void sendMBSNetSettlement(String netSettlementAmount) {
        
        // send to mbs
        Form form = new Form(); //bank info
        form.param("netSettlementAmount", netSettlementAmount);
        form.param("fromBankCode", "001");
        form.param("toBankCode", "002");
        form.param("agencyCode", "000");
        form.param("referenceNumber", "");

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
    
    @Asynchronous
    public void sendMBSPaymentTransferSettlement(PaymentTransfer pt) {
        
        // send to mbs
        Form form = new Form(); //bank info
        form.param("referenceNumber", pt.getReferenceNumber());
        form.param("amount", pt.getAmount().toString());
        form.param("accountNumber", pt.getAccountNumber());
        form.param("toName", pt.getToName());
        form.param("fromName", pt.getFromName());
        form.param("myInitial", pt.getMyInitial());

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(SACH_TRANSFER_PAYMENT);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        System.out.println(jsonString);
        
        if (jsonString != null && jsonString.getString("error").equals("SUCCESS")) {
            System.out.println("Request received");
            em.persist(pt);
        } else {
            System.out.println("FAIL");
        }
    }
    
    @Asynchronous
    public void sendMBSCCPaymentSettlement(BillTransfer bt) {
        
        // send to mbs
        Form form = new Form(); //bank info
        form.param("ccNumber", bt.getReferenceNumber());
        form.param("ccAmount", bt.getAmount().toString());
        form.param("referenceNumber", bt.getBillReferenceNumber());

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(SACH_CC_PAYMENT);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        System.out.println(jsonString);
        
        if (jsonString != null && jsonString.getString("error").equals("SUCCESS")) {
            System.out.println("Request received");
            em.persist(bt);
        } else {
            System.out.println("FAIL");
        }
    }
}
