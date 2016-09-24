/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.dams.DepositProductSessionBeanLocal;
import entity.customer.Customer;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.DepositAccount;
import java.io.Serializable;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FlowEvent;
import server.utilities.EnumUtils;
import server.utilities.ConstantUtils;
import server.utilities.EnumUtils.DepositAccountType;
import server.utilities.EnumUtils.IdentityType;
import server.utilities.EnumUtils.StatusType;
import utils.CommonUtils;
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
    @EJB
    private CustomerDepositSessionBeanLocal depositAccountBean;
    @EJB
    private DepositProductSessionBeanLocal depositProductBean;

    private Customer customer = new Customer();
    private String initialDepositAccount;

    private List<String> selectIdentityTypes = CommonUtils.getEnumList(EnumUtils.IdentityType.class);
    private List<String> selectDepositAccountTypes = CommonUtils.getEnumList(EnumUtils.DepositAccountType.class);
    private List<String> selectNationalities = CommonUtils.getEnumList(EnumUtils.Nationality.class);
    private List<String> selectGenders = CommonUtils.getEnumList(EnumUtils.Gender.class);
    private List<String> selectOccupations = CommonUtils.getEnumList(EnumUtils.Occupation.class);
    private List<String> selectIncome = CommonUtils.getEnumList(EnumUtils.Income.class);
    // TODO: For resend Email Button
    private Boolean emailSuccessFlag = true;

    /**
     * Creates a new instance of customerApplicationManagedBean
     */
    public CustomerApplicationManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        System.out.println("CustomerApplicationManagedBean @PostContruct");
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void save() {

        customer.setMainAccount(new MainAccount());
        MainAccount mainAccount = customer.getMainAccount();
        mainAccount.setStatus(StatusType.PENDING);
        mainAccount.setUserID(generateUserID(customer.getIdentityType(), customer.getIdentityNumber()));
        String randomPwd = generatePwd();
        mainAccount.setPassword(randomPwd);

        newCustomerSessionBean.createCustomer(customer);

        CustomerDepositAccount depostiAccount = new CustomerDepositAccount();
        depostiAccount.setMainAccount(mainAccount);
        if (initialDepositAccount.equals(ConstantUtils.DEMO_CURRENT_DEPOSIT_PRODUCT_NAME)) {
            depostiAccount.setType(DepositAccountType.CURRENT);
        } else if (initialDepositAccount.equals(ConstantUtils.DEMO_CUSTOM_DEPOSIT_PRODUCT_NAME)) {
            depostiAccount.setType(DepositAccountType.CUSTOM);
        }
        depostiAccount.setProduct(depositProductBean.getDepositProductByName(initialDepositAccount));
        depositAccountBean.createAccount(depostiAccount);
        
        try {
            emailServiceSessionBean.sendActivationGmailForCustomer(customer.getEmail(), randomPwd);
            RedirectUtils.redirect("../common/register_successful.xhtml");
        } catch (Exception ex) {
            setEmailSuccessFlag((Boolean) false);
        }

//        emailServiceSessionBean.sendActivationEmailForCustomer(customer.getEmail());
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public String generateUserID(IdentityType identityType, String identityNum) {

        if (identityType.equals(IdentityType.NRIC)) {
            return "c" + identityNum.substring(1, identityNum.length() - 1);
        } else if (identityType.equals(IdentityType.PASSPORT)) {
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

 

    /**
     * @return the selectIdentityTypes
     */
    public List<String> getSelectIdentityTypes() {
        return selectIdentityTypes;
    }

    /**
     * @param selectIdentityTypes the selectIdentityTypes to set
     */
    public void setSelectIdentityTypes(List<String> selectIdentityTypes) {
        this.selectIdentityTypes = selectIdentityTypes;
    }

    /**
     * @return the selectDepositAccountTypes
     */
    public List<String> getSelectDepositAccountTypes() {
        return selectDepositAccountTypes;
    }

    /**
     * @param selectDepositAccountTypes the selectDepositAccountTypes to set
     */
    public void setSelectDepositAccountTypes(List<String> selectDepositAccountTypes) {
        this.selectDepositAccountTypes = selectDepositAccountTypes;
    }

    /**
     * @return the selectNationalities
     */
    public List<String> getSelectNationalities() {
        return selectNationalities;
    }

    /**
     * @param selectNationalities the selectNationalities to set
     */
    public void setSelectNationalities(List<String> selectNationalities) {
        this.selectNationalities = selectNationalities;
    }

    /**
     * @return the selectGenders
     */
    public List<String> getSelectGenders() {
        return selectGenders;
    }

    /**
     * @param selectGenders the selectGenders to set
     */
    public void setSelectGenders(List<String> selectGenders) {
        this.selectGenders = selectGenders;
    }

    /**
     * @return the selectOccupations
     */
    public List<String> getSelectOccupations() {
        return selectOccupations;
    }

    /**
     * @param selectOccupations the selectOccupations to set
     */
    public void setSelectOccupations(List<String> selectOccupations) {
        this.selectOccupations = selectOccupations;
    }

    /**
     * @return the selectIncome
     */
    public List<String> getSelectIncome() {
        return selectIncome;
    }

    /**
     * @param selectIncome the selectIncome to set
     */
    public void setSelectIncome(List<String> selectIncome) {
        this.selectIncome = selectIncome;
    }

    /**
     * @return the initialDepositAccount
     */
    public String getInitialDepositAccount() {
        return initialDepositAccount;
    }

    /**
     * @param initialDepositAccount the initialDepositAccount to set
     */
    public void setInitialDepositAccount(String initialDepositAccount) {
        this.initialDepositAccount = initialDepositAccount;
    }

    /**
     * @return the emailSuccessFlag
     */
    public Boolean getEmailSuccessFlag() {
        return emailSuccessFlag;
    }

    /**
     * @param emailSuccessFlag the emailSuccessFlag to set
     */
    public void setEmailSuccessFlag(Boolean emailSuccessFlag) {
        this.emailSuccessFlag = emailSuccessFlag;
    }
}
