/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import dto.TransactionDTO;
import dto.TransactionSummaryDTO;
import mb.caller.CreditCardDTO;
import entity.VisaCardTransaction;
import entity.VisaSettlement;
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
    
    public final String ipAddress = "192.168.0.103";

    private final String CLEARING_PATH = "https://" + ipAddress +":8181/StaffInternalSystem/rest/credit_card_clearing";
    private final String AUTHORIZATION_PATH = "https://" + ipAddress +":8181/StaffInternalSystem/rest/credit_card_authorization";
    private final String MEPS_SETTLEMENT = "https://" + ipAddress +":8181/MEPSSimulator/meps/meps_settlement";

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
    public VisaSettlement persistSettlement(VisaSettlement vs) {
        em.persist(vs);
        return vs;
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

    public void clearNetSettlements() {
        Query q = em.createQuery("SELECT vs FROM VisaSettlement vs");
        List<VisaSettlement> result = q.getResultList();
        for (VisaSettlement s : result) {
            s.setAmount(0.0);
            em.merge(s);
        }

    }

    public List<VisaSettlement> getSettlements() {
        Query q = em.createQuery("SELECT vs FROM VisaSettlement vs");
        return q.getResultList();
    }

    public void updateVisaSettlement(VisaCardTransaction vct) {
        List<VisaSettlement> settlements = getSettlements();

        if (settlements.isEmpty()) {
            System.out.println("Entity builder failed");
        }
        for (VisaSettlement s : settlements) {

            if (vct.getFromBankCode().equals(s.getFromBankCode()) && vct.getToBankCode().equals(s.getToBankCode())) {
                s.setAmount(s.getAmount() + Double.valueOf(vct.getAmount()));
                em.merge(s);
            }

            if (vct.getFromBankCode().equals(s.getToBankCode()) && vct.getToBankCode().equals(s.getFromBankCode())) {
                s.setAmount(s.getAmount() - Double.valueOf(vct.getAmount()));
                em.merge(s);
            }

        }
    }

    @Override
    public void sendSuccessAuthorization(String transactionAmount, String creditCardNumber, String referenceNum, String requestBankCode) {

        String ccNum;
        if (creditCardNumber.equals("")) {
            System.out.println("Read from hardware");
            ccNum = readCardCCNumber();
        } else {
            System.out.println("Read from input box");
            ccNum = creditCardNumber;
        }

        if (ccNum != null) {

            Form form = new Form();
            form.param("ccNumber", ccNum);
            form.param("ccAmount", transactionAmount); // 500 is daily limit and 1000 for monthly limist, current out standing is 800
            form.param("referenceNum", referenceNum);
            form.param("fromBankCode", requestBankCode);
            form.param("ccNumber", ccNum);

            form.param("ccTcode", "MDS");
            form.param("ccDescription", "Test Success authorization");

            System.out.println("---------------Card Transaction---------------");
            System.out.println("[VISA]");
            System.out.println("Received card transaction from acquirer's bank... ");
            System.out.println(".        Reference Num: " + referenceNum);
            System.out.println(".        Credit Card number: " + creditCardNumber);
            System.out.println(".        Transaction Amount: " + transactionAmount);
            System.out.println(".        Request Bank Code: " + requestBankCode);

            System.out.println(".");
            System.out.println("Sending to MBS for authorization...");
            // Start calling
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(AUTHORIZATION_PATH);

            // This is the response
            JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
            CreditCardDTO cc = new CreditCardDTO(jsonString);

            if (!cc.getAuthorizationCode().equals("-1") && !cc.getAuthorizationCode().equals("-2")) {
                // check code validation
                System.out.println(".");
                System.out.println("[VISA]");
                System.out.println("Received authorization from MBS... ");
                System.out.println("Getting Authorizaed response with code: " + cc.getAuthorizationCode());
                String returnCode = OTPUtils.generateSingleToken(cc.getCreditCardNumber(), Integer.parseInt(cc.getAuthorizationCode()));
                // call for clearing
                sendSuccessClearing(returnCode, cc.getAuthorizationCode(), transactionAmount, creditCardNumber, referenceNum, requestBankCode);

            } else {
                System.out.println(".");
                System.out.println("[VISA]");
                System.out.println("Received failed response from MBS... ");
                System.out.println("Sending failed response failed responose to acquirer's bank... ");
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

    private void sendSuccessClearing(String returnCode, String aCode, String transactionAmount, String creditCardNumber, String referenceNum, String toBankCode) {

        String ccNum;
        if (creditCardNumber.equals("")) {
            System.out.println("Read from hardware");
            ccNum = readCardCCNumber();
        } else {
            System.out.println("Read from input box");
            ccNum = creditCardNumber;
        }

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

//            System.out.println("Calling bank to check credit card transaction");
//            System.out.println("Credit Card number " + ccNum);
//            System.out.println("Requesting amount " + transactionAmount);
//            System.out.println("Requesting with token:" + returnCode);
            // Start calling
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(CLEARING_PATH);

            // This is the response
            JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), JsonObject.class);
//            CreditCardDTO cc = new CreditCardDTO(jsonString);
            VisaCardTransaction vct = new VisaCardTransaction(jsonString, visaCardTransactionId, referenceNum, toBankCode);
            em.persist(vct);
            System.out.println(vct.getMessage());

            List<VisaSettlement> bankAccounts = getSettlements();
            System.out.println("Current net settlement:");
            for (VisaSettlement s : bankAccounts) {
                if (s.getAmount() < 0.0) {
                    System.out.println(".       " + s.getToBankCode() + " " + s.getToBankName() + " to " + s.getFromBankCode() + " " + s.getFromBankName() + ": " + s.getAmount());
                } else {
                    System.out.println(".       " + s.getFromBankCode() + " " + s.getFromBankName() + " to " + s.getToBankCode() + " " + s.getToBankName() + ": " + s.getAmount());

                }
            }

            System.out.println("Updating net settlement...");
            updateVisaSettlement(vct);

            List<VisaSettlement> updatedbankAccounts = getSettlements();

            System.out.println("Updated net settlement:");
            for (VisaSettlement s : updatedbankAccounts) {
                if (s.getAmount() < 0.0) {
                    System.out.println(".       " + s.getToBankCode() + " " + s.getToBankName() + " to " + s.getFromBankCode() + " " + s.getFromBankName() + ": " + s.getAmount());
                } else {
                    System.out.println(".       " + s.getFromBankCode() + " " + s.getFromBankName() + " to " + s.getToBankCode() + " " + s.getToBankName() + ": " + s.getAmount());

                }
            }
            System.out.println("[VISA]");
            System.out.println("Sedind authorization to requesting bank...");

        } else {
            System.out.println("Fail to read Credit Card card values");
        }
    }

    @Asynchronous
    public void sendMEPSNetSettlement() {
        List<VisaSettlement> settlements = getSettlements();
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
        summary.setCitiSettlementAmount(settlements.get(0).getAmount().toString());

        summary.setOcbcFromBankCode(settlements.get(1).getFromBankCode());
        summary.setOcbcFromBankName(settlements.get(1).getFromBankName());
        summary.setOcbcToBankCode(settlements.get(1).getToBankCode());
        summary.setOcbcToBankName(settlements.get(1).getToBankName());
        summary.setOcbcSettlementAmount(settlements.get(1).getAmount().toString());
        summary.setDate(new Date().toString());

        System.out.println("----------------VISA Settlement to MBS----------------");
        System.out.println("[VISA]:");
        System.out.println("Sending Net Settlement Amount to MEPS...");

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(MEPS_SETTLEMENT);

        clearNetSettlements();
        updateTransactions(transactions);
        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).post(Entity.entity(summary, MediaType.APPLICATION_JSON), JsonObject.class);

        if (jsonString.getString("message").equals("SUCCESS")) {
            System.out.println(".");
            System.out.println("[VISA]");
            System.out.println("Received successful response from MEPS");

        } else {
            System.out.println("FAIL");
        }
    }

    public List<String> getTodayMBSTransactions() {
        List<String> transactions = new ArrayList<>();
        Date startDate = getBeginOfDay();
        Date endDate = getEndOfDay();
        Query q = em.createQuery("SELECT v FROM VisaCardTransaction v WHERE ( v.toBankCode =:billToCode OR v.fromBankCode =:billFromCode) "
                + " AND (v.creationDate BETWEEN :startDate AND :endDate)");
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        q.setParameter("billToCode", "001");
        q.setParameter("billFromCode", "001");

        List<VisaCardTransaction> bts = q.getResultList();
        for (VisaCardTransaction bt : bts) {
            transactions.add(bt.getReferenceNum());
        }
        return transactions;
    }

    private void updateTransactions(List<String> transactions) {
        for (String tr : transactions) {
            Query q = em.createQuery("SELECT v FROM VisaCardTransaction v WHERE v.referenceNum=:tr");
            q.setParameter("tr", tr);
            VisaCardTransaction vct = (VisaCardTransaction) q.getSingleResult();
            vct.setSettledStatus(Boolean.TRUE);
            em.merge(vct);

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

}
