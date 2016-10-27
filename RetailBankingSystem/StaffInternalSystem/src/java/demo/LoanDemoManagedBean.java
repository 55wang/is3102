/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import ejb.session.loan.LoanReminderSessionBeanLocal;
import entity.loan.LoanAccount;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author qiuxiaqing
 */
@Named(value = "loanDemoManagedBean")
@ViewScoped
public class LoanDemoManagedBean implements Serializable {

    @EJB
    private LoanReminderSessionBeanLocal loanReminderSessionBean;

    private Date currentDate1;
        private Date currentDate2;
    private LoanAccount demoLoanAccount;

    public LoanDemoManagedBean() {
    }

    public void sendPaymentReminder() {
        loanReminderSessionBean.remindLoanPaymentForAllActiveCustomers(currentDate1);
    }

    public void sendLatePaymentReminder() {
        loanReminderSessionBean.remindLatePaymentPenaltyForAllActiveCustomers(currentDate2);
    }

    public void sendBadLoanNotice() {
        loanReminderSessionBean.remindBadLoanForLoanOfficer();
    }

    public Date getCurrentDate1() {
        return currentDate1;
    }

    public void setCurrentDate1(Date currentDate1) {
        this.currentDate1 = currentDate1;
    }

    public Date getCurrentDate2() {
        return currentDate2;
    }

    public void setCurrentDate2(Date currentDate2) {
        this.currentDate2 = currentDate2;
    }
    
    public LoanAccount getDemoLoanAccount() {
        return demoLoanAccount;
    }


    public void setDemoLoanAccount(LoanAccount demoLoanAccount) {
        this.demoLoanAccount = demoLoanAccount;
    }

}
