/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.wealth;

import ejb.session.cms.CustomerProfileSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.fact.FactSessionBean;
import ejb.session.fact.FactSessionBeanLocal;
import ejb.session.wealth.PortfolioSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import entity.customer.Customer;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import entity.fact.customer.SinglePortfolioFactTable;
import entity.loan.LoanAccount;
import entity.wealth.Portfolio;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;
import static server.utilities.CommonUtils.round;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.LoanProductType;
import utils.SessionUtils;

/**
 *
 * @author wang
 */
@Named(value = "portfolioManagedBean")
@ViewScoped
public class PortfolioManagedBean implements Serializable {

    @EJB
    PortfolioSessionBeanLocal portfolioSessionBean;
    @EJB
    LoginSessionBeanLocal loginSessionBean;
    @EJB
    CustomerProfileSessionBeanLocal customerProfileSessionBean;
    @EJB
    FactSessionBeanLocal factSessionBean;

    private List<Portfolio> portfolios;
    private Customer customer;
    private PieChartModel pieModel;
    private LineChartModel lineModel;
    private String currentDate;
    private String monthStartDate;
    private String monthEndDate;

    //dropdown menu
    private String selectedPortfolio;

    private List<String> portfolioOptions = new ArrayList<>();

    public void onDropDownChange() {
        
        System.out.println("on drop down changed");
        System.out.println(selectedPortfolio);
        if (!selectedPortfolio.equals("None")) {
            System.out.println("activate model");
            String[] selectedPortfolioIdStringSplit = selectedPortfolio.split("\\s+");
            String selectedPortfolioIdString = selectedPortfolioIdStringSplit[selectedPortfolioIdStringSplit.length - 1];
            createLineModels(selectedPortfolioIdString);
        }
    }

    public PortfolioManagedBean() {
    }

