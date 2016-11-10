/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.timer;


import ejb.session.loan.LoanReminderSessionBeanLocal;
import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.report.ReportGenerationBeanLocal;
import entity.card.account.CreditCardAccount;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Schedule;
import javax.ejb.SessionContext;
import javax.ejb.Startup;
import javax.ejb.Timer;
import server.utilities.DateUtils;

/**
 *
 * @author leiyang
 */
@Singleton
@Startup
public class AutomaticTimerSessionBean {

    @Resource
    private SessionContext ctx;

    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;
    @EJB
    private ReportGenerationBeanLocal reportGenerationBean;
    @EJB
    private SinglePortfolioFactTimerSessionBeanLocal singlePortfolioFactTimerSessionBean;
    @EJB
    private DAMSBatchProcessingSessionBean damsBean;
    @EJB 
    private LoanReminderSessionBeanLocal loanReminderBean;
    @EJB
    private LoanBatchProcessingSessionBean loanBean;
    @EJB
    private BankFactTimerSessionBeanLocal bankFactTimerSessionBean;

    // REMARK: Rules: http://docs.oracle.com/javaee/6/tutorial/doc/bnboy.html
    @Schedule(year = "*", month = "*", dayOfMonth = "1", dayOfWeek = "*", hour = "0", minute = "10", second = "0")
    public void beginMonthTasks(Timer timer) {
        System.out.println("Perform Beginning of Month Tasks with 10min delay: " + new Date());
        // TODO: Perform tasks at begining of month
        damsBean.generateLastMonthEStatement();
    }

    @Schedule(year = "*", month = "*", dayOfMonth = "Last", dayOfWeek = "*", hour = "*", minute = "*", second = "*")
    public void endMonthTasks(Timer timer) {
        System.out.println("Perform End of Month Tasks: " + new Date());
        damsBean.calculateMonthlyInterest(); // TESTED
        //cut off day
        demoCutOffTimer();
        bankFactTimerSessionBean.insertBankFactTable();
        singlePortfolioFactTimerSessionBean.initSPFTimer();
    }

    @Schedule(year = "*", month = "*", dayOfMonth = "15", dayOfWeek = "*", hour = "0", minute = "0", second = "0")
    public void midMonthTasks(Timer timer) {
        System.out.println("Perform Mid Month Tasks: " + new Date());
        //due day 
        cardAcctSessionBean.calculateDueCreditCardAccount();
    }

    @Schedule(year = "*", month = "*", dayOfMonth = "Last", dayOfWeek = "*", hour = "0", minute = "0", second = "0")
    public void beginDayTasks(Timer timer) {
        System.out.println("Perform Beginning of Day Tasks: " + new Date());
        damsBean.calculateDailyInterest(); // TESTED
        loanReminderBean.remindLatePaymentPenaltyForAllActiveCustomers(new Date());
        loanReminderBean.remindLoanPaymentForAllActiveCustomers(new Date());
        loanReminderBean.remindBadLoanForLoanOfficer();
        loanBean.accumulateOverduePayment();
        loanBean.calculateOverduePayment();
        
        
    }

    @Schedule(year = "*", month = "*", dayOfMonth = "Last", dayOfWeek = "*", hour = "9", minute = "0", second = "0")
    public void beginDayNotificationTasks(Timer timer) {
        System.out.println("Perform Beginning of Day Tasks at 9 AM: " + new Date());
    }

//    @Schedule(year = "*", month = "*", dayOfMonth = "*", dayOfWeek = "*",  hour = "*", minute = "*/10", second = "*")
//    public void testingMethod(Timer timer) 
//    {
//        System.out.println("Perform Testing Tasks: " + new Date());
//        System.out.println("REMARK: MUST ensure the method is correct before write into the actual timer");
//        // For example: damsBean.calculateDailyInterest();
//    }
    public void demoCutOffTimer() {
        //shift current month amount to outstanding amount
        //retrieve again to prevent inaccuracy.
        List<CreditCardAccount> ccas = cardAcctSessionBean.getListCreditCardAccountsByActiveOrFreezeCardStatus();

        for (CreditCardAccount cca : ccas) {
            cardAcctSessionBean.addCurrentMonthAmountToOutstandingAmount(cca);
            cardAcctSessionBean.setOverDueDateAndMPD(cca);
            Date startDate = DateUtils.getBeginOfMonth();
            Date endDate = DateUtils.getMidOfMonth();
            reportGenerationBean.generateMonthlyCreditCardAccountTransactionReportCounter(cca.getCreditCardNum(), startDate, endDate);
        }
    }

}
