/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.webservice;

import ejb.session.bill.BillSessionBeanLocal;
import entity.bill.BankEntity;
import entity.bill.BillFundTransferRecord;
import entity.common.BillTransferRecord;
import entity.common.TransferRecord;
import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
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
import protocal.swift.MT103;
import protocal.swift.SwiftMessage;
import server.utilities.ConstantUtils;

/**
 *
 * @author leiyang
 */
@Stateless
public class WebserviceSessionBean implements WebserviceSessionBeanLocal {

    @EJB
    private BillSessionBeanLocal billSessionBean;

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    private final String MEPS_SETTLEMENT_AGENCY = "https://" + ConstantUtils.ipAddress + ":8181/MEPSSimulator/meps/meps_settlement_agency";
    private final String SACH_TRANSFER_CLEARING = "https://" + ConstantUtils.ipAddress + ":8181/SACHSimulator/sach/sach_transfer_clearing";
    private final String SACH_BILLING_CLEARING = "https://" + ConstantUtils.ipAddress + ":8181/SACHSimulator/sach/sach_billing_clearing";
    private final String SACH_SWIFT_TRANSFER = "https://" + ConstantUtils.ipAddress + ":8181/SACHSimulator/sach/sach_swift_transfer";
    private final String FAST_TRANSFER_CLEARING = "https://" + ConstantUtils.ipAddress + ":8181/FASTSimulator/fast/fast_transfer_clearing";

    @Asynchronous
    @Override
    public void paySACHSettlement(String netSettlementAmount, String fromBankCode, String toBankCode, String agencyCode) {
        System.out.println("Paying Settlement");
        // send to MEPS+
        Form form = new Form(); //bank info
        form.param("fromBankCode", fromBankCode);// MBS is 001
        form.param("toBankCode", toBankCode);
        form.param("agencyCode", agencyCode); // SACH is 000
        form.param("netSettlementAmount", netSettlementAmount);
        form.param("referenceNumber", "");

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MEPS_SETTLEMENT_AGENCY);

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
    public void payFASTSettlement(String netSettlementAmount, String fromBankCode, String toBankCode, String agencyCode, String referenceNumber) {
        System.out.println("Paying Settlement");
        // send to MEPS+
        Form form = new Form(); //bank info
        form.param("fromBankCode", fromBankCode);// MBS is 001
        form.param("toBankCode", toBankCode);
        form.param("agencyCode", agencyCode); // FAST is 111
        form.param("netSettlementAmount", netSettlementAmount);
        form.param("referenceNumber", referenceNumber);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MEPS_SETTLEMENT_AGENCY);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        System.out.println(jsonString);

