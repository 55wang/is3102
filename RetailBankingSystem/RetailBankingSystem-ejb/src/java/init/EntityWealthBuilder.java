/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.wealth.DesignInvestmentPlanSessionBeanLocal;
import ejb.session.wealth.FinancialInstrumentSessionBeanLocal;
import ejb.session.wealth.InvestmentPlanSessionBeanLocal;
import ejb.session.wealth.PortfolioSessionBean;
import ejb.session.wealth.PortfolioSessionBeanLocal;
import ejb.session.wealth.WealthManegementSubscriberSessionBeanLocal;
import entity.customer.MainAccount;
import entity.customer.WealthManagementSubscriber;
import entity.wealth.FinancialInstrument;
import entity.wealth.FinancialInstrumentAndWeight;
import entity.wealth.InvestmentPlan;
import entity.wealth.Portfolio;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import server.utilities.CommonUtils;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils;

/**
 *
 * @author VIN-S
 */
@Stateless
@LocalBean
public class EntityWealthBuilder {

    @EJB
    private DesignInvestmentPlanSessionBeanLocal designInvestmentPlanSessionBean;

    @EJB
    private LoginSessionBeanLocal loginBean;

    @EJB
    private InvestmentPlanSessionBeanLocal investmentPlanSessionBean;

    @EJB
    private FinancialInstrumentSessionBeanLocal financialInstrumentSessionBean;

    @EJB
    private WealthManegementSubscriberSessionBeanLocal wealthManegementSubscriberSessionBean;

    @EJB
    private PortfolioSessionBeanLocal portfolioSessionBean;

    @EJB
    private StaffAccountSessionBeanLocal staffAccountSessionBean;

    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;

