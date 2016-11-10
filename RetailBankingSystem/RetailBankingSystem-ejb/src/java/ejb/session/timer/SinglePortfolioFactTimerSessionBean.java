/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.timer;

import ejb.session.fact.PortfolioFactSessionBeanLocal;
import ejb.session.wealth.PortfolioSessionBeanLocal;
import entity.fact.customer.SinglePortfolioFactTable;
import entity.wealth.Portfolio;
import java.util.Calendar;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author wang
 */
@Stateless
public class SinglePortfolioFactTimerSessionBean implements SinglePortfolioFactTimerSessionBeanLocal {

    @EJB
    private PortfolioFactSessionBeanLocal portfolioFactSessionBean;
    @EJB
    PortfolioSessionBeanLocal portfolioSessionBean;
    private Portfolio p;

    @Override
    public void initSPFTimer() {
        portfolioSessionBean.updatePortfolio(p);

        Double totalBuyingValue = 0.0;
        Double totalCurrentValue = 0.0;

        for (int i = 0;
                i < p.getExecutedInvestmentPlan()
                .getSuggestedFinancialInstruments().size(); i++) {
            totalBuyingValue += p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().get(i).getBuyingValuePerShare() * p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().get(i).getBuyingNumberOfShare();
            totalCurrentValue += p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().get(i).getCurrentValuePerShare() * p.getExecutedInvestmentPlan().getSuggestedFinancialInstruments().get(i).getBuyingNumberOfShare();

        }

        Calendar today = Calendar.getInstance();

        SinglePortfolioFactTable spf;

        try {
            spf = portfolioFactSessionBean.getSinglePortfolioFactTable(today.getTime(), p.getWealthManagementSubscriber().getMainAccount().getCustomer().getId(), p.getId());
        } catch (Exception ex) {
//                System.out.println(ex);
            System.out.println("create new single portfolio fact table");
            spf = new SinglePortfolioFactTable();
            portfolioFactSessionBean.createSinglePortfolioFactTable(spf);
        }

        spf.setCustomer(p.getWealthManagementSubscriber().getMainAccount().getCustomer());
        spf.setPortfolio(p);

        spf.setTotalBuyingValue(totalBuyingValue);

        spf.setTotalCurrentValue(totalCurrentValue);

        spf.setCreationDate(today.getTime());
        portfolioFactSessionBean.updateSinglePortfolioFactTable(spf);
    }

}
