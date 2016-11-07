/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.bi;

import ejb.session.bi.BizIntelligenceSessionBeanLocal;
import ejb.session.fact.BankFactTableSessionBeanLocal;
import entity.fact.bank.BankFactTable;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.DateUtils;

/**
 *
 * @author wang
 */
@Named(value = "demoPageManagedBean")
@ViewScoped
public class DemoPageManagedBean implements Serializable {

    @EJB
    private BankFactTableSessionBeanLocal bankFactTableSessionBean;
    @EJB
    private BizIntelligenceSessionBeanLocal bizIntelligenceSessionBean;

    private Long bankTotalDepositAcct;
    private Long bankTotalActiveDepositAcct;
    private Long bankTotalNewDepositAcct;
    private Double bankTotalDepositAmount;
    private Double bankDepositInterestAmount;

    private Long bankTotalLoanAcct;
    private Long bankTotalNewLoanAcct;
    private Double bankTotalLoanAmount;
    private Double bankLoanInterestEarned;
    private Double bankLoanInterestUnearned;
    private Long bankTotalDefaultLoanAcct;

    private Long bankTotalCardAcct;
    private Long bankTotalActiveCardAcct;
    private Long bankTotalNewCardAcct;
    private Long bankNumOfBadCardAccount;
    private Double bankTotalCardCurrentAmount;
    private Double bankTotalCardOutstandingAmount;

    private Long bankTotalWealthManagementSubscriber;
    private Long bankTotalExecutedPortfolio;
    private Long bankNewExecutedPortfolio;
    private Double bankTotalInvestmentAmount;
    private Double bankTotalProfitAmount;

    public DemoPageManagedBean() {
    }

    public void insertBankFactTable() {
        BankFactTable bft = new BankFactTable();
        Calendar cal = Calendar.getInstance();
        Date predefined = cal.getTime();
        bft.setCreationDate(predefined);
        bft.setMonthOfDate(DateUtils.getStringMonth(cal.get(Calendar.MONTH))); //might need change
        bft.setYearOfDate(cal.get(Calendar.YEAR));

        Date startDate = DateUtils.getBeginOfMonth();
        Date endDate = DateUtils.getEndOfMonth();
        bankTotalDepositAcct = bizIntelligenceSessionBean.getBankTotalDepositAcct(endDate);
        bft.setTotalDepositAcct(bankTotalDepositAcct);

        bankTotalActiveDepositAcct = bizIntelligenceSessionBean.getBankTotalActiveDepositAcct(startDate, endDate);
        bft.setTotalActiveDepositAcct(bankTotalActiveDepositAcct);

        bankTotalNewDepositAcct = bizIntelligenceSessionBean.getBankTotalNewDepositAcct(startDate, endDate);
        bft.setNewDepositAcct(bankTotalNewDepositAcct);

        bankTotalDepositAmount = bizIntelligenceSessionBean.getBankTotalDepositAmount(endDate);
        if(bankTotalDepositAmount == null) bankTotalDepositAmount = 0.0;
        bft.setTotalDepositAmount(bankTotalDepositAmount);

        bankDepositInterestAmount = bizIntelligenceSessionBean.getBankTotalDepositInterestAmount(endDate);
        if(bankDepositInterestAmount == null) bankDepositInterestAmount = 0.0;
        bft.setTotalDepositInterestAmount(bankDepositInterestAmount);

        bankTotalLoanAcct = bizIntelligenceSessionBean.getBankTotalLoanAcct(endDate);
        bft.setTotalLoanAcct(bankTotalLoanAcct);

        bankTotalNewLoanAcct = bizIntelligenceSessionBean.getBankTotalNewLoanAcct(startDate, endDate);
        bft.setNewLoanAcct(bankTotalNewLoanAcct);

        bankTotalLoanAmount = bizIntelligenceSessionBean.getBankTotalLoanAmount(endDate);
        if(bankTotalLoanAmount == null) bankTotalLoanAmount = 0.0;
        bft.setTotalLoanAmount(bankTotalLoanAmount);

        bankLoanInterestEarned = bizIntelligenceSessionBean.getBankLoanInterestEarned(endDate);
        if(bankLoanInterestEarned == null) bankLoanInterestEarned = 0.0;
        bft.setTotalLoanInterestEarned(bankLoanInterestEarned);

        bankLoanInterestUnearned = bizIntelligenceSessionBean.getBankLoanInterestUnearned(endDate);
        if(bankLoanInterestUnearned == null) bankLoanInterestUnearned = 0.0;
        bft.setTotalLoanInterestUnearned(bankLoanInterestUnearned);

        bankTotalDefaultLoanAcct = bizIntelligenceSessionBean.getBankTotalDefaultLoanAcct(endDate);
        bft.setNumOfDefaultLoanAccount(bankTotalDefaultLoanAcct);

        bankTotalCardAcct = bizIntelligenceSessionBean.getBankTotalCardAcct(endDate);
        bft.setTotalCardAcct(bankTotalCardAcct);

        bankTotalActiveCardAcct = bizIntelligenceSessionBean.getBankTotalActiveCardAcct(startDate, endDate);
        bft.setTotalActiveCardAcct(bankTotalActiveCardAcct);

        bankTotalNewCardAcct = bizIntelligenceSessionBean.getBankTotalNewCardAcct(startDate, endDate);
        bft.setNewCardAcct(bankTotalNewCardAcct);
        
        bankNumOfBadCardAccount = bizIntelligenceSessionBean.getBankNumOfBadCardAccount(startDate, endDate);
        bft.setNumOfBadCardAccount(bankNumOfBadCardAccount);

        bankTotalCardCurrentAmount = bizIntelligenceSessionBean.getBankTotalCardCurrentAmount(endDate);
        if(bankTotalCardCurrentAmount == null) bankTotalCardCurrentAmount = 0.0;
        bft.setTotalCardCurrentAmount(bankTotalCardCurrentAmount);

        bankTotalCardOutstandingAmount = bizIntelligenceSessionBean.getBankTotalCardOutstandingAmount(endDate);
        if(bankTotalCardOutstandingAmount == null) bankTotalCardOutstandingAmount = 0.0;
        bft.setTotalOutstandingAmount(bankTotalCardOutstandingAmount);

        bankTotalWealthManagementSubscriber = bizIntelligenceSessionBean.getBankTotalWealthManagementSubsciber();
        bft.setTotalWealthManagementSubscriber(bankTotalWealthManagementSubscriber);
        
        bankTotalExecutedPortfolio = bizIntelligenceSessionBean.getBankTotalExecutedPortfolio(endDate);
        bft.setTotalExecutedPortfolio(bankTotalExecutedPortfolio);
        
        bankNewExecutedPortfolio = bizIntelligenceSessionBean.getBankNewExecutedPortfolio(startDate, endDate);
        bft.setNewExecutedPortfolio(bankNewExecutedPortfolio);
        
        bankTotalInvestmentAmount = bizIntelligenceSessionBean.getBankTotalInvestmentAmount(endDate);
        if(bankTotalInvestmentAmount == null) bankTotalInvestmentAmount = 0.0;
        bft.setTotalPortfolioAmount(bankTotalInvestmentAmount);
        
        bankTotalProfitAmount = bizIntelligenceSessionBean.getBankTotalProfitAmount(endDate);
        if(bankTotalProfitAmount == null) bankTotalProfitAmount = 0.0;
        bft.setTotalPortfolioProfitAmount(bankTotalProfitAmount);
        
        bankFactTableSessionBean.createBankFactTable(bft);
    }

}
