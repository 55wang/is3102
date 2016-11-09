/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.common.CustomerDashboardSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import ejb.session.message.AnnouncementSessionBeanLocal;
import entity.customer.MainAccount;
import entity.staff.Announcement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import util.exception.common.MainAccountNotExistException;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "customerDashboardManagedBean")
@ViewScoped
public class CustomerDashboardManagedBean implements Serializable {

    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;
    @EJB
    private AnnouncementSessionBeanLocal announcementSessionBean;
    @EJB
    private CustomerDashboardSessionBeanLocal customerDashboardSessionBean;

    private MainAccount ma;

    private Long customerDepositAccountNumber;
    private Long customerLoanAccountNumber;
    private Long customerCreditCardAccountNumber;
    private Long customerExecutedInvestmentPlanNumber;

    private Double customerDepositAmount;
    private Double customerLoanAmount;
    private Double customerCreditCardAmount;
    private Double customerInvestmentAmount;

    private List<Announcement> announcementLists = new ArrayList<>();
    private Announcement latestAnnouncement;

    private BarChartModel customerAcctBarModel;

    private ScheduleModel lazyEventModel;

    /**
     * Creates a new instance of CustomerDashboardManagedBean
     */
    public CustomerDashboardManagedBean() {
    }

    @PostConstruct
    public void init() {
        try {
            ma = mainAccountSessionBean.getMainAccountByUserId(SessionUtils.getUserName());
        } catch (MainAccountNotExistException ex) {
            System.out.println("setMainAccount.MainAccountNotExistException");
        }

        customerDepositAccountNumber = customerDashboardSessionBean.getCustomerTotalDepositAccountByMainAccountUserId(ma.getUserID());
        customerLoanAccountNumber = customerDashboardSessionBean.getCustomerTotalLoanAccountByMainAccountUserId(ma.getUserID());
        customerCreditCardAccountNumber = customerDashboardSessionBean.getCustomerTotalCreditCardAccountByMainAccountUserId(ma.getUserID());
        customerExecutedInvestmentPlanNumber = customerDashboardSessionBean.getCustomerTotalExecutedInvestmentPlanByMainAccountUserId(ma.getUserID());

        customerDepositAmount = customerDashboardSessionBean.getCustomerTotalDepositAmountByMainAccountUserId(ma.getUserID());
        customerLoanAmount = customerDashboardSessionBean.getCustomerTotalLoanAmountByMainAccountUserId(ma.getUserID());
        customerCreditCardAmount = customerDashboardSessionBean.getCustomerTotalCreditCardAmountByMainAccountUserId(ma.getUserID());
        customerInvestmentAmount = customerDashboardSessionBean.getCustomerTotalExecutedInvestmentAmountByMainAccountUserId(ma.getUserID());

        if (customerDepositAmount == null) {
            customerDepositAmount = 0.0;
        }
        if (customerLoanAmount == null) {
            customerLoanAmount = 0.0;
        }
        if (customerCreditCardAmount == null) {
            customerCreditCardAmount = 0.0;
        }
        if (customerInvestmentAmount == null) {
            customerInvestmentAmount = 0.0;
        }

        System.out.println("customerDepositAmount: " + customerDepositAmount);
        System.out.println("customerLoanAmount: " + customerLoanAmount);
        System.out.println("customerCreditCardAmount: " + customerCreditCardAmount);
        System.out.println("customerInvestmentAmount: " + customerInvestmentAmount);

        announcementLists = announcementSessionBean.getAllAnnouncements(null, Boolean.FALSE);
        if (announcementLists.isEmpty()) {
            latestAnnouncement = new Announcement();
            latestAnnouncement.setTitle("No Announcement Found");
            latestAnnouncement.setContent("No Content");
            latestAnnouncement.setCreationDate(new Date());
        } else {
            latestAnnouncement = announcementLists.get(0);
        }

        createCustomerAcctBarModel();

        lazyEventModel = new LazyScheduleModel() {

            @Override
            public void loadEvents(Date startDate, Date endDate) {
                Date random = getRandomDate(startDate);
                addEvent(new DefaultScheduleEvent("Report", random, random));

                random = getRandomDate(startDate);
                addEvent(new DefaultScheduleEvent("Meeting", random, random));
            }
        };
    }

    private BarChartModel createCustomerAcctBarModel() {
        customerAcctBarModel = initCustomerAcctBarModel();
        customerAcctBarModel.setTitle("Total Amounts");
        customerAcctBarModel.setLegendPosition("ne");

        Axis xAxis = customerAcctBarModel.getAxis(AxisType.X);
        Axis yAxis = customerAcctBarModel.getAxis(AxisType.Y);


        return customerAcctBarModel;
    }

