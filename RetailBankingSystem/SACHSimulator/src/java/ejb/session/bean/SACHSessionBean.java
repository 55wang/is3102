/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bean;

import dto.TransactionDTO;
import dto.TransactionSummaryDTO;
import entity.BillTransfer;
import entity.PaymentTransfer;
import entity.SachSettlement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
public class SACHSessionBean implements Serializable {
    
    @PersistenceContext(unitName = "SACHSimulatorPU")
    private EntityManager em;

    // all the urls put here
    private final String MEPS_SETTLEMENT = "https://localhost:8181/MEPSSimulator/meps/meps_settlement";
    private final String MBS_NET_SETTLEMENT_PATH = "https://localhost:8181/StaffInternalSystem/rest/net_settlement";
    private final String MBS_TRANSFER_PAYMENT = "https://localhost:8181/StaffInternalSystem/rest/mbs_receive_transfer_payment";
    private final String MBS_CC_PAYMENT = "https://localhost:8181/StaffInternalSystem/rest/mbs_receive_cc_payment";
    private final String MBS_GIRO_REQUEST = "https://localhost:8181/StaffInternalSystem/rest/mbs_receive_giro_request";

    public List<SachSettlement> getAllSachSettlement() {
        Query q = em.createQuery("SELECT ss FROM SachSettlement ss");
        return q.getResultList();
    }

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

    public SachSettlement findSettlementByToBankCode(String toBankCode) {
        Query q = em.createQuery("SELECT ss FROM SachSettlement ss WHERE ss.toBankCode = :toBankCode");
        q.setParameter("toBankCode", toBankCode);
        return (SachSettlement) q.getSingleResult();
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
        List<String> transactions = getTodayMBSTransactions();

        TransactionSummaryDTO summary = new TransactionSummaryDTO();
        for (String tr : transactions) {
            TransactionDTO dto = new TransactionDTO();
            dto.setReferenceNumber(tr);
            summary.getTransactionSummary().add(dto);
        }

        summary.setCitiFromBankCode(settlements.get(0).getFromBankCode());
        summary.setCitiFromBankName(settlements.get(0).getFromBankName());
        summary.setCitiToBankCode(settlements.get(0).getToBankCode());
        summary.setCitiToBankName(settlements.get(0).getToBankName());
        summary.setCitiSettlementAmount(settlements.get(0).getAmount().setScale(4).toString());

        summary.setOcbcFromBankCode(settlements.get(1).getFromBankCode());
        summary.setOcbcFromBankName(settlements.get(1).getFromBankName());
        summary.setOcbcToBankCode(settlements.get(1).getToBankCode());
        summary.setOcbcToBankName(settlements.get(1).getToBankName());
        summary.setOcbcSettlementAmount(settlements.get(1).getAmount().setScale(4).toString());
        summary.setDate(new Date().toString());

        System.out.println("----------------Bill Transfer to MBS----------------");
        System.out.println("[SACH]:");
        System.out.println("Sending Net Settlement Amount to MEPS...");

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MEPS_SETTLEMENT);

