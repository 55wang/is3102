/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bean;

import entity.BillTransfer;
import entity.PaymentTransfer;
import entity.SachSettlement;
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
public class SACHSessionBean {

    // all the urls put here
    private final String MEPS_SETTLEMENT = "https://localhost:8181/MEPSSimulator/meps/meps_settlement";
    private final String MBS_NET_SETTLEMENT_PATH = "https://localhost:8181/StaffInternalSystem/rest/net_settlement";
    private final String MBS_TRANSFER_PAYMENT = "https://localhost:8181/StaffInternalSystem/rest/mbs_receive_transfer_payment";
    private final String MBS_CC_PAYMENT = "https://localhost:8181/StaffInternalSystem/rest/mbs_receive_cc_payment";
    private final String MBS_GIRO_REQUEST = "https://localhost:8181/StaffInternalSystem/rest/mbs_receive_giro_request";

    @PersistenceContext(unitName = "BillPaymentSimulatorPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public void merge(Object object) {
        em.merge(object);
    }

    public void persistSettlement(SachSettlement object) {
        em.persist(object);
    }

    public SachSettlement findSettlement(String code) {
        return em.find(SachSettlement.class, code);
    }

    public BillTransfer findBillTransferByReferenceNumber(String referenceNumber) {
        return em.find(BillTransfer.class, referenceNumber);
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

//    @Asynchronous
//    public void sendMEPS(String netSettlementAmount, String toBankCode) {
//        // send to MEPS+
//        Form form = new Form(); //bank info
//        form.param("fromBankCode", "000");// SACH is 000
//        form.param("toBankCode", toBankCode); // Other is 002
//        form.param("netSettlementAmount", netSettlementAmount);
//
//        Client client = ClientBuilder.newClient();
//        WebTarget target = client.target(MEPS_SETTLEMENT);
//
//        // This is the response
//        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
//        System.out.println(jsonString);
//        
//        if (jsonString.getString("message").equals("SUCCESS")) {
//            System.out.println("Request received");
//        } else {
//            System.out.println("FAIL");
//        }
//    }
    @Asynchronous
    public void sendMEPSNetSettlement() {
        List<SachSettlement> settlements = getSettlements();

        // send to MEPS+
        Form form = new Form(); //bank info
        form.param("mbsCode", settlements.get(0).getBankCode());
        form.param("mbsSettlementAmount", settlements.get(0).getAmount().setScale(4).toString());
        form.param("mbsName", settlements.get(0).getName());
        form.param("citiCode", settlements.get(1).getBankCode());
        form.param("citiSettlementAmount", settlements.get(1).getAmount().setScale(4).toString());
        form.param("citiName", settlements.get(1).getName());
        form.param("ocbcCode", settlements.get(2).getBankCode());
        form.param("ocbcSettlementAmount", settlements.get(2).getAmount().setScale(4).toString());
        form.param("ocbcName", settlements.get(2).getName());

        System.out.println(".");
        System.out.println("[SACH]:");
        System.out.println("Sending Net Settlement Amount to MEPS...");

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MEPS_SETTLEMENT);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);

        if (jsonString.getString("message").equals("SUCCESS")) {
            System.out.println(".");
            System.out.println("[MPES]");
            System.out.println("Received successful response from MEPS");

        } else {
            System.out.println("FAIL");
        }
    }

    public List<SachSettlement> getSettlements() {
        Query q = em.createQuery("SELECT ss FROM SachSettlement ss");
        return q.getResultList();
    }

    public void updateNetSettlement(PaymentTransfer tr) {
        List<SachSettlement> settlements = getSettlements();

        if (settlements.isEmpty()) {
            System.out.println("Entity builder failed");
        }
        for (SachSettlement s : settlements) {

            if (tr.getFromBankCode().equals(s.getBankCode())) {
                s.setAmount(s.getAmount().subtract(tr.getAmount()));
                em.merge(s);
            }
            if (tr.getToBankCode().equals(s.getBankCode())) {
                s.setAmount(s.getAmount().add(tr.getAmount()));
                em.merge(s);
            }
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
        WebTarget target = client.target(MBS_TRANSFER_PAYMENT);

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

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MBS_CC_PAYMENT);

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

    @Asynchronous
    public void sendMBSGiroRequest(BillTransfer bt) {

        // send to mbs
        // REMARK: Form key must match FormParam in the receiver side
        // REMARK: Form sample code
        Form form = new Form(); //bank info
        form.param("referenceNumber", bt.getReferenceNumber());
        form.param("amount", bt.getAmount().toString());
        form.param("shortCode", bt.getShortCode());
        form.param("billReferenceNumber", bt.getBillReferenceNumber());

        // REMARK: Copy this, and change client.target("TO BE CHANGED");
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MBS_GIRO_REQUEST);

        // This is the response
        // REMARK: Copy this, and do not change anything
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        System.out.println(jsonString);

        // REMARK: Copy this, and change client.target("TO BE CHANGED");
        if (jsonString != null && jsonString.getString("error").equals("SUCCESS")) {
            System.out.println("Request received");
            // REMARK: Do necessary action if it succeed
            em.persist(bt);
        } else {
            // REMARK: Do necesary action if it failed
            System.out.println("FAIL");
        }
    }

}