        if (jsonString.getString("message").equals("SUCCESS")) {
            System.out.println("Request received");
        } else {
            System.out.println("FAIL");
        }
    }

    @Override
    public void transferClearingSACH(TransferRecord tr) {
        System.out.println("[MBS]:");
        System.out.println("Generating IBG transfer...");
        
        Form form = new Form(); //bank info
        form.param("referenceNumber", tr.getReferenceNumber());
        form.param("amount", tr.getAmount().toString());
        form.param("toBankCode", tr.getToBankCode()); // other bank
        form.param("toBranchCode", tr.getToBranchCode());
        form.param("fromBankCode", "001");
        form.param("accountNumber", tr.getAccountNumber());
        form.param("toName", tr.getName());
        form.param("fromName", tr.getFromName());
        form.param("myInitial", tr.getMyInitial());

        System.out.println("Sending IBG transfer...");
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(SACH_TRANSFER_CLEARING);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);

        BillFundTransferRecord bft = new BillFundTransferRecord();
        bft.setReferenceNumber(tr.getReferenceNumber());
        bft.setAmount(tr.getAmount());
        bft.setToBankCode(tr.getToBankCode());
        bft.setToBankAccount(tr.getAccountNumber());
        bft.setFromBankCode("001");
        bft.setSettled(Boolean.FALSE);
        bft.setCreationDate(new Date());

        billSessionBean.createBillFundTransferRecord(bft);
        if (jsonString.getString("message").equals("SUCCESS")) {
            System.out.println(".");
            System.out.println("[MBS]:");
            System.out.println("Received response from SACH...");
            
        } else {
            System.out.println("FAIL");
        }
    }

    @Asynchronous
    @Override
    public void billingClearingSACH(BillTransferRecord btr) {
        System.out.println("[MBS]:");
        System.out.println("Generating payment instruction...");

        Form form = new Form(); //bank info
        form.param("referenceNumber", btr.getReferenceNumber());
        form.param("amount", btr.getAmount().toString());
        form.param("partnerBankCode", btr.getPartnerBankCode()); // other bank
        form.param("partnerBankAccount", btr.getPartnerBankAccount());
        form.param("shortCode", btr.getShortCode());
        form.param("fromBankCode", "001");
        form.param("organizationName", btr.getOrganizationName());
        form.param("billReferenceNumber", btr.getBillReferenceNumber());

        System.out.println("Sending payment instruction...");
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(SACH_BILLING_CLEARING);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);

        BillFundTransferRecord bft = new BillFundTransferRecord();
        bft.setReferenceNumber(btr.getReferenceNumber());
        bft.setAmount(btr.getAmount());
        bft.setBillReferenceNumber(btr.getBillReferenceNumber());
        bft.setToBankCode(btr.getPartnerBankCode());
        bft.setToBankAccount(btr.getPartnerBankAccount());
        bft.setShortCode(btr.getShortCode());
        bft.setFromBankCode("001");
        bft.setOrganizationName(btr.getOrganizationName());
        bft.setSettled(Boolean.FALSE);
        bft.setCreationDate(new Date());

        billSessionBean.createBillFundTransferRecord(bft);

        if (jsonString.getString("message").equals("SUCCESS")) {
            System.out.println(".");
            System.out.println("[MBS]:");
            System.out.println("Received response from SACH...");
            
        } else {
            System.out.println("FAIL");
        }
    }

    @Asynchronous
    @Override
    public void transferClearingFAST(TransferRecord tr) {
        System.out.println("[MBS]:");
        System.out.println("Generating FAST transfer...");
        
        Form form = new Form(); //bank info
        form.param("referenceNumber", tr.getReferenceNumber());
        form.param("amount", tr.getAmount().toString());
        form.param("toBankCode", tr.getToBankCode()); // other bank
        form.param("toBankName", findBankNameByBankCode(tr.getToBankCode())); // other bank
        form.param("toBranchCode", tr.getToBranchCode());
        form.param("fromBankCode", "001");
        form.param("fromBankName", "Merlion Bank Singapore"); // other bank
        form.param("accountNumber", tr.getAccountNumber());
        form.param("toName", tr.getName());
        form.param("fromName", tr.getFromName());
        form.param("myInitial", tr.getMyInitial());

        System.out.println("Sending FAST transfer...");
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(FAST_TRANSFER_CLEARING);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
        
        BillFundTransferRecord bft = new BillFundTransferRecord();
        bft.setReferenceNumber(tr.getReferenceNumber());
        bft.setAmount(tr.getAmount());
        bft.setToBankCode(tr.getToBankCode());
        bft.setToBankAccount(tr.getAccountNumber());
        bft.setFromBankCode("001");
        bft.setSettled(Boolean.FALSE);
        bft.setCreationDate(new Date());

        billSessionBean.createBillFundTransferRecord(bft);
        
        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        if (jsonString.getString("message").equals("SUCCESS")) {
            System.out.println(".");
            System.out.println("[MBS]:");
            System.out.println("Received response from SACH...");
        } else {
            System.out.println("FAIL");
        }
    }

    @Asynchronous
    @Override
    public void transferSWIFT(TransferRecord tr) {
        System.out.println("[MBS]:");
        System.out.println("Generating SWIFT MT103 message...");

        MT103 message = new MT103();
        message.setBankOperationCode(tr.getToBankCode());
        message.setBeneficiaryCustomer(tr.getName());
        message.setOrderingCustomer(tr.getFromName());
        message.setValueCurrencyInterbankSettledAmount(tr.getAmount().toString());
        message.setSenderReference(tr.getMyInitial());
        SwiftMessage sm = new SwiftMessage();
        sm.setMessageType("103");
        sm.setMessage(message.toString());
        System.out.println(sm.toString());

        Form form = new Form(); //bank info
        form.param("referenceNumber", tr.getReferenceNumber());
        form.param("amount", tr.getAmount().toString());
        form.param("delegatingBank", "005"); // citibank
        form.param("toName", tr.getName());
        form.param("fromBankCode", "001");
        form.param("fromName", tr.getFromName());
        form.param("myInitial", tr.getMyInitial());
        form.param("swiftMessage", sm.toString());

        System.out.println("Sending SWIFT message to SACH...");
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(SACH_SWIFT_TRANSFER);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);

        BillFundTransferRecord bft = new BillFundTransferRecord();
        bft.setReferenceNumber(tr.getReferenceNumber());
        bft.setAmount(tr.getAmount());
        bft.setToBankCode("005");
        bft.setFromBankCode("001");
        bft.setSettled(Boolean.FALSE);
        bft.setCreationDate(new Date());

        billSessionBean.createBillFundTransferRecord(bft);

        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        if (jsonString.getString("message").equals("SUCCESS")) {
            System.out.println(".");
            System.out.println("[MBS]:");
            System.out.println("Received response from SACH...");
        } else {
            System.out.println("FAIL");
        }
    }

    public void persist(Object object) {
        em.persist(object);
    }

    public String findBankNameByBankCode(String bankCode) {
        Query q = em.createQuery("SELECT b FROM BankEntity b WHERE b.bankCode =:bankCode");
        q.setParameter("bankCode", bankCode);
        BankEntity be = (BankEntity) q.getSingleResult();
        return be.getName();
    }
}