        clearNetSettlements();
        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(summary, MediaType.APPLICATION_JSON), JsonObject.class);

        if (jsonString.getString("message").equals("SUCCESS")) {
            System.out.println(".");
            System.out.println("[MPES]");
            System.out.println("Received successful response from MEPS");

        } else {
            System.out.println("FAIL");
        }
    }

    public List<String> getTodayMBSTransactions() {
        List<String> transactions = new ArrayList<String>();
        transactions.addAll(getTodayBillTransactions());
        transactions.addAll(getTodayTransferTransactions());
        return transactions;
    }

    public List<String> getTodayBillTransactions() {
        List<String> transactions = new ArrayList<String>();
        Date startDate = getBeginOfDay();
        Date endDate = getEndOfDay();
        Query q = em.createQuery("SELECT br FROM BillTransfer br WHERE ( br.partnerBankCode =:billToCode OR br.fromBankCode =:billFromCode) "
                + " AND (br.creationDate BETWEEN :startDate AND :endDate)");
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        q.setParameter("billToCode", "001");
        q.setParameter("billFromCode", "001");

        List<BillTransfer> bts = q.getResultList();
        for (BillTransfer bt : bts) {
            transactions.add(bt.getReferenceNumber());
        }
        return transactions;
    }

    public List<String> getTodayTransferTransactions() {
        List<String> transactions = new ArrayList<String>();
        Date startDate = getBeginOfDay();
        Date endDate = getEndOfDay();
        Query q = em.createQuery("SELECT pr FROM PaymentTransfer pr WHERE ( pr.toBankCode =:billToCode OR pr.fromBankCode =:billFromCode) "
                + " AND (pr.creationDate BETWEEN :startDate AND :endDate)");
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        q.setParameter("billToCode", "001");
        q.setParameter("billFromCode", "001");

        List<PaymentTransfer> bts = q.getResultList();
        for (PaymentTransfer bt : bts) {
            transactions.add(bt.getReferenceNumber());
        }
        return transactions;
    }

    public void clearNetSettlements() {
        Query q = em.createQuery("SELECT ss FROM SachSettlement ss");
        List<SachSettlement> result = q.getResultList();
        for (SachSettlement s : result) {
            s.setAmount(BigDecimal.ZERO);
            em.merge(s);
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

            if (tr.getFromBankCode().equals(s.getFromBankCode()) && tr.getToBankCode().equals(s.getToBankCode())) {
                s.setAmount(s.getAmount().add(tr.getAmount()));
                em.merge(s);
            }

            if (tr.getFromBankCode().equals(s.getToBankCode()) && tr.getToBankCode().equals(s.getFromBankCode())) {
                s.setAmount(s.getAmount().subtract(tr.getAmount()));
                em.merge(s);
            }

        }
    }

    public void updateNetSettlementAddBill(BillTransfer br) {
        
        Query q = em.createQuery("SELECT ss FROM SachSettlement ss WHERE ss.fromBankCode =:fromBankCode AND ss.toBankCode =:toBankCode");
        q.setParameter("fromBankCode", br.getFromBankCode());
        q.setParameter("toBankCode", br.getPartnerBankCode());
        List<SachSettlement> result = q.getResultList();
        for (SachSettlement s : result) {
            System.out.println("adding settlement");
            s.setAmount(s.getAmount().add(br.getAmount()));
            em.merge(s);
        }
        
        q = em.createQuery("SELECT ss FROM SachSettlement ss WHERE ss.fromBankCode =:fromBankCode AND ss.toBankCode =:toBankCode");
        q.setParameter("fromBankCode", br.getPartnerBankCode());
        q.setParameter("toBankCode", br.getFromBankCode());
        result = q.getResultList();
        for (SachSettlement s : result) {
            System.out.println("adding settlement");
            s.setAmount(s.getAmount().subtract(br.getAmount()));
            em.merge(s);
        }
        
        
//        List<SachSettlement> settlements = getSettlements();
//
//        if (settlements.isEmpty()) {
//            System.out.println("Entity builder failed");
//        }
//        System.out.println(settlements.size());
//        for (SachSettlement s : settlements) {
//
//            System.out.println(s);
//            System.out.println(br);
//            if (br.getFromBankCode().equals(s.getFromBankCode()) && br.getPartnerBankCode().equals(s.getToBankCode())) {
//                System.out.println("adding settlement");
//                s.setAmount(s.getAmount().add(br.getAmount()));
//                em.merge(s);
//                System.out.println(s);
//            }
//            System.out.println("before removing settlement");
//            if (br.getFromBankCode().equals(s.getToBankCode()) && br.getPartnerBankAccount().equals(s.getFromBankCode())) {
//                System.out.println("removing settlement");
//                s.setAmount(s.getAmount().subtract(br.getAmount()));
//                em.merge(s);
//                System.out.println(s);
//            }
//        }
//        System.out.println("End of updateNetSettlementAddBill");
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

    public void sendMBSPaymentTransfer(PaymentTransfer pt) {
        System.out.println("[Other bank]:");
        System.out.println("Generating payment instruction...");
        System.out.println("Sending payment instruction to SACH...");

        System.out.println(".");
        System.out.println("[SACH]:");
        System.out.println("Received payment instruction...");

        pt.setSettled(true);
        // send to mbs
        Form form = new Form(); //bank info
        form.param("referenceNumber", pt.getReferenceNumber());
        form.param("amount", pt.getAmount().toString());
        form.param("toBankCode", pt.getToBankCode());
        form.param("toBankAccount", pt.getAccountNumber());
        form.param("toName", pt.getToName());
        form.param("fromCode", pt.getFromBankCode());
        form.param("fromName", pt.getFromName());
        form.param("myInitial", pt.getMyInitial());

        List<SachSettlement> bankAccounts = getSettlements();

        System.out.println("Current net settlement:");
        for (SachSettlement s : bankAccounts) {
            if (s.getAmount().compareTo(BigDecimal.ZERO) == -1) {
                System.out.println(".       " + s.getToBankCode() + " " + s.getToBankName() + " to " + s.getFromBankCode() + " " + s.getFromBankName() + ": " + s.getAmount().abs().setScale(4).toString());
            } else {
                System.out.println(".       " + s.getFromBankCode() + " " + s.getFromBankName() + " to " + s.getToBankCode() + " " + s.getToBankName() + ": " + s.getAmount().abs().setScale(4).toString());

            }
        }

        System.out.println("Updating net settlement...");
        updateNetSettlement(pt);

        List<SachSettlement> updatedbankAccounts = getSettlements();

        System.out.println("Updated net settlement:");
        for (SachSettlement s : updatedbankAccounts) {
            if (s.getAmount().compareTo(BigDecimal.ZERO) == -1) {
                System.out.println(".       " + s.getToBankCode() + " " + s.getToBankName() + " to " + s.getFromBankCode() + " " + s.getFromBankName() + ": " + s.getAmount().abs().setScale(4).toString());
            } else {
                System.out.println(".       " + s.getFromBankCode() + " " + s.getFromBankName() + " to " + s.getToBankCode() + " " + s.getToBankName() + ": " + s.getAmount().abs().setScale(4).toString());

            }
        }

        System.out.println(".");
        System.out.println("Sending MBS payment instruction...");
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MBS_TRANSFER_PAYMENT);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);

        if (jsonString != null && jsonString.getString("error").equals("SUCCESS")) {
            System.out.println("Request approved");
            em.persist(pt);
        } else {
            System.out.println("FAIL");
        }
    }

    public void sendMBSCCPaymentSettlement(BillTransfer bt) {
        System.out.println("[Other bank]:");
        System.out.println("Generating payment instruction...");
        System.out.println("Sending payment instruction to SACH...");

        System.out.println(".");
        System.out.println("[SACH]:");
        System.out.println("Received payment instruction...");
        // send to mbs
        Form form = new Form(); //bank info
        form.param("referenceNumber", bt.getReferenceNumber());
        form.param("partnerBankCode", bt.getPartnerBankCode());
        form.param("fromBankCode", bt.getFromBankCode());
        form.param("ccNumber", bt.getBillReferenceNumber());
        form.param("ccAmount", bt.getAmount().toString());

        List<SachSettlement> bankAccounts = getSettlements();

        System.out.println("Current net settlement:");
        for (SachSettlement s : bankAccounts) {
            if (s.getAmount().compareTo(BigDecimal.ZERO) == -1) {
                System.out.println(".       " + s.getToBankCode() + " " + s.getToBankName() + " to " + s.getFromBankCode() + " " + s.getFromBankName() + ": " + s.getAmount().abs().setScale(4).toString());
            } else {
                System.out.println(".       " + s.getFromBankCode() + " " + s.getFromBankName() + " to " + s.getToBankCode() + " " + s.getToBankName() + ": " + s.getAmount().abs().setScale(4).toString());

            }
        }

        System.out.println("Updating net settlement...");
        System.out.println(bt);
        updateNetSettlementAddBill(bt);

        List<SachSettlement> updatedbankAccounts = getSettlements();

        System.out.println("Updated net settlement:");
        for (SachSettlement s : updatedbankAccounts) {
            if (s.getAmount().compareTo(BigDecimal.ZERO) == -1) {
                System.out.println(".       " + s.getToBankCode() + " " + s.getToBankName() + " to " + s.getFromBankCode() + " " + s.getFromBankName() + ": " + s.getAmount().abs().setScale(4).toString());
            } else {
                System.out.println(".       " + s.getFromBankCode() + " " + s.getFromBankName() + " to " + s.getToBankCode() + " " + s.getToBankName() + ": " + s.getAmount().abs().setScale(4).toString());

            }
        }

        System.out.println(".");
        System.out.println("Sending MBS payment instruction...");
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MBS_CC_PAYMENT);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);

        if (jsonString != null && jsonString.getString("error").equals("SUCCESS")) {
            System.out.println("Request approved");
            em.persist(bt);
        } else {
            System.out.println("FAIL");
        }
    }

    @Asynchronous
    public void sendMBSGiroRequest(BillTransfer bt) {
        System.out.println("[Other bank]");
        System.out.println("Generating GIRO request...");
        System.out.println("Sending GIRO request to SACH...");

        System.out.println(".");
        System.out.println("[SACH]");
        System.out.println("Received GIRO request...");

        // send to mbs
        // REMARK: Form key must match FormParam in the receiver side
        // REMARK: Form sample code
        Form form = new Form(); //bank info
        form.param("referenceNumber", bt.getReferenceNumber());
        form.param("amount", bt.getAmount().toString());
        form.param("shortCode", bt.getShortCode());
        form.param("billReferenceNumber", bt.getBillReferenceNumber());

        System.out.println("Sending GIRO request to MBS...");
        // REMARK: Copy this, and change client.target("TO BE CHANGED");
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MBS_GIRO_REQUEST);

        // This is the response
        // REMARK: Copy this, and do not change anything
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);

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

    private Date getBeginOfDay() {
        Calendar calendar = getCalendarForNow();
        getTimeToBeginningOfDay(calendar);
        return calendar.getTime();
    }

    private Date getEndOfDay() {
        Calendar calendar = getCalendarForNow();
        getTimeToEndofDay(calendar);
        return calendar.getTime();
    }

    private Calendar getCalendarForNow() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    public Calendar getTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    private Calendar getTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar;
    }

    public void persist1(Object object) {
        em.persist(object);
    }

    public void persist2(Object object) {
        em.persist(object);
    }

    public void persist3(Object object) {
        em.persist(object);
    }

}
