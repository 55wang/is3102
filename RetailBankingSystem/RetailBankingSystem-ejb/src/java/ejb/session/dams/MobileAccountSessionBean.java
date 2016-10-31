/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import ejb.session.card.CardAcctSessionBeanLocal;
import entity.common.TransactionRecord;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import entity.dams.account.MobileAccount;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils;
import server.utilities.GenerateAccountAndCCNumber;

/**
 *
 * @author leiyang
 */
@Stateless
@LocalBean
public class MobileAccountSessionBean implements MobileAccountSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private CardAcctSessionBeanLocal cardBean;

    @Override
    public MobileAccount createMobileAccount(MainAccount ma) {
        MobileAccount mobileAccount = new MobileAccount();
        mobileAccount.setMainAccount(ma);
        mobileAccount.setAccountNumber(generateAccountNumber(ma));
        mobileAccount.setStatus(EnumUtils.StatusType.ACTIVE);
        mobileAccount.setType(EnumUtils.DepositAccountType.MOBILE);
        mobileAccount.setPhoneNumber(ma.getCustomer().getPhone());
        em.persist(mobileAccount);
        return mobileAccount;
    }

    @Override
    public MobileAccount updateMobileAccount(MobileAccount ma) {
        return em.merge(ma);
    }

    @Override
    public MobileAccount getMobileAccountByUserId(String userId) {
        Query q = em.createQuery("SELECT ma FROM MobileAccount ma WHERE ma.mainAccount.userID =:userId");
        q.setParameter("userId", userId);
        List<MobileAccount> result = q.getResultList();
        if (result.size() == 1) {
            return result.get(0);
        } else {
            return null;
        }
    }

    @Override
    public MobileAccount getMobileAccountByMobileNumber(String number) {
        return em.find(MobileAccount.class, number);
    }

    @Override
    public MobileAccount topupMobileAccount(MobileAccount ma, DepositAccount da, BigDecimal amount) {
        if (da.getBalance().compareTo(amount) >= 0) {
            ma.addBalance(amount);
            da.removeBalance(amount);
            TransactionRecord t = new TransactionRecord();
            t.setAmount(amount);
            t.setCredit(Boolean.TRUE);
            t.setActionType(EnumUtils.TransactionType.TOPUP);
            t.setFromAccount(da);
            t.setToAccount(ma);
            t.setReferenceNumber(generateReferenceNumber());
            da.addTransaction(t);
            ma.addTransaction(t);
            em.merge(da);
            return em.merge(ma);
        } else {
            return null;
        }
    }
    
    @Override
    public String payCCBillFromMobileAccount(String mobileNumber, String ccNumber, BigDecimal amount) {
        MobileAccount fromMobileAccount = getMobileAccountByMobileNumber(mobileNumber);
        if (fromMobileAccount == null) {
            return "Account not found";
        } else if (fromMobileAccount.getBalance().compareTo(amount) < 0) {
            return "Mobile Account Balance not enough. Please Top up first!";
        } else {
            cardBean.payCreditCardAccountBillByCardNumber(ccNumber, amount);
            
            fromMobileAccount.removeBalance(amount);
            TransactionRecord t = new TransactionRecord();
            t.setAmount(amount);
            t.setCredit(Boolean.TRUE);
            t.setActionType(EnumUtils.TransactionType.CCSPENDING);
            t.setFromAccount(fromMobileAccount);
            t.setReferenceNumber(generateReferenceNumber());
            fromMobileAccount.addTransaction(t);
            em.merge(fromMobileAccount);
            return "SUCCESS";
        }
    }

    @Override
    public TransactionRecord latestTransactionFromMobileAccount(MobileAccount ma) {
        Query q = em.createQuery("SELECT tr FROM TransactionRecord tr WHERE tr.toAccount =:mobileAccount OR tr.fromAccount =:mobileAccount ORDER BY tr.creationDate DESC");
        q.setParameter("mobileAccount", ma);
        List<TransactionRecord> result = q.getResultList();
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
    
    @Override
    public TransactionRecord latestTransactionFromMobileNumber(String mobileNumber) {
        Query q = em.createQuery("SELECT tr FROM TransactionRecord tr WHERE tr.toAccount.accountNumber =:mobileNumber OR tr.fromAccount.accountNumber =:mobileNumber ORDER BY tr.creationDate DESC");
        q.setParameter("mobileNumber", mobileNumber);
        List<TransactionRecord> result = q.getResultList();
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    @Override
    public String transferFromAccountToAccount(String from, String to, BigDecimal amount) {
        MobileAccount fromMobileAccount = getMobileAccountByMobileNumber(from);
        MobileAccount toMobileAccount = getMobileAccountByMobileNumber(to);
        if (fromMobileAccount == null || toMobileAccount == null) {
            return "Account not found";
        } else if (fromMobileAccount.getBalance().compareTo(amount) < 0) {
            return "Amount not enough";
        } else {
            fromMobileAccount.removeBalance(amount);
            toMobileAccount.addBalance(amount);
            TransactionRecord t = new TransactionRecord();
            t.setAmount(amount);
            t.setCredit(Boolean.TRUE);
            t.setActionType(EnumUtils.TransactionType.TRANSFER);
            t.setFromAccount(fromMobileAccount);
            t.setToAccount(toMobileAccount);
            t.setReferenceNumber(generateReferenceNumber());
            toMobileAccount.addTransaction(t);
            fromMobileAccount.addTransaction(t);
            em.merge(fromMobileAccount);
            em.merge(toMobileAccount);
            return "SUCCESS";
        }
    }

    private String generateReferenceNumber() {
        String referenceNumber = "";
        for (;;) {
            referenceNumber = GenerateAccountAndCCNumber.generateReferenceNumber();
            TransactionRecord a = em.find(TransactionRecord.class, referenceNumber);
            if (a == null) {
                return referenceNumber;
            }
        }
    }

    private String generateAccountNumber(MainAccount ma) {
        return ma.getCustomer().getPhone();
    }
}
