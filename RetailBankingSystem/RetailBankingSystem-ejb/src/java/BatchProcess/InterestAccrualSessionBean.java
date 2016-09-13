/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BatchProcess;

import entity.BankAccount;
import entity.ConditionInterest;
import entity.Interest;
import entity.RangeInterest;
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
    
    public List<BankAccount> getAccountList() {
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
    
    
    public void calculateAccruedInterest(BankAccount ba) {
        switch (ba.getType()) {
            case "CURRENT":
                // cailculate interest
                break;
            case "SAVING":
                // calculate
                break;
              
        }
        List<Interest> rules = ba.getRules();
        for (Interest i : rules) {
            if (i instanceof ConditionInterest) {
                
            } else if (i instanceof RangeInterest) {
                
            } else {
                
            }
        }
    }
    
    public void interestAccrualBatchProcess() {
        List<BankAccount> accountList = getAccountList();
        for (BankAccount ba : accountList)
            calculateAccruedInterest(ba);
    }
    
    
}
