/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import entity.customer.Customer;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FlowEvent;
import utils.EnumUtils.DepositAccountType;
import utils.EnumUtils.StatusType;
import utils.MessageUtils;
import utils.RedirectUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "customerApplicationManagedBean")
@ViewScoped
public class CustomerApplicationManagedBean implements Serializable {

    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private NewCustomerSessionBeanLocal newCustomerSessionBean;

    private Customer customer = new Customer();
    private String initialDepositAccount;

    /**
     * Creates a new instance of customerApplicationManagedBean
     */
    public CustomerApplicationManagedBean() {
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

    public void save() {

        customer.setMainAccount(new MainAccount());
        MainAccount mainAccount = customer.getMainAccount();
        Boolean emailSuccessFlag = true;

        mainAccount.setStatus(StatusType.PENDING);

        mainAccount.setUserID(generateUserID(customer.getIdentityType(), customer.getIdentityNumber()));
        String randomPwd = generatePwd();
        mainAccount.setPassword(randomPwd);

        List<DepositAccount> bankAccounts = new ArrayList<>();
        switch (initialDepositAccount) {
            case "MBS Current Account":
                CustomerDepositAccount currentAccount = new CustomerDepositAccount();
                currentAccount.setType(DepositAccountType.CURRENT);
                bankAccounts.add(currentAccount);
                mainAccount.setBankAcounts(bankAccounts);
                currentAccount.setMainAccount(mainAccount);
                break;
            case "MBS Savings":
                CustomerDepositAccount savingAccount = new CustomerDepositAccount();
                savingAccount.setType(DepositAccountType.SAVING);
                bankAccounts.add(savingAccount);
                mainAccount.setBankAcounts(bankAccounts);
                savingAccount.setMainAccount(mainAccount);
                break;
        };

        try {
            emailServiceSessionBean.sendActivationGmailForCustomer(customer.getEmail(), randomPwd);
        } catch (Exception ex) {
            emailSuccessFlag = false;
        }

        if (emailSuccessFlag) {
            newCustomerSessionBean.createCustomer(customer);
            RedirectUtils.redirect("../common/register_successful.xhtml");
        } else {
            MessageUtils.displayInfo("Fail!");
        }

//        emailServiceSessionBean.sendActivationEmailForCustomer(customer.getEmail());
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public String generateUserID(String identityType, String identityNum) {
        if (identityType.equals("Singaporean/PR NRIC")) {
            return "c" + identityNum.substring(1, identityNum.length() - 1);
        } else if (identityType.equals("Passport")) {
            return "c" + identityNum.substring(1);
        } else {
            return "error";
        }
    }

    private String generatePwd() {
        int pwdLen = 10;
        SecureRandom rnd = new SecureRandom();

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(pwdLen);
        for (int i = 0; i < pwdLen; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}