    @PostConstruct
    public void init() {
        initDate();
        customer = customerProfileSessionBean.getCustomerByUserID(SessionUtils.getUserName());
        portfolios = portfolioSessionBean.getListPortfoliosByCustomerId(customer.getId());
        createPieModels();
        System.out.println("portfolio size: " + portfolios.size());
        try {
            for (Portfolio pOption : portfolios) {
                System.out.println(pOption.getId());
                portfolioOptions.add("Investment Plan " + Long.toString(pOption.getId()));
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    public LineChartModel getLineModel() {
        return lineModel;
    }

    private LineChartModel createLineModels(String selectedPortfolioIdString) {
        lineModel = initLinearModel(Long.parseLong(selectedPortfolioIdString));
        lineModel.setTitle("Investment Plan " + selectedPortfolioIdString);
//        lineModel.setLegendPosition("e");
        Axis yAxis = lineModel.getAxis(AxisType.Y);

        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-50);
        axis.setTickFormat("%Y-%m-%d");
        lineModel.getAxes().put(AxisType.X, axis);
        return lineModel;
    }

    private LineChartModel initLinearModel(Long selectedPortfolioIdString) {
//        String quandl = "https://www.quandl.com/api/v3/datasets/WIKI/FB.json?column_index=4&start_date="+monthStartDate+"&transform=diff&api_key=wh4e1aGKQwZyE4RXWP7s";
//        Client client = ClientBuilder.newClient();
//        WebTarget target = client.target(quandl);
//
//        // This is the response
//        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).get(JsonObject.class);
//        System.out.println(jsonString);
//
//        JsonObject dataset = (JsonObject) jsonString.getJsonObject("dataset");
//        JsonArray data = (JsonArray) dataset.getJsonArray("data");

        //sql 
        List<SinglePortfolioFactTable> spf = factSessionBean.getListPortfoliosFtByCustomerIdPortfolioId(customer.getId(), selectedPortfolioIdString);
        System.out.println("spf: " + spf.size());
        SimpleDateFormat simpleformat = new SimpleDateFormat("yyyy-MM-dd");
        LineChartModel model = new LineChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Portfolio ID"); //insert id number

//        for (int i = 0; i < data.size(); i++) {
//            JsonArray innerData = (JsonArray) data.getJsonArray(i);
//            System.out.println(innerData.get(0) + " " + innerData.get(1));
//            series1.set(innerData.get(0).toString(), innerData.getInt(1));
//        }
        for (int i = 0; i < spf.size(); i++) {
            String dateGraph = simpleformat.format(spf.get(i).getCreationDate());
            System.out.println(spf.get(i).getCreationDate() + " " + simpleformat.format(spf.get(i).getCreationDate()));
            System.out.println(spf.get(i).getTotalCurrentValue());
            series1.set(dateGraph, spf.get(i).getTotalCurrentValue());
        }

        model.addSeries(series1);
        return model;
    }

    public Double getTotalMortgageMonthlyInstallment(MainAccount main) {
        List<LoanAccount> las = main.getLoanAccounts();

        Double totalBalance = 0.0;
        for (LoanAccount la : las) {
            if (la.getLoanProduct().getProductType().equals(LoanProductType.LOAN_PRODUCT_TYPE_HDB) || la.getLoanProduct().getProductType().equals(LoanProductType.LOAN_PRODUCT_TYPE_HDB)) {
                totalBalance += la.getMonthlyInstallment();
            }
        }
        return round(totalBalance, 1);
    }

    public Double getTotalMonthlyInstallment(MainAccount main) {
        List<LoanAccount> las = main.getLoanAccounts();

        Double totalBalance = 0.0;
        for (LoanAccount la : las) {
            totalBalance += la.getMonthlyInstallment();
        }
        return round(totalBalance, 1);
    }

    public Double getTotalAsset(MainAccount main) {
        return main.getWealthManagementSubscriber().getTotalPortfolioValue()
                + getTotalDepositAmount(customer.getMainAccount()).doubleValue()
                + getTotalDebtAmount(main);
    }

    public Double getSavingToIncome() {
        return customer.getSavingPerMonth() / customer.getIncome().getAvgValue();
    }

    public Double getDebtToIncome() {
        return getTotalMonthlyInstallment(customer.getMainAccount()) / customer.getIncome().getAvgValue();
    }

    public Double getHousingCostRatio() {
        return getTotalMortgageMonthlyInstallment(customer.getMainAccount()) / customer.getIncome().getAvgValue();
    }

    public Double getDebtRatio() {
        return getTotalDebtAmount(customer.getMainAccount()) / getTotalAsset(customer.getMainAccount());
    }

    public Double getNetWorth() {
        return getTotalAsset(customer.getMainAccount()) - getTotalDebtAmount(customer.getMainAccount());
    }

    public Double calcFinancialHealthScore() {
        Double score = 100.0;
        if (getSavingToIncome() >= 10 && getSavingToIncome() <= 20) {
            score += 5;
        } else if (getSavingToIncome() < 10) {
            //too little saving
            score -= 20;
        } else if (getSavingToIncome() > 20) {
            //too much saving
            score -= 5;
        }

        if (getHousingCostRatio() >= 28) {
            score -= 20;
        } else {
            score += 5;
        }

        if (getDebtRatio() >= 36) {
            score -= 20;
        } else {
            score += 5;
        }

        if (customer.getAge() <= 22 && getNetWorth() > 0.0) {
            score += 5;
        } else if (customer.getAge() <= 25 && getNetWorth() > 50000.0) {
            score += 20;
        } else if (customer.getAge() <= 30 && getNetWorth() > 150000.0) {
            score += 20;
        } else if (customer.getAge() <= 35 && getNetWorth() > 250000.0) {
            score += 20;
        } else if (customer.getAge() <= 40 && getNetWorth() > 400000.0) {
            score += 20;
        } else if (customer.getAge() <= 45 && getNetWorth() > 600000.0) {
            score += 20;
        } else if (customer.getAge() <= 50 && getNetWorth() > 850000.0) {
            score += 20;
        } else if (customer.getAge() <= 55 && getNetWorth() > 1000000.0) {
            score += 20;
        } else if (customer.getAge() <= 60 && getNetWorth() > 1500000.0) {
            score += 20;
        } else if (customer.getAge() >= 60 && getNetWorth() > 2000000.0) {
            score += 20;
        } else {
            score -= 20;
        }

        return score;
        /*
         http://www.thefrugaltoad.com/personalfinance/personal-financial-ratios-everyone-should-know
        
         Savings to Income (S to I) Savings Ratio: Should be between 10% to 20%.
        
         Debt to Income (D to I) Consumer Debt Ratio: Should not exceed 20%.
         Calculated by dividing total monthly loan payments by monthly income.
        
         Housing Cost Ratio:  Should not exceed 28% of gross income.  
         = Total of monthly mortgage payment (principal + interest) / the gross monthly income
        
         Total Debt Ratio: Should not exceed 36% of gross income.  
         = total Debt / Total Asset
         
        
         networth = asset - liabilities
         http://www.financialsamurai.com/the-average-net-worth-for-the-above-average-person/
         */
    }

    public String calcFinancialHealthScoreLevel() {
        if (calcFinancialHealthScore() >= 80) {
            return EnumUtils.FinancialHealthLevel.VERYHEALTHY.getValue();
        } else if (calcFinancialHealthScore() >= 60 && calcFinancialHealthScore() < 80) {
            return EnumUtils.FinancialHealthLevel.HEALTHY.getValue();
        } else if (calcFinancialHealthScore() >= 40 && calcFinancialHealthScore() < 60) {
            return EnumUtils.FinancialHealthLevel.UNHEALTHY.getValue();
        } else {
            return EnumUtils.FinancialHealthLevel.VERYUNHEALTHY.getValue();
        }
    }

    public void initDate() {
        Date current = new Date();
        setCurrentDate(new SimpleDateFormat("yyyy-MM-dd").format(current));

        Calendar cStart = Calendar.getInstance();   // this takes current date
        cStart.set(Calendar.DAY_OF_MONTH, 1);
        setMonthStartDate(new SimpleDateFormat("yyyy-MM-dd").format(cStart.getTime()));

        Calendar cEnd = Calendar.getInstance();   // this takes current date
        cEnd.set(Calendar.DAY_OF_MONTH, cEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        setMonthEndDate(new SimpleDateFormat("yyyy-MM-dd").format(cEnd.getTime()));
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }

    private void createPieModels() {
        createPieModel();
    }

    private void createPieModel() {
        pieModel = new PieChartModel();

        pieModel.set("Deposit", getTotalDepositAmount(customer.getMainAccount()));
        pieModel.set("Loan", getTotalLoanAmount(customer.getMainAccount()));
        pieModel.set("Credit Card Outstanding", getTotalCreditAmount(customer.getMainAccount()));
        pieModel.set("Investment Value", getTotalPortfolioCurrentValue(customer.getMainAccount()));

        pieModel.setTitle("Financial Overview");
        pieModel.setLegendPosition("e");
        pieModel.setShowDataLabels(true);
        pieModel.setDiameter(150);
    }

    public BigDecimal getTotalDepositAmount(MainAccount main) {
        List<DepositAccount> das = main.getBankAcounts();
        BigDecimal totalBalance = new BigDecimal(0);
        for (DepositAccount da : das) {
            totalBalance = totalBalance.add(da.getBalance());
        }
        return totalBalance;
    }

    public Double getTotalDebtAmount(MainAccount main) {
        return getTotalLoanAmount(main) + getTotalCreditAmount(main);
    }

    public Double getTotalLoanAmount(MainAccount main) {
        List<LoanAccount> las = main.getLoanAccounts();

        Double totalBalance = 0.0;
        for (LoanAccount la : las) {
            totalBalance += la.getOutstandingPrincipal();
        }
        return totalBalance;
    }

    public Double getTotalCreditAmount(MainAccount main) {
        List<CreditCardAccount> ccas = main.getCreditCardAccounts();

        Double totalBalance = 0.0;
        for (CreditCardAccount cca : ccas) {
            totalBalance += cca.getOutstandingAmount();
        }
        return totalBalance;
    }

    public Double getTotalPortfolioCurrentValue(MainAccount main) { //all the portfolio
        List<Portfolio> ps = main.getWealthManagementSubscriber().getPortfolios();
        Double totalCurrentPortfoliosValue = 0.0;
        for (Portfolio p : ps) {
            totalCurrentPortfoliosValue += p.getTotalCurrentValue();
        }
        return totalCurrentPortfoliosValue;
    }

    public Double getTotalPortfolioBuyingValue(MainAccount main) {
        List<Portfolio> ports = main.getWealthManagementSubscriber().getPortfolios();
        Double totalValue = 0.0;
        for (Portfolio port : ports) {
            totalValue += port.getTotalBuyingValue();
        }
        return totalValue;
    }

    public Double getPortfolioPercentageChange(MainAccount main) {
        return getTotalPortfolioCurrentValue(main) / getTotalPortfolioBuyingValue(main) * 100;
    }

    public List<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public String getSelectedPortfolio() {
        return selectedPortfolio;
    }

    public void setSelectedPortfolio(String selectedPortfolio) {
        this.selectedPortfolio = selectedPortfolio;
    }

    public List<String> getPortfolioOptions() {
        return portfolioOptions;
    }

    public void setPortfolioOptions(List<String> portfolioOptions) {
        this.portfolioOptions = portfolioOptions;
    }

}
