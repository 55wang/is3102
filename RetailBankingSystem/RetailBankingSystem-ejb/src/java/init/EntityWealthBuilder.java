/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.fact.PortfolioFactSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import ejb.session.wealth.FinancialInstrumentSessionBeanLocal;
import ejb.session.wealth.InvestmentPlanSessionBeanLocal;
import ejb.session.wealth.PortfolioSessionBeanLocal;
import ejb.session.wealth.WealthManegementSubscriberSessionBeanLocal;
import entity.customer.MainAccount;
import entity.customer.WealthManagementSubscriber;
import entity.fact.customer.FinancialInstrumentFactTable;
import entity.wealth.FinancialInstrument;
import entity.wealth.FinancialInstrumentAndWeight;
import entity.wealth.InvestmentPlan;
import entity.wealth.Portfolio;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;
import server.utilities.CommonUtils;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils;
import util.exception.common.MainAccountNotExistException;
import util.exception.common.UpdateMainAccountException;

/**
 *
 * @author VIN-S
 */
@Stateless
@LocalBean
public class EntityWealthBuilder {
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
    @EJB
    private PortfolioFactSessionBeanLocal factSessionBean;

    private String currentDate;
    private String monthStartDate;
    private String monthEndDate;

    public Portfolio initWealth(List<FinancialInstrument> allFinancialInstruments) {
        try {

            //generate PortfolioModel table
            constructPortfolioModel();

            MainAccount demoMainAccount = null;
            try{
                demoMainAccount = mainAccountSessionBean.getMainAccountByUserId(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID_1);
            }catch(MainAccountNotExistException ex){
                System.out.println("EntityWealthBuilder.initWealth.MainAccountNotExistException");
            }
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
            List<FinancialInstrument> preferedInstruments = new ArrayList<>();
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
            List<InvestmentPlan> ips = wms.getInvestmentPlans();
            ips.add(investmentPlan);
            wms.setInvestmentPlans(ips);
            wealthManegementSubscriberSessionBean.updateWealthManagementSubscriber(wms);

            InvestmentPlan executedInvestmentPlan = new InvestmentPlan();
            executedInvestmentPlan.setAmountOfInvestment(5000);
            List<FinancialInstrument> preferedInstruments2 = new ArrayList<>();
            preferedInstruments2.add(allFinancialInstruments.get(0));
            preferedInstruments2.add(allFinancialInstruments.get(2));
            preferedInstruments2.add(allFinancialInstruments.get(5));
            preferedInstruments2.add(allFinancialInstruments.get(7));
            executedInvestmentPlan.setPreferedFinancialInstrument(preferedInstruments2);
            executedInvestmentPlan.setRemarks("test executed plan");
            executedInvestmentPlan.setStatus(EnumUtils.InvestmentPlanStatus.EXECUTED);
            executedInvestmentPlan.setWealthManagementSubscriber(wms);
            Calendar cal = Calendar.getInstance();
            Calendar xdate = (Calendar) cal.clone();
            xdate.set(Calendar.DAY_OF_YEAR, cal.getTime().getDate() - 20);
            executedInvestmentPlan.setExecutionDate(xdate.getTime());
            investmentPlanSessionBean.createInvestmentPlan(executedInvestmentPlan);
            List<InvestmentPlan> ips2 = wms.getInvestmentPlans();
            ips2.add(executedInvestmentPlan);
            wms.setInvestmentPlans(ips2);
            wealthManegementSubscriberSessionBean.updateWealthManagementSubscriber(wms);

            List<FinancialInstrumentAndWeight> suggestedFinancialInstruments = new ArrayList<>();

            for (int i = 0; i < allFinancialInstruments.size(); i++) {
                if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.CORPORATE_BONDS)) {
                    FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                    fiaw.setFi(allFinancialInstruments.get(i));
                    fiaw.setWeight(0.0);
                    suggestedFinancialInstruments.add(fiaw);
                } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.DIVIDEND_GROWTH_STOCKS)) {
                    FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                    fiaw.setBuyingNumberOfShare(1800);
                    fiaw.setBuyingValuePerShare(1.0);
                    fiaw.setCurrentValuePerShare(1.1);
                    fiaw.setFi(allFinancialInstruments.get(i));
                    fiaw.setWeight(0.34);
                    suggestedFinancialInstruments.add(fiaw);
                } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.EMERGING_MARKET_BONDS)) {
                    FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                    fiaw.setFi(allFinancialInstruments.get(i));
                    fiaw.setWeight(0.0);
                    suggestedFinancialInstruments.add(fiaw);
                } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.EMERGING_MARKET_STOCKS)) {
                    FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                    fiaw.setBuyingNumberOfShare(0);
                    fiaw.setBuyingValuePerShare(0.0);
                    fiaw.setCurrentValuePerShare(0.0);
                    fiaw.setFi(allFinancialInstruments.get(i));
                    fiaw.setWeight(0.0);
                    suggestedFinancialInstruments.add(fiaw);
                } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.FOREIGN_DEVELOPED_STOCKS)) {
                    FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                    fiaw.setBuyingNumberOfShare(175);
                    fiaw.setBuyingValuePerShare(2.0);
                    fiaw.setCurrentValuePerShare(2.0);
                    fiaw.setFi(allFinancialInstruments.get(i));
                    fiaw.setWeight(0.07);
                    suggestedFinancialInstruments.add(fiaw);
                } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.MUNICIPAL_BONDS)) {
                    FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                    fiaw.setBuyingNumberOfShare(25);
                    fiaw.setBuyingValuePerShare(2.0);
                    fiaw.setCurrentValuePerShare(2.1);
                    fiaw.setFi(allFinancialInstruments.get(i));
                    fiaw.setWeight(0.01);
                    suggestedFinancialInstruments.add(fiaw);
                } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.NATURAL_RESOURCES)) {
                    FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                    fiaw.setBuyingNumberOfShare(200);
                    fiaw.setBuyingValuePerShare(3.0);
                    fiaw.setCurrentValuePerShare(2.9);
                    fiaw.setFi(allFinancialInstruments.get(i));
                    fiaw.setWeight(0.12);
                    suggestedFinancialInstruments.add(fiaw);
                } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.REAL_ESTATE)) {
                    FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                    fiaw.setBuyingNumberOfShare(30);
                    fiaw.setBuyingValuePerShare(5.0);
                    fiaw.setCurrentValuePerShare(4.8);
                    fiaw.setFi(allFinancialInstruments.get(i));
                    fiaw.setWeight(0.12);
                    suggestedFinancialInstruments.add(fiaw);
                } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.TREASURY_INFLATION_PROTECTED_SECURITIES)) {
                    FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                    fiaw.setFi(allFinancialInstruments.get(i));
                    fiaw.setWeight(0.0);
                    suggestedFinancialInstruments.add(fiaw);
                } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.US_GOVERNMENT_BONDS)) {
                    FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                    fiaw.setBuyingNumberOfShare(400);
                    fiaw.setBuyingValuePerShare(1.0);
                    fiaw.setCurrentValuePerShare(1.2);
                    fiaw.setFi(allFinancialInstruments.get(i));
                    fiaw.setWeight(0.08);
                    suggestedFinancialInstruments.add(fiaw);
                } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.US_STOCKS)) {
                    FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                    fiaw.setBuyingNumberOfShare(550);
                    fiaw.setBuyingValuePerShare(3.0);
                    fiaw.setCurrentValuePerShare(3.5);
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
        } catch (UpdateMainAccountException e) {
            System.out.println("Exception at entityWealth builder");
            return null;
        }
    }

    public Portfolio initPortfolioFactTable2(MainAccount ma, List<FinancialInstrument> allFinancialInstruments) {

        //2nd portfolio
        InvestmentPlan executedInvestmentPlan = new InvestmentPlan();
        executedInvestmentPlan.setAmountOfInvestment(15000);
        List<FinancialInstrument> preferedInstruments = new ArrayList<>();
//        List<FinancialInstrument> allFinancialInstruments = financialInstrumentSessionBean.getAllFinancialInstruments();
        preferedInstruments.add(allFinancialInstruments.get(0));
        preferedInstruments.add(allFinancialInstruments.get(2));
        preferedInstruments.add(allFinancialInstruments.get(5));
        preferedInstruments.add(allFinancialInstruments.get(7));
        executedInvestmentPlan.setPreferedFinancialInstrument(preferedInstruments);
        executedInvestmentPlan.setRemarks("test executed plan 3");
        executedInvestmentPlan.setStatus(EnumUtils.InvestmentPlanStatus.EXECUTED);
        executedInvestmentPlan.setWealthManagementSubscriber(ma.getWealthManagementSubscriber());
        Calendar cal = Calendar.getInstance();
        Calendar xdate = (Calendar) cal.clone();
        xdate.set(Calendar.DAY_OF_YEAR, cal.getTime().getDay() - 50);
        executedInvestmentPlan.setExecutionDate(xdate.getTime());
        investmentPlanSessionBean.createInvestmentPlan(executedInvestmentPlan);
        List<InvestmentPlan> ips3 = ma.getWealthManagementSubscriber().getInvestmentPlans();
        ips3.add(executedInvestmentPlan);
        ma.getWealthManagementSubscriber().setInvestmentPlans(ips3);
        wealthManegementSubscriberSessionBean.updateWealthManagementSubscriber(ma.getWealthManagementSubscriber());

        List<FinancialInstrumentAndWeight> suggestedFinancialInstruments = new ArrayList<FinancialInstrumentAndWeight>();

        for (int i = 0; i < allFinancialInstruments.size(); i++) {
            if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.CORPORATE_BONDS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.0);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.DIVIDEND_GROWTH_STOCKS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(258);
                fiaw.setBuyingValuePerShare(7.0);
                fiaw.setCurrentValuePerShare(6.9);
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
                fiaw.setBuyingNumberOfShare(275);
                fiaw.setBuyingValuePerShare(6.0);
                fiaw.setCurrentValuePerShare(6.7);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.11);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.FOREIGN_DEVELOPED_STOCKS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(1125);
                fiaw.setBuyingValuePerShare(2.0);
                fiaw.setCurrentValuePerShare(2.3);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.15);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.MUNICIPAL_BONDS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(50);
                fiaw.setBuyingValuePerShare(3.0);
                fiaw.setCurrentValuePerShare(2.9);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.01);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.NATURAL_RESOURCES)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(243);
                fiaw.setBuyingValuePerShare(8.0);
                fiaw.setCurrentValuePerShare(8.4);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.13);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.REAL_ESTATE)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(1350);
                fiaw.setBuyingValuePerShare(1.0);
                fiaw.setCurrentValuePerShare(1.0);
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
                fiaw.setBuyingNumberOfShare(225);
                fiaw.setBuyingValuePerShare(4.0);
                fiaw.setCurrentValuePerShare(5.0);
                fiaw.setFi(allFinancialInstruments.get(i));
                fiaw.setWeight(0.06);
                suggestedFinancialInstruments.add(fiaw);
            } else if (allFinancialInstruments.get(i).getName().equals(EnumUtils.FinancialInstrumentClass.US_STOCKS)) {
                FinancialInstrumentAndWeight fiaw = new FinancialInstrumentAndWeight();
                fiaw.setBuyingNumberOfShare(990);
                fiaw.setBuyingValuePerShare(5.0);
                fiaw.setCurrentValuePerShare(5.0);
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

        ma.getWealthManagementSubscriber().setMonthlyAdvisoryFee(2.05);
        ma.getWealthManagementSubscriber().setAccumulatedAdvisoryFee(0.99);
        ma.getWealthManagementSubscriber().setAdvisoryFeeClearDate(p3.getExecutedInvestmentPlan().getExecutionDate());

        wealthManegementSubscriberSessionBean.updateWealthManagementSubscriber(ma.getWealthManagementSubscriber());

        return p3;

    }

    public static void constructPortfolioModel() {
        RConnection connection = null;

        try {
            /* Create a connection to Rserve instance running
             * on default port 6311
             */
            connection = new RConnection("localhost", 6311);

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

        initDate();

        FinancialInstrument financialInstrument1 = buildFinancialInstrument(EnumUtils.FinancialInstrumentClass.CORPORATE_BONDS, "Income, low historical volatility, diversification", 0.05, -0.002, "iShares LQD ETF", "https://www.ishares.com/us/products/239566/ishares-iboxx-investment-grade-corporate-bond-etf?fundSearch=true&qt=LQD");
        buildFinFactTable("FB", financialInstrument1);

        FinancialInstrument financialInstrument2 = buildFinancialInstrument(EnumUtils.FinancialInstrumentClass.DIVIDEND_GROWTH_STOCKS, "Capital growth, income, long-run inflation protection, tax efficiency", 0.14, 0.037, "Vanguard VIG ETF", "https://personal.vanguard.com/us/funds/snapshot?FundId=0920&FundIntExt=INT");
        buildFinFactTable("AAPL", financialInstrument2);

        FinancialInstrument financialInstrument3 = buildFinancialInstrument(EnumUtils.FinancialInstrumentClass.EMERGING_MARKET_BONDS, "Income, diversification", 0.07, 0.01, "iShares EMB ETF", "https://www.ishares.com/us/products/239572/ishares-jp-morgan-usd-emerging-markets-bond-etf?fundSearch=true&qt=EMB");
        buildFinFactTable("IBM", financialInstrument3);

        FinancialInstrument financialInstrument4 = buildFinancialInstrument(EnumUtils.FinancialInstrumentClass.EMERGING_MARKET_STOCKS, "Capital growth, long-run inflation protection, tax efficiency", 0.24, 0.081, "Vanguard VWO ETF", "https://personal.vanguard.com/us/FundsSnapshot?FundId=0964&FundIntExt=INT");
        buildFinFactTable("AMZN", financialInstrument4);

        FinancialInstrument financialInstrument5 = buildFinancialInstrument(EnumUtils.FinancialInstrumentClass.FOREIGN_DEVELOPED_STOCKS, "Capital growth, long-run inflation protection, tax efficiency", 0.18, 0.062, "Vanguard VEA ETF", "https://personal.vanguard.com/us/funds/snapshot?FundId=0936&FundIntExt=INT");
        buildFinFactTable("GOOG", financialInstrument5);

        FinancialInstrument financialInstrument6 = buildFinancialInstrument(EnumUtils.FinancialInstrumentClass.MUNICIPAL_BONDS, "Income, low historical volatility, diversification, tax efficiency", 0.05, -0.08, "iShares MUB ETF", "https://www.ishares.com/us/products/239766/ishares-national-amtfree-muni-bond-etf");
        buildFinFactTable("NFLX", financialInstrument6);

        FinancialInstrument financialInstrument7 = buildFinancialInstrument(EnumUtils.FinancialInstrumentClass.NATURAL_RESOURCES, "Diversification, inflation protection, tax efficiency", 0.22, 0.062, "State Street XLE ETF", "https://www.spdrs.com/product/fund.seam?ticker=XLE");
        buildFinFactTable("C", financialInstrument7);

        FinancialInstrument financialInstrument8 = buildFinancialInstrument(EnumUtils.FinancialInstrumentClass.REAL_ESTATE, "Income, diversification, inflation protection", 0.18, 0.05, "Vanguard VNQ ETF", "https://personal.vanguard.com/us/FundsSnapshot?FundId=0986&FundIntExt=INT");
        buildFinFactTable("CAG", financialInstrument8);

        FinancialInstrument financialInstrument9 = buildFinancialInstrument(EnumUtils.FinancialInstrumentClass.TREASURY_INFLATION_PROTECTED_SECURITIES, "Income, low historical volatility, diversification, inflation protection", 0.05, -0.005, "Schwab SCHP ETF", "https://www.csimfunds.com/public/csim/home/products/exchange_traded_funds/summary.html?symbol=SCHP");
        buildFinFactTable("JPM", financialInstrument9);

        FinancialInstrument financialInstrument10 = buildFinancialInstrument(EnumUtils.FinancialInstrumentClass.US_GOVERNMENT_BONDS, "Income, low historical volatility, diversification", 0.05, -0.008, "Vanguard BND ETF", "https://personal.vanguard.com/us/funds/snapshot?FundIntExt=INT&FundId=0928");
        buildFinFactTable("TDC", financialInstrument10);

        FinancialInstrument financialInstrument11 = buildFinancialInstrument(EnumUtils.FinancialInstrumentClass.US_STOCKS, "Capital growth, long-run inflation protection, tax efficiency", 0.16, 0.053, "Vanguard VTI ETF", "https://personal.vanguard.com/us/FundsSnapshot?FundId=0970&FundIntExt=INT");
        buildFinFactTable("VRSK", financialInstrument11);

        List<FinancialInstrument> allFinancialInstruments = financialInstrumentSessionBean.getAllFinancialInstruments();

        return allFinancialInstruments;
    }

    public void buildFinFactTable(String stockName, FinancialInstrument financialInstrument) {
        Client client = ClientBuilder.newClient();
        String quandl = "https://www.quandl.com/api/v3/datasets/WIKI/" + stockName + ".json?column_index=4&start_date=" + monthStartDate + "&transform=diff&api_key=wh4e1aGKQwZyE4RXWP7s";
        WebTarget target = client.target(quandl);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).get(JsonObject.class);
        System.out.println(jsonString);

        JsonObject dataset = (JsonObject) jsonString.getJsonObject("dataset");
        JsonArray data = (JsonArray) dataset.getJsonArray("data");

        List<FinancialInstrumentFactTable> listFif = financialInstrument.getFif();

        for (int i = 0; i < data.size(); i++) {
            FinancialInstrumentFactTable fif = new FinancialInstrumentFactTable();
            fif.setFi(financialInstrument);

            JsonArray innerData = (JsonArray) data.getJsonArray(i);
            System.out.println(innerData.get(0) + " " + innerData.get(1));

            System.out.println(innerData.get(0).toString());
            Date date;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(innerData.get(0).toString().substring(1, innerData.get(0).toString().length() - 1));
                System.out.println(date);
                fif.setCreationDate(date);
            } catch (Exception ex) {
                System.out.println(ex);
            }

            Double perc = Double.parseDouble(innerData.get(1).toString()) / 100;
            fif.setInstrumentValueChange(perc);

            factSessionBean.createFinancialInstrumentFactTable(fif);
            listFif.add(fif);
        }
        financialInstrument.setFif(listFif);
        financialInstrumentSessionBean.updateFinancialInstrument(financialInstrument);
    }

    public FinancialInstrument buildFinancialInstrument(EnumUtils.FinancialInstrumentClass name, String desc, Double sd, Double er, String ETFName, String ETFLink) {
        FinancialInstrument financialInstrument1 = new FinancialInstrument();
        financialInstrument1.setName(name);
        financialInstrument1.setDescription(desc);
        financialInstrument1.setStandardDeviation(sd);
        financialInstrument1.setExpectedReturn(er);
        financialInstrument1.setETFName(ETFName);
        financialInstrument1.setETFLink(ETFLink);
        financialInstrumentSessionBean.createFinancialInstrument(financialInstrument1);
        return financialInstrument1;
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
