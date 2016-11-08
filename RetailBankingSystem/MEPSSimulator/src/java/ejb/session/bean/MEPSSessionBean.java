/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bean;

import dto.TransactionDTO;
import dto.TransactionSummaryDTO;
import entity.SettlementAccount;
import java.math.BigDecimal;
import java.util.Date;
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
public class MEPSSessionBean {

    private final String SACH_INFORM_NET_SETTLEMENT = "https://localhost:8181/SACHSimulator/sach/sach_inform_settlement";
    private final String FAST_INFORM_NET_SETTLEMENT = "https://localhost:8181/FASTSimulator/fast/fast_inform_settlement";
    private final String MBS_NET_SETTLEMENT_PATH = "https://localhost:8181/StaffInternalSystem/rest/net_settlement";

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

    public List<SettlementAccount> updateSettlementAccountsBalance(String citiFromBankCode, String citiToBankCode, String citiSettlementAmount, String ocbcFromBankCode, String ocbcToBankCode, String ocbcSettlementAmount) {
        List<SettlementAccount> accounts = retrieveThreeSettlementAccounts(citiFromBankCode, citiToBankCode, ocbcToBankCode);
        for (SettlementAccount sa : accounts) {
            if (sa.getBankCode().equals(citiToBankCode)) {
                sa.setAmount(sa.getAmount().add(new BigDecimal(citiSettlementAmount)));
            } 
            if (sa.getBankCode().equals(ocbcToBankCode)) {
                sa.setAmount(sa.getAmount().add(new BigDecimal(ocbcSettlementAmount)));
            } 
            if (sa.getBankCode().equals(citiFromBankCode)) {
                sa.setAmount(sa.getAmount().subtract(new BigDecimal(citiSettlementAmount)));
            }
            if (sa.getBankCode().equals(ocbcFromBankCode)) {
                sa.setAmount(sa.getAmount().subtract(new BigDecimal(ocbcSettlementAmount)));
            } 
        }
        return accounts;
    }

    public List<SettlementAccount> retrieveThreeSettlementAccounts(String mbsCode, String citiCode, String ocbcCode) {
        Query q = em.createQuery("SELECT sa FROM SettlementAccount sa WHERE sa.bankCode = :bc1 OR sa.bankCode = :bc2 OR sa.bankCode = :bc3 ");
        q.setParameter("bc1", mbsCode);
        q.setParameter("bc2", citiCode);
        q.setParameter("bc3", ocbcCode);

        return q.getResultList();
    }

    public void sendMBSNetSettlement(TransactionSummaryDTO transactionSummary){
        // send to mbs

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MBS_NET_SETTLEMENT_PATH);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(transactionSummary, MediaType.APPLICATION_JSON), JsonObject.class);

        if (jsonString != null && jsonString.getString("error").equals("SUCCESS")) {

            System.out.println(".");
            System.out.println("[MEPS]");
            System.out.println("Received response from MBS..");
        } else {
            System.out.println("FAIL");
        }
    }
    
    public void testMBS(){

        // send to mbs
        System.out.println("Testing");
        TransactionSummaryDTO summary = new TransactionSummaryDTO();
        TransactionDTO dto = new TransactionDTO();
        dto.setReferenceNumber("TEST1234");
        summary.getTransactionSummary().add(dto);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://localhost:8181/StaffInternalSystem/rest/test_json");

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(summary, MediaType.APPLICATION_JSON), JsonObject.class);

        if (jsonString != null && jsonString.getString("error").equals("SUCCESS")) {

            System.out.println(".");
            System.out.println("[MEPS]");
            System.out.println("Received response from MBS..");
        } else {
            System.out.println("FAIL");
        }
    }

}
