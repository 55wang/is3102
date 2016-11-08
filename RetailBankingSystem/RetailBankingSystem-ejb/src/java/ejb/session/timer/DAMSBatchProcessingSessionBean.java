/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.timer;

import BatchProcess.InterestAccrualSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.dams.account.DepositAccount;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author leiyang
 */
@Stateless
@LocalBean
public class DAMSBatchProcessingSessionBean {

    @EJB
    private InterestAccrualSessionBeanLocal interestBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    
    public void calculateDailyInterest() {
        List<DepositAccount> accounts = depositBean.showAllActiveAccounts();
        interestBean.calculateDailyInterestsForDepositAccount(accounts);
    }
    
    public void calculateMonthlyInterest() {
        List<DepositAccount> accounts = depositBean.showAllActiveAccounts();
        interestBean.calculateDailyInterestsForDepositAccount(accounts);
    }
}
