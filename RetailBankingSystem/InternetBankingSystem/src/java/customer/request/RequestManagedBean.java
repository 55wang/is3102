/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.request;

import ejb.session.dams.CustomerDepositSessionBeanLocal;
import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import ejb.session.report.ReportGenerationBeanLocal;
import entity.dams.account.DepositAccount;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import server.utilities.DateUtils;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author litong
 */
@Named(value = "requestManagedBean")
@ViewScoped
public class RequestManagedBean implements Serializable {

    @EJB
    private ReportGenerationBeanLocal reportBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    
    private String fromAccountNo;
    private String selectedMonth;
    private List<DepositAccount> accounts = new ArrayList<>();
    private List<String> monthOptions = new ArrayList<>();
    
    /**
     * Creates a new instance of RequestManagedBean
     */
    public RequestManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        setAccounts(depositBean.getAllCustomerAccounts(Long.parseLong(SessionUtils.getUserId())));
        getMonthOptions().add("Oct");
        getMonthOptions().add("Sept");
        getMonthOptions().add("Aug");
    }

    public void requestEStatement() {
        try {
            String accountNumber = fromAccountNo;
            Date startDate = DateUtils.getBeginOfMonth();
            Date endDate = DateUtils.getEndOfMonth();
            reportBean.generateMonthlyDepositAccountTransactionReport(accountNumber, startDate, endDate);
            RedirectUtils.redirect("estatement_" + accountNumber + DateUtils.getYearNumber(endDate) + "_" + DateUtils.getMonthNumber(startDate) + "_" + DateUtils.getMonthNumber(endDate) + ".pdf");
        } catch (Exception ex) {
            System.out.println("RequestManagedBean.requestEStatement: " + ex.toString());
        }
    }

    /**
     * @return the fromAccountNo
     */
    public String getFromAccountNo() {
        return fromAccountNo;
    }

    /**
     * @param fromAccountNo the fromAccountNo to set
     */
    public void setFromAccountNo(String fromAccountNo) {
        this.fromAccountNo = fromAccountNo;
    }

    /**
     * @return the accounts
     */
    public List<DepositAccount> getAccounts() {
        return accounts;
    }

    /**
     * @param accounts the accounts to set
     */
    public void setAccounts(List<DepositAccount> accounts) {
        this.accounts = accounts;
    }

    /**
     * @return the selectedMonth
     */
    public String getSelectedMonth() {
        return selectedMonth;
    }

    /**
     * @param selectedMonth the selectedMonth to set
     */
    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth = selectedMonth;
    }

    /**
     * @return the monthOptions
     */
    public List<String> getMonthOptions() {
        return monthOptions;
    }

    /**
     * @param monthOptions the monthOptions to set
     */
    public void setMonthOptions(List<String> monthOptions) {
        this.monthOptions = monthOptions;
    }
}
