/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.fact.PortfolioFactSessionBeanLocal;
import ejb.session.wealth.FinancialInstrumentSessionBeanLocal;
import ejb.session.wealth.PortfolioSessionBeanLocal;
import entity.customer.MainAccount;
import entity.fact.customer.SinglePortfolioFactTable;
import entity.wealth.FinancialInstrument;
import entity.wealth.FinancialInstrumentAndWeight;
import entity.wealth.Portfolio;
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

    private String currentDate;
    private String monthStartDate;
    private String monthEndDate;

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
