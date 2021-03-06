/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import ejb.session.card.CardAcctSessionBeanLocal;
import entity.dams.account.CustomerDepositAccount;
import entity.common.TransactionRecord;
import entity.customer.MainAccount;
import entity.dams.account.CustomerFixedDepositAccount;
import entity.dams.account.DepositAccount;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.ConstantUtils;
import server.utilities.DateUtils;
import server.utilities.EnumUtils;
import server.utilities.GenerateAccountAndCCNumber;
import util.exception.dams.DepositAccountNotFoundException;
import util.exception.dams.DuplicateDepositAccountException;
import util.exception.dams.UpdateDepositAccountException;

/**
 *
 * @author leiyang
 */
@Stateless
public class CustomerDepositSessionBean implements CustomerDepositSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @EJB
    private DepositProductSessionBeanLocal depositProductBean;
    @EJB
    private CardAcctSessionBeanLocal cardBean;

    @Override
    public DepositAccount getAccountFromId(String accountNumber) throws DepositAccountNotFoundException {

        if (accountNumber == null) {
            throw new DepositAccountNotFoundException("Deposit Account Not Found!");
        }

        try {

            DepositAccount da = em.find(DepositAccount.class, accountNumber);
            if (da == null) {
                throw new DepositAccountNotFoundException("Deposit Account Not Found!");
            }
            // REMARK: See if we need to check cancelstatus
            return da;
        } catch (IllegalArgumentException e) {
            throw new DepositAccountNotFoundException("Deposit Account Not Found!");
        }
    }

    @Override
    public DepositAccount createAccount(DepositAccount account) throws DuplicateDepositAccountException {
        try {
            if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
                TransactionRecord t = new TransactionRecord();
                t.setAmount(account.getBalance());
                t.setCredit(Boolean.TRUE);
                t.setActionType(EnumUtils.TransactionType.INITIAL);
                t.setReferenceNumber(generateReferenceNumber());
                account.addTransaction(t);
                if (account instanceof CustomerDepositAccount) {
                    ((CustomerDepositAccount) account).setPreviousBalance(account.getBalance());
                }
                if (account.getAccountNumber() == null) {
                    account.setAccountNumber(generateAccountNumber());
                } else {
                    DepositAccount c = em.find(DepositAccount.class, account.getAccountNumber());
                    if (c != null) {
                        throw new DuplicateDepositAccountException("Deposit Account already exist.");
                    }
                }
                em.persist(account);
                t.setFromAccount(account);
                em.merge(t);
            } else {
                if (account instanceof CustomerDepositAccount) {
                    ((CustomerDepositAccount) account).setPreviousBalance(account.getBalance());
                }
                if (account.getAccountNumber() == null) {
                    account.setAccountNumber(generateAccountNumber());
                } else {
                    DepositAccount c = em.find(DepositAccount.class, account.getAccountNumber());
                    if (c != null) {
                        throw new DuplicateDepositAccountException("Deposit Account already exist.");
                    }
                }
                em.persist(account);
            }

            return account;
        } catch (EntityExistsException e) {
            throw new DuplicateDepositAccountException("Deposit Account already exist.");
        }
    }

    private String generateAccountNumber() {
        String accountNumber = "";
        for (;;) {
            accountNumber = GenerateAccountAndCCNumber.generateAccountNumber();
            DepositAccount a = em.find(DepositAccount.class, accountNumber);
            if (a == null) {
                return accountNumber;
            }
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

    @Override
    public DepositAccount updateAccount(DepositAccount account) throws UpdateDepositAccountException {

        try {

            if (account.getAccountNumber() == null) {
                throw new UpdateDepositAccountException("Not an entity!");
            }

            if (account.getStatus() == EnumUtils.StatusType.CLOSED) {
                account.setCloseDate(new Date());
            }

            em.merge(account);
            return account;
        } catch (IllegalArgumentException e) {
            throw new UpdateDepositAccountException("Not an entity!");
        }
    }

    @Override
    public CustomerDepositAccount updateCustomerDepositAccount(CustomerDepositAccount account) {
        try {
            if (account.getStatus() == EnumUtils.StatusType.CLOSED) {
                account.setCloseDate(new Date());
            }
            em.merge(account);
            return account;
        } catch (EntityExistsException e) {
            return null;
        }
    }

    @Override
    public List<DepositAccount> showAllAccounts() {
        Query q = em.createQuery("SELECT ba FROM DepositAccount ba");
        return q.getResultList();
    }

    @Override
    public List<DepositAccount> showAllActiveAccounts() {
        Query q = em.createQuery("SELECT ba FROM DepositAccount ba WHERE ba.status =:inStatus");
        q.setParameter("inStatus", EnumUtils.StatusType.ACTIVE);
        return q.getResultList();
    }

    @Override
    public List<DepositAccount> getAllCustomerAccounts(String mainAccountId) {
        Query q = em.createQuery("SELECT ba FROM DepositAccount ba WHERE ba.mainAccount.id =:mainAccountId AND ba.status <> :status");
        q.setParameter("mainAccountId", mainAccountId);
        q.setParameter("status", EnumUtils.StatusType.CLOSED);
        return q.getResultList();
    }

    @Override
    public CustomerDepositAccount getDaytoDayAccountByMainAccount(MainAccount ma) {
        Query q = em.createQuery("SELECT ba FROM CustomerDepositAccount ba WHERE ba.mainAccount.userID =:mainAccountId AND ba.type = :inType");
        q.setParameter("mainAccountId", ma.getUserID());
        q.setParameter("inType", EnumUtils.DepositAccountType.CURRENT);
        List<CustomerDepositAccount> results = q.getResultList();
        if (results.size() > 0) {
            return results.get(0);
        } else {
            // create a default one
            CustomerDepositAccount cda = new CustomerDepositAccount();
            cda.setType(EnumUtils.DepositAccountType.CURRENT);
            cda.setProduct(depositProductBean.getDepositProductByName(ConstantUtils.DEMO_CURRENT_DEPOSIT_PRODUCT_NAME));
            cda.setBalance(BigDecimal.ZERO);
            cda.setStatus(EnumUtils.StatusType.ACTIVE);
            cda.setMainAccount(ma);
            cda.setAccountNumber(generateAccountNumber());
            em.persist(cda);
            return cda;
        }
    }

    @Override
    public List<CustomerDepositAccount> getAllNonFixedCustomerAccounts(String mainAccountId) {
        Query q = em.createQuery("SELECT ba FROM CustomerDepositAccount ba WHERE ba.mainAccount.id =:mainAccountId");
        q.setParameter("mainAccountId", mainAccountId);
        return q.getResultList();
    }

    @Override
    public List<CustomerFixedDepositAccount> getAllFixedCustomerAccounts(String mainAccountId) {
        Query q = em.createQuery("SELECT ba FROM CustomerFixedDepositAccount ba WHERE ba.mainAccount.id =:mainAccountId");
        q.setParameter("mainAccountId", mainAccountId);
        return q.getResultList();
    }

    @Override
    public String depositIntoAccount(String accountNumber, BigDecimal depositAmount) {
        if (accountNumber == null) {
            return "Account Number Cannot be Empty!";
        }
        if (depositAmount == null) {
            return "Your Deposit Amount is Empty!";
        }
        DepositAccount ba = em.find(DepositAccount.class, accountNumber);
        if (ba == null) {
            return "Account Not Found";
        } else {
            TransactionRecord t = new TransactionRecord();
            t.setActionType(EnumUtils.TransactionType.DEPOSIT);
            t.setAmount(depositAmount);
            t.setCredit(Boolean.TRUE);
            t.setFromAccount(ba);
            t.setReferenceNumber(generateReferenceNumber());
            ba.addTransaction(t);
            ba.addBalance(depositAmount);
            em.merge(ba);
            return "SUCCESS";
        }
    }

    @Override
    public DepositAccount transferToAccount(DepositAccount account, BigDecimal amount) {
        TransactionRecord t = new TransactionRecord();
        t.setActionType(EnumUtils.TransactionType.TRANSFER);
        t.setAmount(amount);
        t.setCredit(Boolean.TRUE);
        t.setToAccount(account);
        t.setReferenceNumber(generateReferenceNumber());
        account.addTransaction(t);
        account.addBalance(amount);
        em.merge(account);
        return account;
    }

    @Override
    public String withdrawFromAccount(String accountNumber, BigDecimal withdrawAmount) {
        if (accountNumber == null) {
            return "Account Number Cannot be Empty!";
        }
        if (withdrawAmount == null) {
            return "Your Withdraw Amount is Empty!";
        }
        DepositAccount ba = em.find(DepositAccount.class, accountNumber);
        if (ba == null) {
            return "Account Not Found";
        } else {
            if (ba.getBalance().compareTo(withdrawAmount) < 0) {
                return "Withdraw Failed! Balance not enough! Current balance is:" + ba.getBalance();
            } else {
                TransactionRecord t = new TransactionRecord();
                t.setActionType(EnumUtils.TransactionType.WITHDRAW);
                t.setAmount(withdrawAmount);
                t.setCredit(Boolean.FALSE);
                t.setFromAccount(ba);
                t.setReferenceNumber(generateReferenceNumber());
                ba.addTransaction(t);
                ba.removeBalance(withdrawAmount);
                em.merge(ba);
                return "SUCCESS";
            }
        }
    }

    @Override
    public DepositAccount transferFromAccount(DepositAccount account, BigDecimal amount) {
        int res = account.getBalance().compareTo(amount);
        if (res == -1) {
            return null;
        } else {
            System.out.println("Updateing account transferFromAccount");
            TransactionRecord t = new TransactionRecord();
            t.setActionType(EnumUtils.TransactionType.TRANSFER);
            t.setAmount(amount);
            t.setCredit(Boolean.FALSE);
            t.setFromAccount(account);
            t.setReferenceNumber(generateReferenceNumber());
            account.addTransaction(t);
            account.removeBalance(amount);
            em.merge(account);
            return account;
        }
    }

    @Override
    public DepositAccount depositIntoAccount(DepositAccount account, BigDecimal depositAmount) {
        if (account == null || depositAmount == null) {
            return null;
        } else {
            TransactionRecord t = new TransactionRecord();
            t.setActionType(EnumUtils.TransactionType.DEPOSIT);
            t.setAmount(depositAmount);
            t.setCredit(Boolean.TRUE);
            t.setFromAccount(account);
            t.setReferenceNumber(generateReferenceNumber());
            account.addTransaction(t);
            account.addBalance(depositAmount);
            em.merge(account);
            return account;
        }
    }

    @Override
    public DepositAccount demoDepositIntoAccount(DepositAccount account, BigDecimal depositAmount) {
        if (account == null || depositAmount == null) {
            return null;
        } else {
            TransactionRecord t = new TransactionRecord();
            t.setCreationDate(new Date());
            t.setActionType(EnumUtils.TransactionType.DEPOSIT);
            t.setAmount(depositAmount);
            t.setCredit(Boolean.TRUE);
            t.setFromAccount(account);
            t.setReferenceNumber(generateReferenceNumber());
            account.addTransaction(t);
            account.addBalance(depositAmount);
            em.merge(account);
            return account;
        }
    }

    @Override
    public DepositAccount withdrawFromAccount(DepositAccount account, BigDecimal withdrawAmount) {
        if (account == null || withdrawAmount == null) {
            return null;
        } else {
            if (account.getBalance().compareTo(withdrawAmount) < 0) {
                return null;
            } else {
                TransactionRecord t = new TransactionRecord();
                t.setActionType(EnumUtils.TransactionType.WITHDRAW);
                t.setAmount(withdrawAmount);
                t.setCredit(Boolean.FALSE);
                t.setFromAccount(account);
                t.setReferenceNumber(generateReferenceNumber());
                account.addTransaction(t);
                account.removeBalance(withdrawAmount);
                em.merge(account);
                return account;
            }
        }
    }

    @Override
    public DepositAccount creditSalaryIntoAccount(DepositAccount account, BigDecimal depositAmount) {
        if (account == null || depositAmount == null) {
            return null;
        } else {
            TransactionRecord t = new TransactionRecord();
            t.setActionType(EnumUtils.TransactionType.SALARY);
            t.setAmount(depositAmount);
            t.setCredit(Boolean.TRUE);
            t.setFromAccount(account);
            t.setReferenceNumber(generateReferenceNumber());
            account.addTransaction(t);
            account.addBalance(depositAmount);
            em.merge(account);
            return account;
        }
    }

    @Override
    public DepositAccount payBillFromAccount(DepositAccount account, BigDecimal payAmount) {
        if (account == null || payAmount == null) {
            return null;
        } else {
            int res = account.getBalance().compareTo(payAmount);
            if (res == -1) {
                return null;
            } else {
                TransactionRecord t = new TransactionRecord();
                t.setActionType(EnumUtils.TransactionType.BILL);
                t.setAmount(payAmount);
                t.setCredit(Boolean.FALSE);
                t.setFromAccount(account);
                t.setReferenceNumber(generateReferenceNumber());
                account.addTransaction(t);
                account.removeBalance(payAmount);
                em.merge(account);
                return account;
            }
        }
    }

    @Override
    public DepositAccount ccSpendingFromAccount(DepositAccount account, BigDecimal spendAmount) {
        if (account == null || spendAmount == null) {
            return null;
        } else {
            int res = account.getBalance().compareTo(spendAmount);
            if (res == -1) {
                return null;
            } else {
                TransactionRecord t = new TransactionRecord();
                t.setActionType(EnumUtils.TransactionType.CCSPENDING);
                t.setAmount(spendAmount);
                t.setCredit(Boolean.FALSE);
                t.setFromAccount(account);
                t.setReferenceNumber(generateReferenceNumber());
                account.addTransaction(t);
                account.removeBalance(spendAmount);
                em.merge(account);
                return account;
            }
        }
    }

    @Override
    public DepositAccount investFromAccount(DepositAccount account, BigDecimal investAmount) {
        if (account == null || investAmount == null) {
            return null;
        } else {
            int res = account.getBalance().compareTo(investAmount);
            if (res == -1) {
                return null;
            } else {
                TransactionRecord t = new TransactionRecord();
                t.setActionType(EnumUtils.TransactionType.INVEST);
                t.setAmount(investAmount);
                t.setCredit(Boolean.FALSE);
                t.setFromAccount(account);
                t.setReferenceNumber(generateReferenceNumber());
                account.addTransaction(t);
                account.removeBalance(investAmount);
                em.merge(account);
                return account;
            }
        }
    }

    @Override
    public DepositAccount creditInterestAccount(DepositAccount account) {
        BigDecimal interestAmount = account.getCumulatedInterest().getCummulativeAmount();
        System.out.println("Interest Amount: " + interestAmount + " and greater than 0.001: " + interestAmount.compareTo(new BigDecimal(0.001)));
        if (interestAmount.compareTo(new BigDecimal(0.001)) < 0) {
            return null;
        } else {
            TransactionRecord t = new TransactionRecord();
            t.setActionType(EnumUtils.TransactionType.INTEREST);
            t.setAmount(interestAmount);
            t.setCredit(Boolean.TRUE);
            t.setFromAccount(account);
            t.setReferenceNumber(generateReferenceNumber());
            account.addTransaction(t);
            account.addBalance(interestAmount);
            account.getCumulatedInterest().reset();
            em.merge(account);
            em.flush();
            return account;
        }
    }

    @Override
    public List<TransactionRecord> transactionRecordFromAccountNumber(String accountNumber) {
        Query q = em.createQuery("SELECT tr FROM TransactionRecord tr WHERE tr.toAccount.accountNumber =:accountNumber OR tr.fromAccount.accountNumber =:accountNumber ORDER BY tr.creationDate DESC");
        q.setParameter("accountNumber", accountNumber);
        return q.getResultList();
    }

    @Override
    public TransactionRecord latestTransactionFromAccountNumber(String accountNumber) {
        Query q = em.createQuery("SELECT tr FROM TransactionRecord tr WHERE tr.toAccount.accountNumber =:accountNumber OR tr.fromAccount.accountNumber =:accountNumber ORDER BY tr.creationDate DESC");
        q.setParameter("accountNumber", accountNumber);
        List<TransactionRecord> result = q.getResultList();
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    @Override
    public String payCCBillFromAccount(String accountNumber, String ccNumber, BigDecimal amount) {

        try {
            DepositAccount fromAccount = getAccountFromId(accountNumber);
            
            if (fromAccount.getBalance().compareTo(amount) < 0) {
                return "Mobile Account Balance not enough. Please Top up first!";
            } else {

                cardBean.payCreditCardAccountBillByCardNumber(ccNumber, amount);
                ccSpendingFromAccount(fromAccount, amount);

                return "SUCCESS";
            }
        } catch (DepositAccountNotFoundException e) {
            return "Account not found";
        }
    }
}
