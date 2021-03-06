/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.fact.BankFactTableSessionBeanLocal;
import ejb.session.fact.PortfolioFactSessionBeanLocal;
import ejb.session.wealth.FinancialInstrumentSessionBeanLocal;
import ejb.session.wealth.PortfolioSessionBeanLocal;
import entity.customer.MainAccount;
import entity.fact.bank.BankFactTable;
import entity.fact.customer.SinglePortfolioFactTable;
import entity.wealth.FinancialInstrumentAndWeight;
import entity.wealth.Portfolio;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import server.utilities.DateUtils;
import server.utilities.EnumUtils;

/**
 *
 * @author wang
 */
@Stateless
@LocalBean
public class EntityFactBuilder {

    @EJB
    private PortfolioFactSessionBeanLocal factSessionBean;
    @EJB
    private PortfolioSessionBeanLocal portfolioSessionBean;
    @EJB
    private FinancialInstrumentSessionBeanLocal financialInstrumentSessionBean;
    @EJB
    private BankFactTableSessionBeanLocal bankFactTableSessionBean;

    private String currentDate;
    private String monthStartDate;
    private String monthEndDate;

    public void initBankFact() {
        //hardcode some historical data
        //1
        BankFactTable bft = new BankFactTable();
        Date predefined = DateUtils.getLastNthEndOfMonth(2);
        bft.setCreationDate(predefined);
        bft.setMonthOfDate(EnumUtils.Month.SEPTEMBER);
        bft.setYearOfDate(2016);
        bft.setTotalDepositAcct(100L);
        bft.setTotalActiveDepositAcct(70L);
        bft.setNewDepositAcct(20L);
        bft.setTotalDepositAmount(10000.0);
        bft.setTotalDepositInterestAmount(800.0);

        bft.setTotalLoanAcct(50L);
        bft.setNewLoanAcct(5L);
        bft.setTotalLoanAmount(100000.0);
        bft.setTotalLoanInterestEarned(7.0);
        bft.setTotalLoanInterestUnearned(7000.0);
        bft.setNumOfDefaultLoanAccount(3L);

        bft.setTotalCardAcct(99L);
        bft.setTotalActiveCardAcct(22L);
        bft.setNewCardAcct(11L);
        bft.setTotalCardCurrentAmount(120000.0);
        bft.setTotalOutstandingAmount(300000.0);
        bft.setNumOfBadCardAccount(33L);

        bft.setTotalExecutedPortfolio(55L);
        bft.setNewExecutedPortfolio(9L);
        bft.setTotalPortfolioAmount(300000.0);
        bft.setTotalPortfolioProfitAmount(33333.3);
        bankFactTableSessionBean.createBankFactTable(bft);
        
        //2
        bft = new BankFactTable();
        predefined = DateUtils.getLastNthEndOfMonth(1);
        bft.setCreationDate(predefined);
        bft.setMonthOfDate(EnumUtils.Month.OCTOBER);
        bft.setYearOfDate(2016);
        bft.setTotalDepositAcct(120L);
        bft.setTotalActiveDepositAcct(74L);
        bft.setNewDepositAcct(24L);
        bft.setTotalDepositAmount(12000.0);
        bft.setTotalDepositInterestAmount(1100.0);
        
        bft.setTotalLoanAcct(55L);
        bft.setNewLoanAcct(8L);
        bft.setTotalLoanAmount(110000.0);
        bft.setTotalLoanInterestEarned(7.0);
        bft.setTotalLoanInterestUnearned(7000.0);
        bft.setNumOfDefaultLoanAccount(10L);

        bft.setTotalCardAcct(109L);
        bft.setTotalActiveCardAcct(29L);
        bft.setNewCardAcct(15L);
        bft.setTotalCardCurrentAmount(130000.0);
        bft.setTotalOutstandingAmount(400000.0);
        bft.setNumOfBadCardAccount(43L);

        bft.setTotalExecutedPortfolio(75L);
        bft.setNewExecutedPortfolio(15L);
        bft.setTotalPortfolioAmount(320000.0);
        bft.setTotalPortfolioProfitAmount(43333.3);
        bankFactTableSessionBean.createBankFactTable(bft);
        
    }

    public void initSinglePortfolioFact(MainAccount demoMainAccount, Portfolio demoPortfolio) {

        // select distinct creationdate 
        List<Date> dates = factSessionBean.getDistinctDateFromFIFactTable();
        System.out.println("inside initSinglePortfolioFact");

        for (Date date : dates) {

            List<FinancialInstrumentAndWeight> fiws = demoPortfolio.getExecutedInvestmentPlan().getSuggestedFinancialInstruments();

            Double totalValue = 0.0; //write into spf
            for (FinancialInstrumentAndWeight fiw : fiws) {

                EnumUtils.FinancialInstrumentClass fic = fiw.getFi().getName();
                Double valueChangedPercentage = factSessionBean.getFinancialInstrumentValueChangeByCreationDateAndName(date, fic);
                Double valueChanged = fiw.getTempValue() * valueChangedPercentage;
                Double valueResult = fiw.getTempValue() + valueChanged;
                fiw.setTempValue(valueResult);
                financialInstrumentSessionBean.updateFinancialInstrumentAndWeight(fiw);
                totalValue += valueResult;
            }

            SinglePortfolioFactTable spf = new SinglePortfolioFactTable();
            factSessionBean.createSinglePortfolioFactTable(spf);
            spf.setCreationDate(date);
            spf.setCustomer(demoMainAccount.getCustomer());
            spf.setPortfolio(demoPortfolio);

            spf.setTotalCurrentValue(totalValue);
            spf.setTotalBuyingValue(demoPortfolio.getTotalBuyingValue());
            factSessionBean.updateSinglePortfolioFactTable(spf);

            portfolioSessionBean.updatePortfolio(demoPortfolio);
        }

        SinglePortfolioFactTable spf = new SinglePortfolioFactTable();
        factSessionBean.createSinglePortfolioFactTable(spf);
        spf.setCreationDate(new Date());
        spf.setCustomer(demoMainAccount.getCustomer());
        spf.setPortfolio(demoPortfolio);

        spf.setTotalCurrentValue(demoPortfolio.getTotalCurrentValue());
        spf.setTotalBuyingValue(demoPortfolio.getTotalBuyingValue());
        factSessionBean.updateSinglePortfolioFactTable(spf);

        portfolioSessionBean.updatePortfolio(demoPortfolio);

        System.out.println("end initSinglePortfolioFact");
    }

    public void initDate() {
        Date current = new Date();
        setCurrentDate(new SimpleDateFormat("yyyy-MM-dd").format(current));

        Calendar cStart = Calendar.getInstance();   // this takes current date
        cStart.set(Calendar.DAY_OF_MONTH, 1);
        cStart.set(Calendar.MONTH, 8);
        setMonthStartDate(new SimpleDateFormat("yyyy-MM-dd").format(cStart.getTime()));

        Calendar cEnd = Calendar.getInstance();   // this takes current date
        cEnd.set(Calendar.DAY_OF_MONTH, cEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMonthEndDate(new SimpleDateFormat("yyyy-MM-dd").format(cEnd.getTime()));
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getMonthStartDate() {
        return monthStartDate;
    }

    public void setMonthStartDate(String monthStartDate) {
        this.monthStartDate = monthStartDate;
    }

    public String getMonthEndDate() {
        return monthEndDate;
    }

    public void setMonthEndDate(String monthEndDate) {
        this.monthEndDate = monthEndDate;
    }
}