    private BarChartModel initCustomerAcctBarModel() {
        BarChartModel model = new BarChartModel();

        ChartSeries series1 = new ChartSeries();
        ChartSeries series2 = new ChartSeries();
        ChartSeries series3 = new ChartSeries();
        ChartSeries series4 = new ChartSeries();
        series1.setLabel("Total Deposit Amount");
        series2.setLabel("Total Loan Amount");
        series3.setLabel("Total Credit Card Spending Amount");
        series4.setLabel("Total Investment Amount");

        series1.set("", customerDepositAmount);
        series2.set("", customerLoanAmount);
        series3.set("", customerCreditCardAmount);
        series4.set("", customerInvestmentAmount);

        model.addSeries(series1);
        model.addSeries(series2);
        model.addSeries(series3);
        model.addSeries(series4);
        model.setSeriesColors("2196F3,009688,FFEB3B,F44336");
        model.setExtender("customExtender");

        return model;
    }

    public Date getRandomDate(Date base) {
        Calendar date = Calendar.getInstance();
        date.setTime(base);
        date.add(Calendar.DATE, ((int) (Math.random() * 30)) + 1);    //set random day of month

        return date.getTime();
    }

    public Long getCustomerDepositAccountNumber() {
        return customerDepositAccountNumber;
    }

    public void setCustomerDepositAccountNumber(Long customerDepositAccountNumber) {
        this.customerDepositAccountNumber = customerDepositAccountNumber;
    }

    public Long getCustomerLoanAccountNumber() {
        return customerLoanAccountNumber;
    }

    public void setCustomerLoanAccountNumber(Long customerLoanAccountNumber) {
        this.customerLoanAccountNumber = customerLoanAccountNumber;
    }

    public Long getCustomerCreditCardAccountNumber() {
        return customerCreditCardAccountNumber;
    }

    public void setCustomerCreditCardAccountNumber(Long customerCreditCardAccountNumber) {
        this.customerCreditCardAccountNumber = customerCreditCardAccountNumber;
    }

    public Long getCustomerExecutedInvestmentPlanNumber() {
        return customerExecutedInvestmentPlanNumber;
    }

    public void setCustomerExecutedInvestmentPlanNumber(Long customerExecutedInvestmentPlanNumber) {
        this.customerExecutedInvestmentPlanNumber = customerExecutedInvestmentPlanNumber;
    }

    public Double getCustomerDepositAmount() {
        return customerDepositAmount;
    }

    public void setCustomerDepositAmount(Double customerDepositAmount) {
        this.customerDepositAmount = customerDepositAmount;
    }

    public Double getCustomerLoanAmount() {
        return customerLoanAmount;
    }

    public void setCustomerLoanAmount(Double customerLoanAmount) {
        this.customerLoanAmount = customerLoanAmount;
    }

    public Double getCustomerCreditCardAmount() {
        return customerCreditCardAmount;
    }

    public void setCustomerCreditCardAmount(Double customerCreditCardAmount) {
        this.customerCreditCardAmount = customerCreditCardAmount;
    }

    public Double getCustomerInvestmentAmount() {
        return customerInvestmentAmount;
    }

    public void setCustomerInvestmentAmount(Double customerInvestmentAmount) {
        this.customerInvestmentAmount = customerInvestmentAmount;
    }

    public List<Announcement> getAnnouncementLists() {
        return announcementLists;
    }

    public void setAnnouncementLists(List<Announcement> announcementLists) {
        this.announcementLists = announcementLists;
    }

    public MainAccount getMa() {
        return ma;
    }

    public void setMa(MainAccount ma) {
        this.ma = ma;
    }

    public BarChartModel getCustomerAcctBarModel() {
        return customerAcctBarModel;
    }

    public void setCustomerAcctBarModel(BarChartModel customerAcctBarModel) {
        this.customerAcctBarModel = customerAcctBarModel;
    }

    public ScheduleModel getLazyEventModel() {
        return lazyEventModel;
    }

    public void setLazyEventModel(ScheduleModel lazyEventModel) {
        this.lazyEventModel = lazyEventModel;
    }

    public Announcement getLatestAnnouncement() {
        return latestAnnouncement;
    }

    public void setLatestAnnouncement(Announcement latestAnnouncement) {
        this.latestAnnouncement = latestAnnouncement;
    }
}
