/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.MainAccountSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import entity.BankAccount;
import entity.CurrentAccount;
import entity.Customer;
import entity.MainAccount;
import entity.MainAccount.StatusType;
import entity.SavingAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FlowEvent;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "customerRegisterManagedBean")
@ViewScoped
public class CustomerRegisterManagedBean implements Serializable {

    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private NewCustomerSessionBeanLocal newCustomerSessionBean;
    @EJB
    private MainAccountSessionBeanLocal newMainAccountSessionBean;

    private Customer customer = new Customer();
    private MainAccount mainAccount = new MainAccount();
    private String initialDepositAccount;
    private MainAccount loginAccount = new MainAccount();

    /**
     * Creates a new instance of CustomerRegisterManagedBean
     */
    public CustomerRegisterManagedBean() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getInitialDepositAccount() {
        return initialDepositAccount;
    }

    public void setInitialDepositAccount(String initialDepositAccount) {
        this.initialDepositAccount = initialDepositAccount;
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    public void save() {
        mainAccount.setStatus(StatusType.PENDING);

        List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
        switch (initialDepositAccount) {
            case "MBS Current Account":
                CurrentAccount currentAccount = new CurrentAccount();
                bankAccounts.add(currentAccount);
                mainAccount.setBankAcounts(bankAccounts);
                currentAccount.setMainAccount(mainAccount);
                break;
            case "MBS Savings":
                SavingAccount savingAccount = new SavingAccount();
                bankAccounts.add(savingAccount);
                mainAccount.setBankAcounts(bankAccounts);
                savingAccount.setMainAccount(mainAccount);
                break;
        };

        newCustomerSessionBean.createCustomer(customer, mainAccount);

        FacesMessage msg = new FacesMessage("Successful", "Welcome :" + customer.getFirstname() + " " + customer.getLastname());
        FacesContext.getCurrentInstance().addMessage(null, msg);

        emailServiceSessionBean.sendActivationEmailForNewCustomer();
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public MainAccount getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(MainAccount loginAccount) {
        this.loginAccount = loginAccount;
    }

    public void loginCustomer() {
        try {
            Long userID = newMainAccountSessionBean.loginAccount(loginAccount.getUserID(), loginAccount.getPassword()).getId();
            String userName = newMainAccountSessionBean.loginAccount(loginAccount.getUserID(), loginAccount.getPassword()).getUserID();
            SessionUtils.setUserId(userID);
            SessionUtils.setUserName(userName);
            RedirectUtils.redirect("success.xhtml");
        } catch (NullPointerException e) {
            RedirectUtils.redirect("fail.xhtml");
        }

    }
}
