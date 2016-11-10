/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.timer;

import ejb.session.bi.BizIntelligenceSessionBeanLocal;
import ejb.session.fact.BankFactTableSessionBeanLocal;
import entity.fact.bank.BankFactTable;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import server.utilities.DateUtils;

/**
 *
 * @author wang
 */
@Stateless
public class BankFactTimerSessionBean implements BankFactTimerSessionBeanLocal {

    @EJB
    private BankFactTableSessionBeanLocal bankFactTableSessionBean;
    @EJB
    private BizIntelligenceSessionBeanLocal bizIntelligenceSessionBean;
    
    public void insertBankFactTable() {

        Calendar cal = Calendar.getInstance();
        Date predefined = cal.getTime();
        BankFactTable bft;
        bft = bankFactTableSessionBean.getBankFactTableByCreationDate(predefined);

        bft.setCreationDate(predefined);
        bft.setMonthOfDate(DateUtils.getStringMonth(cal.get(Calendar.MONTH))); //might need change
        bft.setYearOfDate(cal.get(Calendar.YEAR));

        Date startDate = DateUtils.getBeginOfMonth();
        Date endDate = DateUtils.getEndOfMonth();
        Long bankTotalDepositAcct = bizIntelligenceSessionBean.getBankTotalDepositAcct(endDate);
        bft.setTotalDepositAcct(bankTotalDepositAcct);

        Long bankTotalActiveDepositAcct = bizIntelligenceSessionBean.getBankTotalActiveDepositAcct(startDate, endDate);
        bft.setTotalActiveDepositAcct(bankTotalActiveDepositAcct);

        Long bankTotalNewDepositAcct = bizIntelligenceSessionBean.getBankTotalNewDepositAcct(startDate, endDate);
        bft.setNewDepositAcct(bankTotalNewDepositAcct);

        Double bankTotalDepositAmount = bizIntelligenceSessionBean.getBankTotalDepositAmount(endDate);
        if (bankTotalDepositAmount == null) {
            bankTotalDepositAmount = 0.0;
        }
        bft.setTotalDepositAmount(bankTotalDepositAmount);

        Double bankDepositInterestAmount = bizIntelligenceSessionBean.getBankTotalDepositInterestAmount(endDate);
        if (bankDepositInterestAmount == null) {
            bankDepositInterestAmount = 0.0;
        }
        bft.setTotalDepositInterestAmount(bankDepositInterestAmount);

        Long bankTotalLoanAcct = bizIntelligenceSessionBean.getBankTotalLoanAcct(endDate);
        bft.setTotalLoanAcct(bankTotalLoanAcct);

        Long bankTotalNewLoanAcct = bizIntelligenceSessionBean.getBankTotalNewLoanAcct(startDate, endDate);
        bft.setNewLoanAcct(bankTotalNewLoanAcct);

        Double bankTotalLoanAmount = bizIntelligenceSessionBean.getBankTotalLoanAmount(endDate);
        if (bankTotalLoanAmount == null) {
            bankTotalLoanAmount = 0.0;
        }
        bft.setTotalLoanAmount(bankTotalLoanAmount);

        Double bankLoanInterestEarned = bizIntelligenceSessionBean.getBankLoanInterestEarned(endDate);
        if (bankLoanInterestEarned == null) {
            bankLoanInterestEarned = 0.0;
        }
        bft.setTotalLoanInterestEarned(bankLoanInterestEarned);

        Double bankLoanInterestUnearned = bizIntelligenceSessionBean.getBankLoanInterestUnearned(endDate);
        if (bankLoanInterestUnearned == null) {
            bankLoanInterestUnearned = 0.0;
        }
        bft.setTotalLoanInterestUnearned(bankLoanInterestUnearned);

        Long bankTotalDefaultLoanAcct = bizIntelligenceSessionBean.getBankTotalDefaultLoanAcct(endDate);
        bft.setNumOfDefaultLoanAccount(bankTotalDefaultLoanAcct);

        Long bankTotalCardAcct = bizIntelligenceSessionBean.getBankTotalCardAcct(endDate);
        bft.setTotalCardAcct(bankTotalCardAcct);

        Long bankTotalActiveCardAcct = bizIntelligenceSessionBean.getBankTotalActiveCardAcct(startDate, endDate);
        bft.setTotalActiveCardAcct(bankTotalActiveCardAcct);

        Long bankTotalNewCardAcct = bizIntelligenceSessionBean.getBankTotalNewCardAcct(startDate, endDate);
        bft.setNewCardAcct(bankTotalNewCardAcct);

        Long bankNumOfBadCardAccount = bizIntelligenceSessionBean.getBankNumOfBadCardAccount(startDate, endDate);
        bft.setNumOfBadCardAccount(bankNumOfBadCardAccount);

        Double bankTotalCardCurrentAmount = bizIntelligenceSessionBean.getBankTotalCardCurrentAmount(endDate);
        if (bankTotalCardCurrentAmount == null) {
            bankTotalCardCurrentAmount = 0.0;
        }
        bft.setTotalCardCurrentAmount(bankTotalCardCurrentAmount);

        Double bankTotalCardOutstandingAmount = bizIntelligenceSessionBean.getBankTotalCardOutstandingAmount(endDate);
        if (bankTotalCardOutstandingAmount == null) {
            bankTotalCardOutstandingAmount = 0.0;
        }
        bft.setTotalOutstandingAmount(bankTotalCardOutstandingAmount);

        bankNumOfBadCardAccount = bizIntelligenceSessionBean.getBankNumOfBadCardAccount(startDate, endDate);
        bft.setNumOfBadCardAccount(bankNumOfBadCardAccount);

        Long bankTotalWealthManagementSubscriber = bizIntelligenceSessionBean.getBankTotalWealthManagementSubsciber();
        bft.setTotalWealthManagementSubscriber(bankTotalWealthManagementSubscriber);

        Long bankTotalExecutedPortfolio = bizIntelligenceSessionBean.getBankTotalExecutedPortfolio(endDate);
        bft.setTotalExecutedPortfolio(bankTotalExecutedPortfolio);

        Long bankNewExecutedPortfolio = bizIntelligenceSessionBean.getBankNewExecutedPortfolio(startDate, endDate);
        bft.setNewExecutedPortfolio(bankNewExecutedPortfolio);

        Double bankTotalInvestmentAmount = bizIntelligenceSessionBean.getBankTotalInvestmentAmount(endDate);
        if (bankTotalInvestmentAmount == null) {
            bankTotalInvestmentAmount = 0.0;
        }
        bft.setTotalPortfolioAmount(bankTotalInvestmentAmount);

        Double bankTotalProfitAmount = bizIntelligenceSessionBean.getBankTotalProfitAmount(endDate);
        if (bankTotalProfitAmount == null) {
            bankTotalProfitAmount = 0.0;
        }
        bft.setTotalPortfolioProfitAmount(bankTotalProfitAmount);

        bankFactTableSessionBean.createBankFactTable(bft);
    }

}
