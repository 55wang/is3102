/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BatchProcess;

import entity.DepositAccount;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.math.BigDecimal;

/**
 *
 * @author litong
 */
@Stateless
public class InterestAccrualSessionBean implements InterestAccrualSessionBeanLocal {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    private EntityManager em;
    
    public List<DepositAccount> getAccountList() {
        Query q = em.createQuery("SELECT DISTINCT * FROM BankAccount");
        return q.getResultList();
    }
    
    public Long getCurrentInterestRate(BigDecimal balance) {
        if (balance.compareTo(new BigDecimal(1000)) == -1) {
         // get interest rate    
        }
        return null;
    }
    
    public Long getSavingsInterestRate(BigDecimal balance, BigDecimal initialDeposit){
    return null;
    }
    
    public Long getFixedDepositInterestRate(BigDecimal balance, int year){
        return null;
    }
    
    
    public void calculateAccruedInterest(DepositAccount ba) {
        
    }
    
    public void interestAccrualBatchProcess() {
        List<DepositAccount> accountList = getAccountList();
        for (DepositAccount ba : accountList)
            calculateAccruedInterest(ba);
    }
}
