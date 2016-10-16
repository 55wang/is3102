/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.webservice;

import entity.common.BillTransferRecord;
import entity.common.TransferRecord;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import protocal.swift.MT103;
import protocal.swift.SwiftMessage;

/**
 *
 * @author leiyang
 */
@Stateless
public class WebserviceSessionBean implements WebserviceSessionBeanLocal {
    
    private final String MEPS_SETTLEMENT_AGENCY = "https://localhost:8181/MEPSSimulator/meps/meps_settlement_agency";
    private final String SACH_TRANSFER_CLEARING = "https://localhost:8181/SACHSimulator/sach/sach_transfer_clearing";
    private final String SACH_BILLING_CLEARING = "https://localhost:8181/SACHSimulator/sach/sach_billing_clearing";
    private final String FAST_TRANSFER_CLEARING = "https://localhost:8181/FASTSimulator/fast/fast_transfer_clearing";

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
    
    @Asynchronous
    @Override
    public void transferClearingSACH(TransferRecord tr) {
        System.out.println("Clearing transfer");
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
    
    @Asynchronous
    @Override
    public void billingClearingSACH(BillTransferRecord btr) {
        System.out.println("Clearing transfer");
        Form form = new Form(); //bank info
        form.param("referenceNumber", btr.getReferenceNumber());
        form.param("amount", btr.getAmount().toString());
        form.param("partnerBankCode", btr.getPartnerBankCode()); // other bank
        form.param("shortCode", btr.getShortCode());
        form.param("fromBankCode", "001");
        form.param("organizationName", btr.getOrganizationName());
        form.param("billReferenceNumber", btr.getBillReferenceNumber());

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(SACH_BILLING_CLEARING);

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
    public void transferClearingFAST(TransferRecord tr) {
        
        System.out.println("Generating transfer");
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
        form.param("FAST", "false");
        
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(FAST_TRANSFER_CLEARING);

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
    public void transferSWIFT(TransferRecord tr) {
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
    }
}
