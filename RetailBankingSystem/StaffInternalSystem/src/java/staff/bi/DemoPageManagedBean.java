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
import server.utilities.EnumUtils;

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

    public DemoPageManagedBean() {
    }

    public void insertBankFactTable() {
        BankFactTable bft = new BankFactTable();
        Calendar cal = Calendar.getInstance();
        Date predefined = cal.getTime();
        bft.setCreationDate(predefined);
        bft.setMonthOfDate(EnumUtils.Month.NOVEMBER); //might need change
        bft.setYearOfDate(cal.get(Calendar.YEAR));

        //hardcode, need to continue to retrieve live data
        Date startDate = DateUtils.getBeginOfMonth();
        Date endDate = DateUtils.getEndOfMonth();
        bankTotalDepositAcct = bizIntelligenceSessionBean.getBankTotalDepositAcct(endDate);
        bft.setTotalDepositAcct(bankTotalDepositAcct);
        bft.setTotalActiveDepositAcct(70L);
        bft.setNewDepositAcct(20L);
        bft.setTotalDepositAmount(10888.0);
        bft.setTotalDepositInterestAmount(800.0);

        bft.setTotalLoanAcct(50L);
        bft.setTotalActiveLoanAcct(20L);
        bft.setNewLoanAcct(5L);
        bft.setTotalLoanAmount(100000.0);
        bft.setTotalLoanInterestEarned(7.0);
        bft.setTotalLoanInterestUnearned(7000.0);
        bft.setNumOfDefaultLoanAccount(3L);

        bft.setTotalCardAcct(99L);
        bft.setTotalActiveCardAcct(22L);
        bft.setNewCardAcct(11L);
        bft.setTotalCardAmount(1200000.0);
        bft.setTotalOutstandingAmount(3000000.0);
        bft.setNumOfBadCardAccount(33L);

        bft.setTotalExecutedPortfolio(55L);
        bft.setNewExecutedPortfolio(9L);
        bft.setTotalPortfolioAmount(3000000000.0);
        bft.setTotalPortfolioProfitAmount(33333.3);
        bankFactTableSessionBean.createBankFactTable(bft);
    }

}
