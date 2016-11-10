/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.report.ReportGenerationBeanLocal;
import entity.card.account.CreditCardAccount;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.DateUtils;
import utils.MessageUtils;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "eStatementCounterManagedBean")
@ViewScoped
public class EStatementCounterManagedBean implements Serializable {

    @EJB
    private LoginSessionBeanLocal loginBean;
    @EJB
    private ReportGenerationBeanLocal reportBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private CardAcctSessionBeanLocal cardBean;

    private String customerIC;
    private MainAccount mainAccount;

    private String fromAccountNo;
    private String fromCCNo;
    private String selectedMonth;
    private String selectedMonthCC;
    private List<DepositAccount> accounts = new ArrayList<>();
    private List<CreditCardAccount> ccAccounts = new ArrayList<>();
    private List<String> monthOptions = new ArrayList<>();

    /**
     * Creates a new instance of EStatementManagedBean
     */
    public EStatementCounterManagedBean() {
    }

    public void retrieveMainAccount() {
        try {
            mainAccount = loginBean.getMainAccountByUserIC(getCustomerIC());
            System.out.println("Customer mainaccount:" + mainAccount.getUserID());
            accounts = depositBean.getAllCustomerAccounts(mainAccount.getId());
            ccAccounts = cardBean.getAllActiveCreditCardAccountsByMainId(mainAccount.getId());
            getMonthOptions().add("Nov");
            getMonthOptions().add("Oct");
            selectedMonth = "Nov";
        } catch (Exception ex) {
            System.out.println(ex);
            setMainAccount(null);
            MessageUtils.displayError("Customer Main Account Not Found!");
        }
    }
    
    public void requestEStatement() {
        try {
            String accountNumber = fromAccountNo;
            if (getSelectedMonth().equals("Nov")) {
                Date startDate = DateUtils.getBeginOfMonth();
                Date endDate = DateUtils.getEndOfMonth();
                reportBean.generateMonthlyDepositAccountTransactionReportCounter(accountNumber, startDate, endDate);
                RedirectUtils.redirect("estatement_"
                        + accountNumber
                        + DateUtils.getYearNumber(endDate) + "_"
                        + DateUtils.getMonthNumber(startDate) + "_"
                        + DateUtils.getMonthNumber(endDate) + ".pdf"
                );
            } else if (getSelectedMonth().equals("Oct")) {
                Date startDate = DateUtils.getLastBeginOfMonth();
                Date endDate = DateUtils.getLastEndOfMonth();
                reportBean.generateMonthlyDepositAccountTransactionReportCounter(accountNumber, startDate, endDate);
                RedirectUtils.redirect("estatement_"
                        + accountNumber
                        + DateUtils.getYearNumber(endDate) + "_"
                        + DateUtils.getMonthNumber(startDate) + "_"
                        + DateUtils.getMonthNumber(endDate) + ".pdf"
                );
            }

        } catch (Exception ex) {
            System.out.println("RequestManagedBean.requestEStatement: " + ex.toString());
        }
    }
    
    public void requestCCEStatement() {
        try {
            String ccNumber = fromCCNo;
            System.out.println(fromCCNo);
            System.out.println(getSelectedMonth());
            if (getSelectedMonth().equals("Nov")) {
                Date startDate = DateUtils.getBeginOfMonth();
                Date endDate = DateUtils.getEndOfMonth();
                reportBean.generateMonthlyCreditCardAccountTransactionReportCounter(ccNumber, startDate, endDate);
                RedirectUtils.redirect("cc_estatement_"
                        + ccNumber
                        + DateUtils.getYearNumber(endDate) + "_"
                        + DateUtils.getMonthNumber(startDate) + "_"
                        + DateUtils.getMonthNumber(endDate) + ".pdf"
                );
            } else if (getSelectedMonth().equals("Oct")) {
                Date startDate = DateUtils.getLastBeginOfMonth();
                Date endDate = DateUtils.getLastEndOfMonth();
                reportBean.generateMonthlyCreditCardAccountTransactionReportCounter(ccNumber, startDate, endDate);
                RedirectUtils.redirect("cc_estatement_"
                        + ccNumber
                        + DateUtils.getYearNumber(endDate) + "_"
                        + DateUtils.getMonthNumber(startDate) + "_"
                        + DateUtils.getMonthNumber(endDate) + ".pdf"
                );
            }

        } catch (Exception ex) {
            System.out.println("RequestManagedBean.requestCCEStatement: " + ex.toString());
        }
    }

    /**
     * @return the customerIC
     */
    public String getCustomerIC() {
        return customerIC;
    }

    /**
     * @param customerIC the customerIC to set
     */
    public void setCustomerIC(String customerIC) {
        this.customerIC = customerIC;
    }

    /**
     * @return the mainAccount
     */
    public MainAccount getMainAccount() {
        return mainAccount;
    }

    /**
     * @param mainAccount the mainAccount to set
     */
    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
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
     * @return the fromCCNo
     */
    public String getFromCCNo() {
        return fromCCNo;
    }

    /**
     * @param fromCCNo the fromCCNo to set
     */
    public void setFromCCNo(String fromCCNo) {
        this.fromCCNo = fromCCNo;
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
     * @return the selectedMonthCC
     */
    public String getSelectedMonthCC() {
        return selectedMonthCC;
    }

    /**
     * @param selectedMonthCC the selectedMonthCC to set
     */
    public void setSelectedMonthCC(String selectedMonthCC) {
        this.selectedMonthCC = selectedMonthCC;
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
     * @return the ccAccounts
     */
    public List<CreditCardAccount> getCcAccounts() {
        return ccAccounts;
    }

    /**
     * @param ccAccounts the ccAccounts to set
     */
    public void setCcAccounts(List<CreditCardAccount> ccAccounts) {
        this.ccAccounts = ccAccounts;
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
