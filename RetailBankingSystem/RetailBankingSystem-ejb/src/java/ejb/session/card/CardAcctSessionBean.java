/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.card;

import entity.card.account.CardTransaction;
import entity.card.account.CreditCardAccount;
import entity.card.account.DebitCardAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.DepositAccount;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.DateUtils;
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
    public Date setOverDueDateAndMPD(CreditCardAccount cca) {
        cca.setMinPayDue(cca.calculateMinPayDue());
        Calendar cal = Calendar.getInstance();
        cal = DateUtils.addDaysToDate(cal, 15);
        cca.setOverDueDate(cal.getTime());
        updateCreditCardAccount(cca);
        return cca.getOverDueDate();
    }

    @Override
    public List<CreditCardAccount> updateListDemoPaidMPD() {
        //set MPD = 0 and deduct outstanding
        //assume paid amount is $60
        Double paidAmount = 60.0;

        //need to change, retrieve cca by accountId and deduct instead of all accounts
        List<CreditCardAccount> ccas = getListCreditCardAccountsByActiveOrFreezeCardStatus();
        for (CreditCardAccount cca : ccas) {

            if (paidAmount >= cca.getMinPayDue()) {
                cca.setMinPayDue(0.0);
                cca.setOutstandingAmount(cca.getOutstandingAmount() - paidAmount);
            } else if (paidAmount < cca.getMinPayDue()) {
                cca.setMinPayDue(cca.getMinPayDue() - paidAmount);
                cca.setOutstandingAmount(cca.getOutstandingAmount() - paidAmount);
            }
            updateCreditCardAccount(cca);
        }
        return ccas;
    }

    @Override
    public List<CreditCardAccount> calculateDueCreditCardAccount() {

        List<CreditCardAccount> ccas = getListCreditCardAccountsByActiveOrFreezeCardStatus();

        for (CreditCardAccount cca : ccas) {
            //if fail to pay MPD
            if (cca.getMinPayDue() > 0) {
                if (cca.getOutstandingAmount() > 0) {
                    //it is overdue
                    cca.setLatePaymentFee(60.0);
                    System.out.println(cca.getCreditCardNum() + ": Total Outstanding Amount: " + cca.getOutstandingAmount() + " + " + cca.getInterestAmount() + " + " + cca.getLatePaymentFee());
                    addInterestToOutStandingAmount(cca);
                    addLatePaymentToOutstandingAmount(cca);
                    cca.setLatePaymentFee(0.0);

                    //add bad credit record
                    cca.setNumOfLatePayment(cca.getNumOfLatePayment() + 1);
                    cca.setLastLatePaymentDate(new Date());
                    if (cca.getOverDueDays() >= 30 && cca.getOverDueDays() <= 59) {
                        cca.setNumOf_30_59_LatePayment(cca.getNumOf_30_59_LatePayment() + 1);
                    } else if (cca.getOverDueDays() >= 60 && cca.getOverDueDays() <= 89) {
                        cca.setNumOf_60_89_LatePayment(cca.getNumOf_60_89_LatePayment() + 1);
                    } else if (cca.getOverDueDays() >= 90) {
                        cca.setNumOf_90_LatePayment(cca.getNumOf_90_LatePayment() + 1);
                    }
                    cca.setMinPayDue(0.0);
                    updateCreditCardAccount(cca);

                }
            } else if (cca.getMinPayDue() == 0) {
                //paid MPD only, still got outstandingamount;
                if (cca.getOutstandingAmount() > 0) {
                    System.out.println(cca.getCreditCardNum() + ": Total Outstanding Amount: " + cca.getOutstandingAmount() + " + " + cca.getInterestAmount());
                    addInterestToOutStandingAmount(cca);
                    //clear overdue dates
                    cca.setOverDueDate(null);
                    cca.setMinPayDue(0.0);
                    updateCreditCardAccount(cca);
                }
            } else if (cca.getMinPayDue() == 0 && cca.getOutstandingAmount() == 0) {
                //No outstanding amount
                //clear overdue dates
                cca.setOverDueDate(null);
                cca.setMinPayDue(0.0);
                updateCreditCardAccount(cca);

                System.out.println(cca.getCreditCardNum() + ": Total Outstanding Amount: " + cca.getOutstandingAmount() + " + " + cca.getInterestAmount());
            }
        }
        return ccas;
    }

    @Override
    public List<CreditCardAccount> updateListDemoPaidOutstandingAmountCcas() {
        //set both MPD and outstanding = 0
        List<CreditCardAccount> ccas = getListCreditCardAccountsByActiveOrFreezeCardStatus();
        for (CreditCardAccount cca : ccas) {
            cca.setMinPayDue(0.0);
            cca.setOutstandingAmount(0.0);
            updateCreditCardAccount(cca);
        }
        return ccas;
    }

    @Override
    public List<CreditCardAccount> updateListInterestToOutstandingAmountCcas() {
        List<CreditCardAccount> ccas = getListCreditCardAccountsByActiveOrFreezeCardStatus();
        for (CreditCardAccount cca : ccas) {
            addInterestToOutStandingAmount(cca);
        }
        return ccas;
    }

    @Override
    public Double addInterestToOutStandingAmount(CreditCardAccount cca) {
        cca.setOutstandingAmount(cca.getOutstandingAmount() + cca.calculateCurrentMonthlyInterest());
        updateCreditCardAccount(cca);
        return cca.getOutstandingAmount();
    }

    @Override
    public List<CreditCardAccount> updateListLatePaymentToOutstandingAmountCcas() {
        List<CreditCardAccount> ccas = getListCreditCardAccountsByActiveOrFreezeCardStatus();
        for (CreditCardAccount cca : ccas) {
            addLatePaymentToOutstandingAmount(cca);
        }
        return ccas;
    }

    @Override
    public Double addLatePaymentToOutstandingAmount(CreditCardAccount cca) {
        cca.setOutstandingAmount(cca.getOutstandingAmount() + cca.getLatePaymentFee());
        updateCreditCardAccount(cca);
        return cca.getOutstandingAmount();
    }

    @Override
    public Double addCurrentMonthAmountToOutstandingAmount(CreditCardAccount cca) {
        cca.setOutstandingAmount(cca.getOutstandingAmount() + cca.getCurrentMonthAmount());
        cca.setCurrentMonthAmount(0.0);
        //set minpaydue amount
        cca.setMinPayDue(cca.calculateMinPayDue());

        cca = updateCreditCardAccount(cca);
        return cca.getOutstandingAmount();
    }

    @Override
    public List<CreditCardAccount> getListCreditCardAccountsByActiveOrFreezeCardStatus() {
        Query q = em.createQuery("SELECT cca FROM CreditCardAccount cca WHERE cca.CardStatus =:inStatusOne OR cca.CardStatus =:inStatusTwo");
        q.setParameter("inStatusOne", CardAccountStatus.ACTIVE);
        q.setParameter("inStatusTwo", CardAccountStatus.FREEZE);
        return q.getResultList();
    }

    @Override
    public List<CreditCardAccount> getAllActiveCreditCardAccountsByMainId(String id) {
        Query q = em.createQuery("SELECT cca FROM CreditCardAccount cca WHERE cca.CardStatus =:inStatus AND cca.mainAccount.id =:mainAccountId");
        q.setParameter("inStatus", CardAccountStatus.ACTIVE);
        q.setParameter("mainAccountId", id);
        return q.getResultList();
    }

    @Override
    public CreditCardAccount updateCreditCardAccount(CreditCardAccount cca) {
        if (cca.getCardStatus() == CardAccountStatus.CLOSED) {
            cca.setCloseDate(new Date());
        }
        em.merge(cca);
        return cca;
    }

    @Override
    public List<CreditCardAccount> getListCreditCardAccountsByIdAndNotStatus(String id, CardAccountStatus status) {
        System.out.println("Status:" + status + " and id:" + id);
        Query q = em.createQuery("SELECT cca FROM CreditCardAccount cca WHERE cca.CardStatus != :inStatus AND cca.mainAccount.id =:id");
        q.setParameter("inStatus", status);
        q.setParameter("id", id);
        return q.getResultList();
    }

    @Override
    public List<CreditCardAccount> getListCreditCardAccountsByCardStatusAndAppStatus(CardAccountStatus cardAccountStatus, EnumUtils.CardApplicationStatus cardApplicationStatus) {
        Query q = em.createQuery("SELECT cca FROM CreditCardAccount cca WHERE cca.CardStatus = :cardStatus AND cca.creditCardOrder.applicationStatus =:appStatus");
        q.setParameter("cardStatus", cardAccountStatus);
        q.setParameter("appStatus", cardApplicationStatus);
        return q.getResultList();
    }

    @Override
    public List<DebitCardAccount> getListDebitCardAccountsByIdAndNotStatus(String id, CardAccountStatus status) {
        System.out.println("Status:" + status + " and id:" + id);
        Query q = em.createQuery("SELECT cca FROM DebitCardAccount cca WHERE cca.CardStatus != :inStatus AND cca.customerDepositAccount.mainAccount.id =:id");
        q.setParameter("inStatus", status);
        q.setParameter("id", id);
        return q.getResultList();
    }

    @Override
    public List<DebitCardAccount> getListDebitCardAccountsByStatus(CardAccountStatus status) {
        Query q = em.createQuery("SELECT cca FROM DebitCardAccount cca WHERE cca.CardStatus = :inStatus");
        q.setParameter("inStatus", status);
        return q.getResultList();
    }

    @Override
    public CreditCardAccount getCardAccountById(Long cardID) {
        return em.find(CreditCardAccount.class, cardID);
    }

    @Override
    public CreditCardAccount getCreditCardAccountByCardNumber(String cardNumber) {
        Query q = em.createQuery("SELECT cca FROM CreditCardAccount cca WHERE cca.creditCardNum = :cardNumber");
        q.setParameter("cardNumber", cardNumber);
        return (CreditCardAccount) q.getSingleResult();
    }

    @Override
    public CreditCardAccount payCreditCardAccountBillByCardNumber(String cardNumber, BigDecimal amount) {
        CreditCardAccount ccAccount = getCreditCardAccountByCardNumber(cardNumber);

        CardTransaction t = new CardTransaction();
        t.setAmount(amount.doubleValue());
        t.setCardTransactionStatus(EnumUtils.CardTransactionStatus.SETTLEDTRANSACTION);
        t.setCreditCardAccount(ccAccount);
        t.setIsCredit(Boolean.TRUE);
        em.persist(t);

        ccAccount.payOutstandingAmount(amount);
        ccAccount.addTransactions(t);
        em.merge(ccAccount);

        return ccAccount;
    }

    @Override
    public CreditCardAccount createCardAccount(CreditCardAccount cca) {
        try {
            if (cca.getCreditCardNum() == null || cca.getCreditCardNum().isEmpty()) {
                cca.setCreditCardNum(generateMasterCardNumber());
                cca.setCvv(generateCVVNumber());
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
    public DebitCardAccount updateDebitAccount(DebitCardAccount dca) {
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
            dca.setCvv(server.utilities.CommonHelper.generateRandom(true, 3));
            dca.setCardStatus(EnumUtils.CardAccountStatus.APPROVED);
            Calendar cal = Calendar.getInstance();
            dca.setCreationDate(cal.getTime());
            cal.set(Calendar.YEAR, 2);
            dca.setValidDate(cal.getTime());
            dca.setNameOnCard(da.getMainAccount().getCustomer().getFullName());
            dca.setCustomerDepositAccount((CustomerDepositAccount) da);
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Query q = em.createQuery("SELECT c FROM CreditCardAccount c WHERE c.creditCardNum =:cardNumber AND c.CardStatus =:inStatus");
            q.setParameter("inStatus", CardAccountStatus.ISSUED);
            q.setParameter("cardNumber", cardNumber);

            CreditCardAccount cca = (CreditCardAccount) q.getSingleResult();

            String formattedBirthdayToCheck = sdf.format(birthday);
            String formattedBirthdayInDB = sdf.format(cca.getMainAccount().getCustomer().getBirthDay());

            if (cca.getCvv().equals(cvv) == false) {
                return null;
            } else if (formattedBirthdayToCheck.equals(formattedBirthdayInDB) == false) {

                return null;
            } else if (identityNumber.equals(cca.getMainAccount().getCustomer().getIdentityNumber()) == false) {

                return null;
            } else {

                cca.setCardStatus(CardAccountStatus.ACTIVE);
                return cca;
            }
        } catch (Exception ex) {
            return null;
        }

    }

    @Override
    public DebitCardAccount activateDebitCard(String identityNumber, Date birthday, String cardNumber, String cvv) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

            Query q = em.createQuery("SELECT c FROM DebitCardAccount c WHERE c.creditCardNum =:cardNumber AND c.CardStatus =:inStatus");
            q.setParameter("cardNumber", cardNumber);
            q.setParameter("inStatus", CardAccountStatus.ISSUED);

            DebitCardAccount dca = (DebitCardAccount) q.getSingleResult();
            System.out.println(dca);

            String formattedBirthdayToCheck = sdf.format(birthday);
            String formattedBirthdayInDB = sdf.format(dca.getCustomerDepositAccount().getMainAccount().getCustomer().getBirthDay());

            if (dca.getCvv().equals(cvv) == false) {
                return null;
            } else if (formattedBirthdayToCheck.equals(formattedBirthdayInDB) == false) {
                return null;
            } else if (identityNumber.equals(dca.getCustomerDepositAccount().getMainAccount().getCustomer().getIdentityNumber()) == false) {
                return null;
            } else {
                dca.setCardStatus(CardAccountStatus.ACTIVE);
                return dca;
            }
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