    public Portfolio initWealth(List<FinancialInstrument> allFinancialInstruments) {
        //generate PortfolioModel table
        constructPortfolioModel();

        MainAccount demoMainAccount = loginBean.getMainAccountByUserID(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID);
        demoMainAccount.getCustomer().setSavingPerMonth(500.0);
        WealthManagementSubscriber wms = new WealthManagementSubscriber();
        wms.setMainAccount(demoMainAccount);
        wms.setRelationshipManager(staffAccountSessionBean.getAccountByUsername(ConstantUtils.RELATIONSHIP_MANAGER_USERNAME));
        wms.setRiskToleranceLevel(EnumUtils.RiskToleranceLevel.ABOVE_AVERAGE_RISK_TOLERANCE);
        wms.setRiskToleranceScore(31);

        wealthManegementSubscriberSessionBean.createWealthManagementSubscriber(wms);
        demoMainAccount.setWealthManagementSubscriber(wms);
        mainAccountSessionBean.updateMainAccount(demoMainAccount);

        InvestmentPlan investmentPlan = new InvestmentPlan();
        investmentPlan.setAmountOfInvestment(10000);
        investmentPlan.setCustomerExpectedReturn(0.13);
        List<FinancialInstrument> preferedInstruments = new ArrayList<FinancialInstrument>();
//        List<FinancialInstrument> allFinancialInstruments = financialInstrumentSessionBean.getAllFinancialInstruments();
        preferedInstruments.add(allFinancialInstruments.get(0));
        preferedInstruments.add(allFinancialInstruments.get(2));
        preferedInstruments.add(allFinancialInstruments.get(5));
        preferedInstruments.add(allFinancialInstruments.get(7));
        investmentPlan.setPreferedFinancialInstrument(preferedInstruments);
        investmentPlan.setRemarks("test plan");
        investmentPlan.setStatus(EnumUtils.InvestmentPlanStatus.PENDING);
        investmentPlan.setWealthManagementSubscriber(wms);
        investmentPlanSessionBean.createInvestmentPlan(investmentPlan);

        InvestmentPlan executedInvestmentPlan = new InvestmentPlan();
        executedInvestmentPlan.setAmountOfInvestment(100000);
        executedInvestmentPlan.setCustomerExpectedReturn(0.13);
        List<FinancialInstrument> preferedInstruments2 = new ArrayList<FinancialInstrument>();
        preferedInstruments2.add(allFinancialInstruments.get(0));
        preferedInstruments2.add(allFinancialInstruments.get(2));
        preferedInstruments2.add(allFinancialInstruments.get(5));
        preferedInstruments2.add(allFinancialInstruments.get(7));
        executedInvestmentPlan.setPreferedFinancialInstrument(preferedInstruments2);
        executedInvestmentPlan.setRemarks("test executed plan");
        executedInvestmentPlan.setStatus(EnumUtils.InvestmentPlanStatus.EXECUTED);
        executedInvestmentPlan.setWealthManagementSubscriber(wms);
        investmentPlanSessionBean.createInvestmentPlan(executedInvestmentPlan);

        List<FinancialInstrumentAndWeight> suggestedFinancialInstruments = new ArrayList<FinancialInstrumentAndWeight>();

        for (int i = 0; i < allFinancialInstruments.size(); i++) {
            if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.CORPORATE_BONDS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.0);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.DIVIDEND_GROWTH_STOCKS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(1714);
                fiaw.setBuyingValuePerShare(7.0);
                fiaw.setCurrentValuePerShare(7.0);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.12);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.EMERGING_MARKET_BONDS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.0);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.EMERGING_MARKET_STOCKS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(1222);
                fiaw.setBuyingValuePerShare(9.0);
                fiaw.setCurrentValuePerShare(9.0);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.11);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.FOREIGN_DEVELOPED_STOCKS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(3000);
                fiaw.setBuyingValuePerShare(5.0);
                fiaw.setCurrentValuePerShare(5.0);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.15);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.MUNICIPAL_BONDS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(166);
                fiaw.setBuyingValuePerShare(6.0);
                fiaw.setCurrentValuePerShare(6.0);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.01);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.NATURAL_RESOURCES)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(1444);
                fiaw.setBuyingValuePerShare(9.0);
                fiaw.setCurrentValuePerShare(9.0);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.13);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.REAL_ESTATE)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(1125);
                fiaw.setBuyingValuePerShare(8.0);
                fiaw.setCurrentValuePerShare(8.0);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.09);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.TREASURY_INFLATION_PROTECTED_SECURITIES)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.0);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.US_GOVERNMENT_BONDS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(1200);
                fiaw.setBuyingValuePerShare(5.0);
                fiaw.setCurrentValuePerShare(5.0);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.06);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.US_STOCKS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(3300);
                fiaw.setBuyingValuePerShare(10.0);
                fiaw.setCurrentValuePerShare(10.0);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.33);
                suggestedFinancialInstruments.add(fiaw);
            }
        }

        executedInvestmentPlan.setSuggestedFinancialInstruments(suggestedFinancialInstruments);
        executedInvestmentPlan.setSystemPredictReturn(0.053);
        executedInvestmentPlan.setSystemPredictRisk(31);
        executedInvestmentPlan.setRiskLevel(EnumUtils.InvestmentRiskLevel.ABOVE_AVERAGE_RISK);

        Portfolio p = new Portfolio();
        p.setStatus(EnumUtils.PortfolioStatus.BOUGHT);
        portfolioSessionBean.createPortfolio(p);
        p.setExecutedInvestmentPlan(executedInvestmentPlan);
        p.setWealthManagementSubscriber(wms);
        portfolioSessionBean.updatePortfolio(p);
        executedInvestmentPlan.setPortfolio(p);
        investmentPlanSessionBean.updateInvestmentPlan(executedInvestmentPlan);
        List<Portfolio> ps = wms.getPortfolios();
        ps.add(p);
        wms.setPortfolios(ps);
        wealthManegementSubscriberSessionBean.updateWealthManagementSubscriber(wms);

        return p;
    }

    public Portfolio initPortfolioFactTable2(MainAccount ma, List<FinancialInstrument> allFinancialInstruments) {

        //2nd portfolio
        InvestmentPlan executedInvestmentPlan = new InvestmentPlan();
        executedInvestmentPlan.setAmountOfInvestment(100000);
        executedInvestmentPlan.setCustomerExpectedReturn(0.13);
        List<FinancialInstrument> preferedInstruments = new ArrayList<FinancialInstrument>();
//        List<FinancialInstrument> allFinancialInstruments = financialInstrumentSessionBean.getAllFinancialInstruments();
        preferedInstruments.add(allFinancialInstruments.get(0));
        preferedInstruments.add(allFinancialInstruments.get(2));
        preferedInstruments.add(allFinancialInstruments.get(5));
        preferedInstruments.add(allFinancialInstruments.get(7));
        executedInvestmentPlan.setPreferedFinancialInstrument(preferedInstruments);
        executedInvestmentPlan.setRemarks("test executed plan 3");
        executedInvestmentPlan.setStatus(EnumUtils.InvestmentPlanStatus.EXECUTED);
        executedInvestmentPlan.setWealthManagementSubscriber(ma.getWealthManagementSubscriber());
        investmentPlanSessionBean.createInvestmentPlan(executedInvestmentPlan);

        List<FinancialInstrumentAndWeight> suggestedFinancialInstruments = new ArrayList<FinancialInstrumentAndWeight>();

        for (int i = 0; i < allFinancialInstruments.size(); i++) {
            if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.CORPORATE_BONDS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.0);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.DIVIDEND_GROWTH_STOCKS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(109);
                fiaw.setBuyingValuePerShare(11.0);
                fiaw.setCurrentValuePerShare(11.0);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.12);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.EMERGING_MARKET_BONDS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.0);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.EMERGING_MARKET_STOCKS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(91);
                fiaw.setBuyingValuePerShare(12.0);
                fiaw.setCurrentValuePerShare(12.0);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.11);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.FOREIGN_DEVELOPED_STOCKS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(300);
                fiaw.setBuyingValuePerShare(5.0);
                fiaw.setCurrentValuePerShare(5.0);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.15);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.MUNICIPAL_BONDS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(10);
                fiaw.setBuyingValuePerShare(10.0);
                fiaw.setCurrentValuePerShare(10.0);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.01);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.NATURAL_RESOURCES)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(650);
                fiaw.setBuyingValuePerShare(2.0);
                fiaw.setCurrentValuePerShare(2.0);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.13);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.REAL_ESTATE)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(128);
                fiaw.setBuyingValuePerShare(7.0);
                fiaw.setCurrentValuePerShare(7.0);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.09);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.TREASURY_INFLATION_PROTECTED_SECURITIES)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.0);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.US_GOVERNMENT_BONDS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(666);
                fiaw.setBuyingValuePerShare(9.0);
                fiaw.setCurrentValuePerShare(9.0);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.06);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.US_STOCKS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(825);
                fiaw.setBuyingValuePerShare(4.0);
                fiaw.setCurrentValuePerShare(4.0);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.33);
                suggestedFinancialInstruments.add(fiaw);
            }
        }

        executedInvestmentPlan.setSuggestedFinancialInstruments(suggestedFinancialInstruments);
        executedInvestmentPlan.setSystemPredictReturn(0.053);
        executedInvestmentPlan.setSystemPredictRisk(31);
        executedInvestmentPlan.setRiskLevel(EnumUtils.InvestmentRiskLevel.ABOVE_AVERAGE_RISK);

        Portfolio p3 = new Portfolio();
        p3.setStatus(EnumUtils.PortfolioStatus.BOUGHT);
        portfolioSessionBean.createPortfolio(p3);
        p3.setExecutedInvestmentPlan(executedInvestmentPlan);
        p3.setWealthManagementSubscriber(ma.getWealthManagementSubscriber());
        portfolioSessionBean.updatePortfolio(p3);

        executedInvestmentPlan.setPortfolio(p3);

        investmentPlanSessionBean.updateInvestmentPlan(executedInvestmentPlan);
        List<Portfolio> ps = ma.getWealthManagementSubscriber().getPortfolios();
        ps.add(p3);
        ma.getWealthManagementSubscriber().setPortfolios(ps);
        wealthManegementSubscriberSessionBean.updateWealthManagementSubscriber(ma.getWealthManagementSubscriber());

        return p3;

    }

    public static void constructPortfolioModel() {
        RConnection connection = null;

        try {
            /* Create a connection to Rserve instance running
             * on default port 6311
             */
            connection = new RConnection(ConstantUtils.ipAddress, 6311);

            String prependingPath = CommonUtils.getPrependFolderName();

            connection.eval("source('" + prependingPath + "ConstructPortfolioModel.R')");

            Integer result = connection.eval("constructPortfolioModel()").asInteger();
            if (result == 0) {
                System.out.println("contruct portfolio success");
            } else {
                System.out.println("contruct portfolio fail");
            }

        } catch (RserveException e) {
            e.printStackTrace();
        } catch (REXPMismatchException e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }

    public List<FinancialInstrument> allFinancialInstrument() {

        FinancialInstrument financialInstrument1 = new FinancialInstrument();
        financialInstrument1.setName(EnumUtils.FinancialInstrumentClass.CORPORATE_BONDS);
        financialInstrument1.setDescription("Income, low historical volatility, diversification");
        financialInstrument1.setStandardDeviation(0.05);
        financialInstrument1.setExpectedReturn(-0.002);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument1);

        FinancialInstrument financialInstrument2 = new FinancialInstrument();
        financialInstrument2.setName(EnumUtils.FinancialInstrumentClass.DIVIDEND_GROWTH_STOCKS);
        financialInstrument2.setDescription("Capital growth, income, long-run inflation protection, tax efficiency");
        financialInstrument2.setStandardDeviation(0.14);
        financialInstrument2.setExpectedReturn(0.037);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument2);

        FinancialInstrument financialInstrument3 = new FinancialInstrument();
        financialInstrument3.setName(EnumUtils.FinancialInstrumentClass.EMERGING_MARKET_BONDS);
        financialInstrument3.setDescription("Income, diversification");
        financialInstrument3.setStandardDeviation(0.07);
        financialInstrument3.setExpectedReturn(0.01);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument3);

        FinancialInstrument financialInstrument4 = new FinancialInstrument();
        financialInstrument4.setName(EnumUtils.FinancialInstrumentClass.EMERGING_MARKET_STOCKS);
        financialInstrument4.setDescription("Capital growth, long-run inflation protection, tax efficiency");
        financialInstrument4.setStandardDeviation(0.24);
        financialInstrument4.setExpectedReturn(0.081);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument4);

        FinancialInstrument financialInstrument5 = new FinancialInstrument();
        financialInstrument5.setName(EnumUtils.FinancialInstrumentClass.FOREIGN_DEVELOPED_STOCKS);
        financialInstrument5.setDescription("Capital growth, long-run inflation protection, tax efficiency");
        financialInstrument5.setStandardDeviation(0.18);
        financialInstrument5.setExpectedReturn(0.062);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument5);

        FinancialInstrument financialInstrument6 = new FinancialInstrument();
        financialInstrument6.setName(EnumUtils.FinancialInstrumentClass.MUNICIPAL_BONDS);
        financialInstrument6.setDescription("Income, low historical volatility, diversification, tax efficiency");
        financialInstrument6.setStandardDeviation(0.05);
        financialInstrument6.setExpectedReturn(-0.08);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument6);

        FinancialInstrument financialInstrument7 = new FinancialInstrument();
        financialInstrument7.setName(EnumUtils.FinancialInstrumentClass.NATURAL_RESOURCES);
        financialInstrument7.setDescription("Diversification, inflation protection, tax efficiency");
        financialInstrument7.setStandardDeviation(0.22);
        financialInstrument7.setExpectedReturn(0.062);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument7);

        FinancialInstrument financialInstrument8 = new FinancialInstrument();
        financialInstrument8.setName(EnumUtils.FinancialInstrumentClass.REAL_ESTATE);
        financialInstrument8.setDescription("Income, diversification, inflation protection");
        financialInstrument8.setStandardDeviation(0.18);
        financialInstrument8.setExpectedReturn(0.05);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument8);

        FinancialInstrument financialInstrument9 = new FinancialInstrument();
        financialInstrument9.setName(EnumUtils.FinancialInstrumentClass.TREASURY_INFLATION_PROTECTED_SECURITIES);
        financialInstrument9.setDescription("Income, low historical volatility, diversification, inflation protection");
        financialInstrument9.setStandardDeviation(0.05);
        financialInstrument9.setExpectedReturn(-0.005);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument9);

        FinancialInstrument financialInstrument10 = new FinancialInstrument();
        financialInstrument10.setName(EnumUtils.FinancialInstrumentClass.US_GOVERNMENT_BONDS);
        financialInstrument10.setDescription("Income, low historical volatility, diversification");
        financialInstrument10.setStandardDeviation(0.05);
        financialInstrument10.setExpectedReturn(-0.008);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument10);

        FinancialInstrument financialInstrument11 = new FinancialInstrument();
        financialInstrument11.setName(EnumUtils.FinancialInstrumentClass.US_STOCKS);
        financialInstrument11.setDescription("Capital growth, long-run inflation protection, tax efficiency");
        financialInstrument11.setStandardDeviation(0.16);
        financialInstrument11.setExpectedReturn(0.053);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument11);

        List<FinancialInstrument> allFinancialInstruments = financialInstrumentSessionBean.getAllFinancialInstruments();
        return allFinancialInstruments;
    }

}
