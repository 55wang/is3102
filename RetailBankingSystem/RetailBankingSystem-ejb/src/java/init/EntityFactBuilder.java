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
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, 2016);
        cal.set(Calendar.MONTH, 8);
        cal.set(Calendar.DATE, 30);
        Date predefined = cal.getTime();
        bft.setCreationDate(predefined);
        bft.setMonthOfDate(EnumUtils.Month.SEPTEMBER);
        bft.setYearOfDate(cal.get(Calendar.YEAR));
        bft.setTotalDepositAcct(100);
        bft.setTotalActiveDepositAcct(70);
        bft.setNewDepositAcct(20);
        bft.setTotalDepositAmount(new BigDecimal(10000));
        bft.setAvgDepositInterestRate(8.0);
        bft.setTotalDepositInterestAmount(800.0);

        bft.setTotalLoanAcct(50);
        bft.setTotalActiveLoanAcct(20);
        bft.setNewLoanAcct(5);
        bft.setTotalLoanAmount(100000.0);
        bft.setAvgLoanInterestRate(7.0);
        bft.setTotalLoanInterestAmount(7000.0);
        bft.setNumOfDefaultLoanAccount(3);

        bft.setTotalCardAcct(99);
        bft.setTotalActiveCardAcct(22);
        bft.setNewCardAcct(11);
        bft.setTotalCardAmount(1200000.0);
        bft.setTotalOutstandingAmount(3000000.0);
        bft.setNumOfBadCardAccount(33);

        bft.setTotalExecutedPortfolio(55);
        bft.setNewExecutedPortfolio(9);
        bft.setTotalPortfolioAmount(3000000000.0);
        bft.setTotalPortfolioProfitAmount(33333.3);
        bankFactTableSessionBean.createBankFactTable(bft);
        
        //2
        bft = new BankFactTable();
        cal = Calendar.getInstance();
        cal.clear();
        cal.set(Calendar.YEAR, 2016);
        cal.set(Calendar.MONTH, 9);
        cal.set(Calendar.DATE, 31);
        predefined = cal.getTime();
        bft.setCreationDate(predefined);
        bft.setMonthOfDate(EnumUtils.Month.OCTOBER);
        bft.setYearOfDate(cal.get(Calendar.YEAR));
        bft.setTotalDepositAcct(120);
        bft.setTotalActiveDepositAcct(74);
        bft.setNewDepositAcct(24);
        bft.setTotalDepositAmount(new BigDecimal(12000));
        bft.setAvgDepositInterestRate(8.0);
        bft.setTotalDepositInterestAmount(1100.0);
        
        bft.setTotalLoanAcct(55);
        bft.setTotalActiveLoanAcct(23);
        bft.setNewLoanAcct(8);
        bft.setTotalLoanAmount(110000.0);
        bft.setAvgLoanInterestRate(5.0);
        bft.setTotalLoanInterestAmount(5000.0);
        bft.setNumOfDefaultLoanAccount(10);

        bft.setTotalCardAcct(109);
        bft.setTotalActiveCardAcct(29);
        bft.setNewCardAcct(15);
        bft.setTotalCardAmount(1300000.0);
        bft.setTotalOutstandingAmount(4000000.0);
        bft.setNumOfBadCardAccount(43);

        bft.setTotalExecutedPortfolio(75);
        bft.setNewExecutedPortfolio(15);
        bft.setTotalPortfolioAmount(3200000000.0);
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
