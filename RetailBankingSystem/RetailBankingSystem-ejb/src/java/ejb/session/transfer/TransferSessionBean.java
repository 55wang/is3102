/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.transfer;

import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.bill.Payee;
import entity.dams.account.DepositAccount;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    
    @Override
    public String transferFromAccountToAccount(String fromAcc, String toAcc, BigDecimal amount) {
        DepositAccount fromAccount = depositBean.getAccountFromId(fromAcc);
        DepositAccount toAccount = depositBean.getAccountFromId(toAcc);
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            // not enough money
            return "FAIL";
        } else {
            depositBean.withdrawFromAccount(fromAccount, amount);
            depositBean.depositIntoAccount(toAccount, amount);
            return "SUCCESS";
        }
    }
    
    // payee
    @Override
    public Payee createPayee(Payee p) {
        em.persist(p);
        return p;
    }
    @Override
    public List<Payee> getPayeeFromUserIdWithType(Long userId, EnumUtils.PayeeType type) {
        Query q = em.createQuery("SELECT p FROM Payee p WHERE p.mainAccount.id =:userId AND p.type = :inType");
        q.setParameter("userId", userId);
        q.setParameter("inType", type);
        return q.getResultList();
    }
}
