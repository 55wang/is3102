/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CreditCardAccount;
import entity.card.account.DebitCardAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.DepositAccount;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.CardAccountStatus;
import server.utilities.GenerateAccountAndCCNumber;
import server.utilities.PincodeGenerationUtils;

/**
 *
 * @author wang
 */
@Stateless
public class CardAcctSessionBean implements CardAcctSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public CreditCardAccount updateCreditCardAccount(CreditCardAccount cca) {
        em.merge(cca);
        return cca;
    }

    @Override
    public List<CreditCardAccount> getListCreditCardAccountsByIdAndNotStatus(Long id, CardAccountStatus status) {
        System.out.println("Status:" + status + " and id:" + id);
        Query q = em.createQuery("SELECT cca FROM CreditCardAccount cca WHERE cca.CardStatus != :inStatus AND cca.mainAccount.id =:id");
        q.setParameter("inStatus", status);
        q.setParameter("id", id);
        return q.getResultList();
    }

    @Override
    public List<CreditCardAccount> getListCreditCardAccountsByCardStatusAndAppStatus(CardAccountStatus cardAccountStatus, EnumUtils.ApplicationStatus cardApplicationStatus) {
        Query q = em.createQuery("SELECT cca FROM CreditCardAccount cca WHERE cca.CardStatus = :cardStatus AND cca.creditCardOrder.applicationStatus =:appStatus");
        q.setParameter("cardStatus", cardAccountStatus);
        q.setParameter("appStatus", cardApplicationStatus);
        return q.getResultList();
    }

    @Override
    public List<DebitCardAccount> getListDebitCardAccountsByIdAndNotStatus(Long id, CardAccountStatus status) {
        System.out.println("Status:" + status + " and id:" + id);
        Query q = em.createQuery("SELECT cca FROM DebitCardAccount cca WHERE cca.CardStatus != :inStatus AND cca.customerDepositAccount.mainAccount.id =:id");
        q.setParameter("inStatus", status);
        q.setParameter("id", id);
        return q.getResultList();
    }

    @Override
    public CreditCardAccount getCardAccountById(Long cardID) {
        return em.find(CreditCardAccount.class, cardID);
    }

    @Override
    public CreditCardAccount getCreditCardAccountByCardNumber(String cardNumber) {
        System.out.println("EJB getCardByCardNumber " + cardNumber);
        Query q = em.createQuery("SELECT cca FROM CreditCardAccount cca WHERE cca.creditCardNum = :cardNumber");
        q.setParameter("cardNumber", cardNumber);
        return (CreditCardAccount) q.getSingleResult();
    }

    @Override
    public CreditCardAccount createCardAccount(CreditCardAccount cca) {
        try {
            if (cca.getCreditCardNum() == null || cca.getCreditCardNum().isEmpty()) {
                cca.setCreditCardNum(generateMasterCardNumber());
                cca.setCvv(Integer.parseInt(generateCVVNumber()));
            }
            System.out.println("Saving cca:" + cca.getCreditCardNum());
            System.out.println("cca:" + cca.getPartialHiddenAccountNumber());
            em.persist(cca);
            return cca;
        } catch (Exception e) {
            //always print an error msg 
            System.out.println("NewCardSessionBean.createCardAccount Error");
            System.out.println(e);
            return null;
        }
    }

    //update cardaccount status 
    @Override
    public CreditCardAccount updateCardAccountStatus(CreditCardAccount cca, CardAccountStatus status) {
        cca.setCardStatus(status);
        em.merge(cca);
        return cca;
    }

    @Override
    public DebitCardAccount updateDebitAccountStatus(DebitCardAccount dca, CardAccountStatus status) {
        dca.setCardStatus(status);
        em.merge(dca);
        return dca;
    }

    @Override
    public CreditCardAccount updateCardAcctTransactionDailyLimit(CreditCardAccount cca, double newDailyLimit) {
        cca.setTransactionDailyLimit(newDailyLimit);
        em.merge(cca);
        return cca;
    }

    // redeem credit card reward generate code
    @Override
    public DebitCardAccount createDebitAccount(CustomerDepositAccount da) {
        try {
//            DepositAccount da = em.find(DepositAccount.class, depositAccountId);
            DebitCardAccount dca = new DebitCardAccount();
            dca.setCreditCardNum(generateAccountNumber());
            dca.setCvv(Integer.parseInt(server.utilities.CommonHelper.generateRandom(true, 3)));
            dca.setCardStatus(EnumUtils.CardAccountStatus.PENDING);
            Calendar cal = Calendar.getInstance();
            dca.setCreationDate(cal.getTime());
            cal.set(Calendar.YEAR, 2);
            dca.setValidDate(cal.getTime());
            dca.setCustomerDepositAccount((CustomerDepositAccount) da);
            dca.setNameOnCard(da.getMainAccount().getCustomer().getFullName());
            em.persist(dca);

            return dca;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    //change to return creditcard account
    @Override
    public CreditCardAccount activateCreditCard(String identityNumber, Date birthday, String cardNumber, String cvv) {
        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

            Query q = em.createQuery("SELECT c FROM CreditCardAccount c WHERE c.creditCardNum =:cardNumber AND c.CardStatus =:inStatus");
            q.setParameter("inStatus", CardAccountStatus.PENDING);
            q.setParameter("cardNumber", cardNumber);

            CreditCardAccount cca = (CreditCardAccount) q.getSingleResult();

//            String formattedBirthdayToCheck = sdf.format(birthday);
//            String formattedBirthdayInDB = sdf.format(cca.getMainAccount().getCustomer().getBirthDay());
//
//            if (Integer.toString(cca.getCvv()).equals(cvv) == false) {
//                return null;
//            } else if (formattedBirthdayToCheck.equals(formattedBirthdayInDB) == false) {
//                return null;
//            } else if (identityNumber.equals(cca.getMainAccount().getCustomer().getIdentityNumber()) == false) {
//                return null;
//            } else {
            cca.setCardStatus(CardAccountStatus.ACTIVE);
            return cca;
//            }
        } catch (Exception ex) {
            return null;
        }

    }

    @Override
    public DebitCardAccount activateDebitCard(String identityNumber, Date birthday, String cardNumber, String cvv) {
        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

            System.out.println(identityNumber + " " + birthday + " " + cardNumber + " " + cvv);
            Query q = em.createQuery("SELECT c FROM DebitCardAccount c WHERE c.creditCardNum =:cardNumber");
            q.setParameter("cardNumber", cardNumber);

            DebitCardAccount dca = (DebitCardAccount) q.getSingleResult();
            System.out.println(dca);

//            String formattedBirthdayToCheck = sdf.format(birthday);
//            String formattedBirthdayInDB = sdf.format(dca.getCustomerDepositAccount().getMainAccount().getCustomer().getBirthDay());
//
//            if (Integer.toString(dca.getCvv()).equals(cvv) == false) {
//                return null;
//            } else if (formattedBirthdayToCheck.equals(formattedBirthdayInDB) == false) {
//                return null;
//            } else if (identityNumber.equals(dca.getCustomerDepositAccount().getMainAccount().getCustomer().getIdentityNumber()) == false) {
//                return null;
//            } else {
            dca.setCardStatus(CardAccountStatus.ACTIVE);
            return dca;
//            }
        } catch (Exception ex) {
            return null;
        }

    }

    private String generateMasterCardNumber() {
        String ccNumber = "";
        for (;;) {
            ccNumber = GenerateAccountAndCCNumber.generateMasterCardNumber();
            CreditCardAccount a = null;
            try {
                a = getCreditCardAccountByCardNumber(ccNumber);
            } catch (Exception e) {
                System.out.println("No cc number found in database");
            }
            if (a == null) {
                return ccNumber;
            }
        }
    }

    private String generateCVVNumber() {
        return PincodeGenerationUtils.generateRandom(true, 3);
    }

    private String generateAccountNumber() {
        for (;;) {
            String accountNumber = GenerateAccountAndCCNumber.generateMasterCardNumber();
            DepositAccount a = em.find(DepositAccount.class, accountNumber);
            if (a == null) {
                return accountNumber;
            }
        }
    }

}
