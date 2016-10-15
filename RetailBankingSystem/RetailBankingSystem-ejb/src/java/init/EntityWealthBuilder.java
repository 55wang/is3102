/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.wealth.FinancialInstrumentSessionBeanLocal;
import ejb.session.wealth.InvestmentPlanSessionBeanLocal;
import ejb.session.wealth.PortfolioSessionBean;
import ejb.session.wealth.PortfolioSessionBeanLocal;
import ejb.session.wealth.WealthManegementSubscriberSessionBeanLocal;
import entity.customer.MainAccount;
import entity.customer.WealthManagementSubscriber;
import entity.wealth.FinancialInstrument;
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

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public WealthManagementSubscriber initWealth() {
        MainAccount demoMainAccount = loginBean.getMainAccountByUserID(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID);
        WealthManagementSubscriber wms = new WealthManagementSubscriber();
        wms.setMainAccount(demoMainAccount);
        wms.setRelationshipManager(staffAccountSessionBean.getAccountByUsername(ConstantUtils.RELATIONSHIP_MANAGER_USERNAME));
        wms.setRiskToleranceLevel(EnumUtils.RiskToleranceLevel.ABOVE_AVERAGE_RISK_TOLERANCE);
        wms.setRiskToleranceScore(31);

        wealthManegementSubscriberSessionBean.createWealthManagementSubscriber(wms);
        demoMainAccount.setWealthManagementSubscriber(wms);
        mainAccountSessionBean.updateMainAccount(demoMainAccount);

        FinancialInstrument financialInstrument1 = new FinancialInstrument();
        financialInstrument1.setName(EnumUtils.FinancialInstrumentClass.CORPORATE_BONDS);
        financialInstrument1.setDescription("Corporate Bonds are debt issued by U.S. corporations with investment-grade credit ratings to fund business activities");
        financialInstrument1.setStandardDeviation(0.05);
        financialInstrument1.setExpectedReturn(0.01);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument1);

        FinancialInstrument financialInstrument2 = new FinancialInstrument();
        financialInstrument2.setName(EnumUtils.FinancialInstrumentClass.DIVIDEND_GROWTH_STOCKS);
        financialInstrument2.setDescription("Dividend Growth Stocks represent an ownership share in U.S. companies that have increased their dividend payout each year for the last ten or more consecutive years.");
        financialInstrument2.setStandardDeviation(0.14);
        financialInstrument2.setExpectedReturn(0.053);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument2);

        FinancialInstrument financialInstrument3 = new FinancialInstrument();
        financialInstrument3.setName(EnumUtils.FinancialInstrumentClass.EMERGING_MARKET_BONDS);
        financialInstrument3.setDescription("Emerging Market Bonds are debt issued by governments and quasi-government organizations from emerging market countries. ");
        financialInstrument3.setStandardDeviation(0.07);
        financialInstrument3.setExpectedReturn(0.02);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument3);

        FinancialInstrument financialInstrument4 = new FinancialInstrument();
        financialInstrument4.setName(EnumUtils.FinancialInstrumentClass.EMERGING_MARKET_STOCKS);
        financialInstrument4.setDescription("Emerging Market Stocks represent an ownership share in foreign companies in developing economies such as Brazil, China, India, South Africa and Taiwan.");
        financialInstrument4.setStandardDeviation(0.24);
        financialInstrument4.setExpectedReturn(0.054);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument4);

        FinancialInstrument financialInstrument5 = new FinancialInstrument();
        financialInstrument5.setName(EnumUtils.FinancialInstrumentClass.FOREIGN_DEVELOPED_STOCKS);
        financialInstrument5.setDescription("Foreign Developed Market Stocks represent an ownership share in companies headquartered in developed economies like Europe, Australia and Japan.");
        financialInstrument5.setStandardDeviation(0.18);
        financialInstrument5.setExpectedReturn(0.033);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument5);

        FinancialInstrument financialInstrument6 = new FinancialInstrument();
        financialInstrument6.setName(EnumUtils.FinancialInstrumentClass.MUNICIPAL_BONDS);
        financialInstrument6.setDescription("Municipal Bonds are debt issued by U.S. state and local governments. Unlike most other bonds, Municipal Bonds’ interest is exempt from federal income taxes. ");
        financialInstrument6.setStandardDeviation(0.05);
        financialInstrument6.setExpectedReturn(0.01);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument6);

        FinancialInstrument financialInstrument7 = new FinancialInstrument();
        financialInstrument7.setName(EnumUtils.FinancialInstrumentClass.NATURAL_RESOURCES);
        financialInstrument7.setDescription("Natural Resources reflect the prices of energy (e.g., natural gas and crude oil). Natural Resources provide inflation protection and diversification. ");
        financialInstrument7.setStandardDeviation(0.22);
        financialInstrument7.setExpectedReturn(0.03);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument7);

        FinancialInstrument financialInstrument8 = new FinancialInstrument();
        financialInstrument8.setName(EnumUtils.FinancialInstrumentClass.REAL_ESTATE);
        financialInstrument8.setDescription("Real Estate is accessed through publicly traded U.S. real estate investment trusts (REITs) that own commercial properties, apartment complexes and retail space.");
        financialInstrument8.setStandardDeviation(0.18);
        financialInstrument8.setExpectedReturn(0.035);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument8);

        FinancialInstrument financialInstrument9 = new FinancialInstrument();
        financialInstrument9.setName(EnumUtils.FinancialInstrumentClass.TREASURY_INFLATION_PROTECTED_SECURITIES);
        financialInstrument9.setDescription("Treasury Inflation-Protected Securities (TIPS) are inflation-indexed bonds issued by the U.S. federal government.");
        financialInstrument9.setStandardDeviation(0.05);
        financialInstrument9.setExpectedReturn(-0.023);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument9);

        FinancialInstrument financialInstrument10 = new FinancialInstrument();
        financialInstrument10.setName(EnumUtils.FinancialInstrumentClass.US_GOVERNMENT_BONDS);
        financialInstrument10.setDescription("U.S. Government Bonds are debt issued by the U.S. federal government and agencies to fund various spending programs.");
        financialInstrument10.setStandardDeviation(0.05);
        financialInstrument10.setExpectedReturn(-0.015);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument10);

        FinancialInstrument financialInstrument11 = new FinancialInstrument();
        financialInstrument11.setName(EnumUtils.FinancialInstrumentClass.US_STOCKS);
        financialInstrument11.setDescription("U.S. Stocks represent an ownership share in U.S.-based corporations. The U.S. has the largest economy and stock market in the world. ");
        financialInstrument11.setStandardDeviation(0.16);
        financialInstrument11.setExpectedReturn(0.043);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument11);

        InvestmentPlan investmentPlan = new InvestmentPlan();
        investmentPlan.setAmountOfInvestment(10000);
        investmentPlan.setCustomerExpectedReturn(0.13);
        List<FinancialInstrument> preferedInstruments = new ArrayList<FinancialInstrument>();
        List<FinancialInstrument> allFinancialInstruments = financialInstrumentSessionBean.getAllFinancialInstruments();
        preferedInstruments.add(allFinancialInstruments.get(0));
        preferedInstruments.add(allFinancialInstruments.get(2));
        preferedInstruments.add(allFinancialInstruments.get(5));
        preferedInstruments.add(allFinancialInstruments.get(7));
        investmentPlan.setPreferedFinancialInstrument(preferedInstruments);
        investmentPlan.setRemarks("test plan");
        investmentPlan.setStatus(EnumUtils.InvestmentPlanStatus.EXECUTED);
        investmentPlan.setWealthManagementSubscriber(wms);
        investmentPlan.setSatisfactionLevel(EnumUtils.InvestmentPlanSatisfactionLevel.VERY_SATISFIED);
        investmentPlan.setSystemPredictReturn(0.5);
        investmentPlan.setSystemPredictRisk(40);
        Portfolio p = new Portfolio();
        List<InvestmentPlan> allInvestmentPlans = new ArrayList<InvestmentPlan>();
        p.setInvestmentPlans(allInvestmentPlans);
        p.setExecutedInvestmentPlan(investmentPlan);
        p.setWealthManagementSubscriber(wms);
        investmentPlan.setPortfolio(p);
        investmentPlan.setOnePortfolio(p);
        portfolioSessionBean.createPortfolio(p);
        investmentPlanSessionBean.createInvestmentPlan(investmentPlan);
        

        //generate PortfolioModel table
        constructPortfolioModel();
        return wms;
    }

    public static void constructPortfolioModel() {
        RConnection connection = null;

        try {
            /* Create a connection to Rserve instance running
             * on default port 6311
             */
            connection = new RConnection("127.0.0.1", 6311);

            String prependingPath = CommonUtils.getPrependFolderName();
            
            connection.eval("source('"+prependingPath+"ConstructPortfolioModel.R')");

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

}
