/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionbean;

import caller.CreditCardDTO;
import entity.VisaCardTransaction;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.smartcardio.CardChannel;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import utils.NfcDevice;
import utils.OTPUtils;

/**
 *
 * @author wang
 */
@Stateless
public class CardTransactionSessionBean implements CardTransactionSessionBeanLocal {

    private final String CLEARING_PATH = "https://localhost:8181/StaffInternalSystem/rest/credit_card_clearing";
    private final String AUTHORIZATION_PATH = "https://localhost:8181/StaffInternalSystem/rest/credit_card_authorization";

    @PersistenceContext(unitName = "CreditCardSimulatorPU")
    private EntityManager em;

    @Override
    public List<VisaCardTransaction> getListVisaCardTransactions() {
        Query q = em.createQuery("SELECT vct FROM VisaCardTransaction vct");
        return q.getResultList();
    }

    @Override
    public List<Long> getListVisaCardTransactionIdsByStatus(Boolean status) {
        Query q = em.createQuery("SELECT vct.id FROM VisaCardTransaction vct WHERE vct.settledStatus =:inStatus");
        q.setParameter("inStatus", status);
        return q.getResultList();
    }

    @Override
    public VisaCardTransaction updateVisaCardTransactionSettledStatusById(Long Id) {
        Query q = em.createQuery("SELECT vct FROM VisaCardTransaction vct WHERE vct.id =:inId");
        q.setParameter("inId", Id);

        VisaCardTransaction vct = (VisaCardTransaction) q.getSingleResult();
        vct.setSettledStatus(Boolean.TRUE);
        em.merge(vct);
        return vct;
    }

    @Override
    public Double calculateNetSettlement() {
        Query q = em.createNativeQuery("SELECT SUM(amount) FROM VisaCardTransaction WHERE settledstatus = FALSE GROUP BY settledstatus");

        Double result;
        try {
            result = (Double) q.getSingleResult();
        } catch (Exception ex) {
            result = 0.0;
        }

        return result;
    }

    @Override
    public void sendSuccessAuthorization(String transactionAmount) {

        String ccNum = readCardCCNumber();

        if (ccNum != null) {

            Form form = new Form();
            form.param("ccNumber", ccNum);
            form.param("ccAmount", transactionAmount); // 500 is daily limit and 1000 for monthly limist, current out standing is 800
            form.param("ccTcode", "MDS");
            form.param("ccDescription", "Test Success authorization");

            System.out.println("Calling bank to check credit card transaction");
            System.out.println("Credit Card number " + ccNum);
            System.out.println("Requesting amount " + transactionAmount);

            // Start calling
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(AUTHORIZATION_PATH);

            // This is the response
            JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
            CreditCardDTO cc = new CreditCardDTO(jsonString);

            if (!cc.getAuthorizationCode().equals("-1") && !cc.getAuthorizationCode().equals("-2")) {
                // check code validation
                System.out.println("Getting Authorizaed response with code: " + cc.getAuthorizationCode());
                String returnCode = OTPUtils.generateSingleToken(cc.getCreditCardNumber(), Integer.parseInt(cc.getAuthorizationCode()));
                // call for clearing
                sendSuccessClearing(returnCode, cc.getAuthorizationCode(), transactionAmount);
            } else {
                System.out.println(cc.getMessage());
            }
        } else {
            System.out.println("Fail to read Credit Card card values");
        }
    }

    private String readCardCCNumber() {
        System.out.println("*** Start nfc device ***");
        CardChannel channel = NfcDevice.initializeDevice();
        try {
            return NfcDevice.readCard(channel);
        } catch (Exception ex) {
            System.out.println("readCardCCNumber Error");
            System.out.println(ex);
            return null;
        }
    }

    private void sendSuccessClearing(String returnCode, String aCode, String transactionAmount) {
        String ccNum = readCardCCNumber();
        Long visaCardTransactionId = System.currentTimeMillis();

        if (ccNum != null) {
            Form form = new Form();
            form.param("ccVisaId", Long.toString(visaCardTransactionId));
            form.param("token", returnCode);
            form.param("aCode", aCode);
            form.param("ccNumber", ccNum);
            form.param("ccAmount", transactionAmount); // 500 is daily limit and 1000 for monthly limist, current out standing is 800
            form.param("ccTcode", "MDS");
            form.param("ccDescription", "Test Success Clearing");

            System.out.println("Calling bank to check credit card transaction");
            System.out.println("Credit Card number " + ccNum);
            System.out.println("Requesting amount " + transactionAmount);
            System.out.println("Requesting with token:" + returnCode);

            // Start calling
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(CLEARING_PATH);

            // This is the response
            JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
//            CreditCardDTO cc = new CreditCardDTO(jsonString);
            VisaCardTransaction vct = new VisaCardTransaction(jsonString, visaCardTransactionId);
            em.persist(vct);
            System.out.println(vct.getMessage());
        } else {
            System.out.println("Fail to read Credit Card card values");
        }
    }

}
