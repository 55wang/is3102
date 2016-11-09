/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bill;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.bill.Payee;
import entity.common.BillTransferRecord;
import entity.common.TransactionRecord;
import entity.common.TransferRecord;
import entity.customer.MainAccount;
import entity.customer.TransferLimits;
import entity.dams.account.DepositAccount;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.ConstantUtils;
import server.utilities.DateUtils;
import server.utilities.EnumUtils;

/**
 *
 * @author leiyang
 */
@Stateless
public class TransferSessionBean implements TransferSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private CardAcctSessionBeanLocal cardBean;

    @Override
    public TransferRecord createTransferRecord(TransferRecord tr) {
        em.persist(tr);
        return tr;
    }

    @Override
    public BillTransferRecord createBillTransferRecord(BillTransferRecord btr) {
        em.persist(btr);
        return btr;
    }

    @Override
    public List<TransactionRecord> getTransactionRecordByAccountNumberStartDateEndDate(String accountNumber, Date startDate, Date endDate) {
        Query q = em.createQuery(
                "SELECT t FROM " + ConstantUtils.TRANSACTION_ENTITY + " t WHERE "
                + "t.fromAccount.accountNumber = :accountNumber AND "
                + "t.creationDate BETWEEN :startDate AND :endDate"
        );
        q.setParameter("accountNumber", accountNumber);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }

    @Override
    public List<TransferRecord> getAllTransactionRecordStartDateEndDateByType(Date startDate, Date endDate, EnumUtils.PayeeType inType) {
        Query q = em.createQuery(
                "SELECT t FROM TransferRecord t WHERE "
                + "t.type =:inType AND "
                + "t.creationDate BETWEEN :startDate AND :endDate"
        );
        q.setParameter("inType", inType);
        q.setParameter("startDate", startDate);
        q.setParameter("endDate", endDate);
        return q.getResultList();
    }

    @Override
    public String transferFromAccountToAccount(String fromAcc, String toAcc, BigDecimal amount) {
        DepositAccount fromAccount = depositBean.getAccountFromId(fromAcc);
        DepositAccount toAccount = depositBean.getAccountFromId(toAcc);
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            // not enough money
            return "FAIL";
        } else {
            depositBean.transferFromAccount(fromAccount, amount);
            depositBean.transferToAccount(toAccount, amount);
            return "SUCCESS";
        }
    }

    @Override
    public String transferFromAccountToCreditCard(String fromAcc, String ccNo, BigDecimal amount) {
        DepositAccount fromAccount = depositBean.getAccountFromId(fromAcc);
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            // not enough money
            return "FAIL";
        } else {
            depositBean.ccSpendingFromAccount(fromAccount, amount);
            cardBean.payCreditCardAccountBillByCardNumber(ccNo, amount);
            return "SUCCESS";
        }
    }

    @Override
    public TransferLimits createTransferLimits(TransferLimits t) {
        em.persist(t);
        return t;
    }

    @Override
    public TransferLimits updateTransferLimits(TransferLimits t) {
        em.merge(t);
        return t;
    }

    @Override
    public BigDecimal getTodayBankTransferAmount(MainAccount ma, EnumUtils.PayeeType inType) {
        List<DepositAccount> dps = ma.getBankAcounts();
        if (dps.isEmpty()) {
            return BigDecimal.ZERO;
        } else {
            String queryString = "SELECT t FROM TransferRecord t WHERE (";
            for (int i = 0; i < dps.size(); i++) {
                if (i == 0) {
                    queryString += " t.fromAccount.accountNumber = " + dps.get(i).getAccountNumber();
                } else {
                    queryString += " OR t.fromAccount.accountNumber = " + dps.get(i).getAccountNumber();
                }
            }
            queryString += ")";
            Query q = em.createQuery(queryString + " AND t.type =:inType  AND t.creationDate BETWEEN :startDate AND :endDate");
            Date startDate = DateUtils.getBeginOfDay();
            Date endDate = DateUtils.getEndOfDay();
            q.setParameter("startDate", startDate);
            q.setParameter("endDate", endDate);
            q.setParameter("inType", inType);
            List<TransferRecord> records = q.getResultList();
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (TransferRecord t : records) {
                totalAmount = totalAmount.add(t.getAmount());
            }
            return totalAmount;
        }
    }

    // payee
    @Override
    public Payee createPayee(Payee p) {
        em.persist(p);
        return p;
    }

    @Override
    public String deletePayee(Payee p) {
        em.remove(p);
        return "SUCCESS";
    }

    @Override
    public String deletePayeeById(Long id) {
        Payee p = em.find(Payee.class, id);
        if (p == null) {
            return "FAIL";
        } else {
            return deletePayee(p);
        }
    }

    @Override
    public List<Payee> getPayeeFromUserIdWithType(String userId, EnumUtils.PayeeType type) {
        Query q = em.createQuery("SELECT p FROM Payee p WHERE p.mainAccount.id =:userId AND p.type = :inType");
        q.setParameter("userId", userId);
        q.setParameter("inType", type);
        return q.getResultList();
    }

    @Override
    public Payee getPayeeById(Long id) {
        return em.find(Payee.class, id);
    }
}
