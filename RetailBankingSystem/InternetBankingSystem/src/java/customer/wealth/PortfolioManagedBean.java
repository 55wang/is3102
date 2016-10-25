/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.wealth;

import ejb.session.cms.CustomerProfileSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.wealth.PortfolioSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import entity.customer.Customer;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import entity.loan.LoanAccount;
import entity.wealth.Portfolio;
import java.io.Serializable;
import java.math.BigDecimal;
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
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;
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

    private List<Portfolio> portfolios;
    private Customer customer;
    private PieChartModel pieModel2;
    private Double financialHealth;
    private LineChartModel lineModel1;

    public PortfolioManagedBean() {
    }

    @PostConstruct
    public void init() {
        customer = customerProfileSessionBean.getCustomerByUserID(SessionUtils.getUserName());
        portfolios = portfolioSessionBean.getListPortfoliosByCustomerId(customer.getId());
        createPieModels();
        createLineModels();
        financialHealth = 0.0;
    }

    public LineChartModel getLineModel1() {
        return lineModel1;
    }

    private void createLineModels() {
        lineModel1 = initLinearModel();
        lineModel1.setTitle("Linear Chart");
        lineModel1.setLegendPosition("e");
        Axis yAxis = lineModel1.getAxis(AxisType.Y);
        yAxis.setMin(-10);
        yAxis.setMax(10);
    }

    private LineChartModel initLinearModel() {
        String quandl = "https://www.quandl.com/api/v3/datasets/WIKI/FB.json?column_index=4&start_date=2016-10-01&transform=diff&api_key=wh4e1aGKQwZyE4RXWP7s";
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(quandl);

        // This is the response
        JsonObject jsonString = target.request(MediaType.APPLICATION_JSON).get(JsonObject.class);
        System.out.println(jsonString);

        JsonObject dataset = (JsonObject) jsonString.getJsonObject("dataset");
        JsonArray data = (JsonArray) dataset.getJsonArray("data");

        LineChartModel model = new LineChartModel();

        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Series 1");

        for (int i = 0; i < data.size(); i++) {
            JsonArray innerData = (JsonArray) data.getJsonArray(i);
            System.out.println(innerData.get(0)+" "+ innerData.get(1));
            series1.set(innerData.get(0), innerData.getInt(1));
        }

        model.addSeries(series1);
        return model;
    }

    public Double getFinancialHealthScore() {
        /*
         http://www.thefrugaltoad.com/personalfinance/personal-financial-ratios-everyone-should-know
        
         Savings to Income (S to I) Savings Ratio: Should be between 10% to 20%.
        
         Debt to Income (D to I) Consumer Debt Ratio: Should not exceed 20%.
        
         Housing Cost Ratio:  Should not exceed 28% of gross income.  
         = Total of monthly mortgage payment (principal + interest) / the gross monthly income
        
         Total Debt Ratio: Should not exceed 36% of gross income.  
         Calculated by dividing total monthly loan payments by monthly income.
        
         networth = asset - liabilities
         */
        return 0.0;
    }

    public PieChartModel getPieModel2() {
        return pieModel2;
    }

    private void createPieModels() {
        createPieModel2();
    }

    private void createPieModel2() {
        pieModel2 = new PieChartModel();

        pieModel2.set("Deposit", getTotalDepositAmount(customer.getMainAccount()));
        pieModel2.set("Loan", getTotalLoanAmount(customer.getMainAccount()));
        pieModel2.set("Credit Card Outstanding", getTotalCreditAmount(customer.getMainAccount()));
        pieModel2.set("Portfolio Value", getTotalPortfolioCurrentValue(customer.getMainAccount()));

        pieModel2.setTitle("Financial Overview");
        pieModel2.setLegendPosition("e");
        pieModel2.setFill(false);
        pieModel2.setShowDataLabels(true);
        pieModel2.setDiameter(150);
    }

    public BigDecimal getTotalDepositAmount(MainAccount main) {
        List<DepositAccount> das = main.getBankAcounts();
        BigDecimal totalBalance = new BigDecimal(0);
        for (DepositAccount da : das) {
            totalBalance = totalBalance.add(da.getBalance());
        }
        return totalBalance;
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

    public Double getFinancialHealth() {
        return financialHealth;
    }

    public void setFinancialHealth(Double financialHealth) {
        this.financialHealth = financialHealth;
    }

}