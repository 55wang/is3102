/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import entity.BankAccount;
import entity.CurrentAccount;
import entity.Customer;
import entity.MainAccount;
import entity.MainAccount.StatusType;
import entity.SavingAccount;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.FlowEvent;
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
    private MainAccount mainAccount = new MainAccount();
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

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    public void save() {
        Boolean emailSuccessFlag = true;
        
        mainAccount.setStatus(StatusType.PENDING);
        
        mainAccount.setUserID(generateUserID(customer.getIdentityType(), customer.getIdentityNumber()));
        String randomPwd = generatePwd();
        mainAccount.setPassword(randomPwd);

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

        try{
            emailServiceSessionBean.sendActivationGmailForNewCustomer(customer.getEmail(), randomPwd);
        }catch(Exception ex){
            emailSuccessFlag = false;
        }
        
        if(emailSuccessFlag){
            newCustomerSessionBean.createCustomer(customer, mainAccount);
            RedirectUtils.redirect("../common/register_successful.xhtml");
        }
        else{
            FacesMessage msg = new FacesMessage("Fail!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        

//        emailServiceSessionBean.sendActivationEmailForNewCustomer(customer.getEmail());
        
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }
    
    public String generateUserID(String identityType, String identityNum){
        if(identityType.equals("Singaporean/PR NRIC")){
            return "c"+identityNum.substring(1, identityNum.length()-1);
        }
        else if(identityType.equals("Passport")){
            return "c"+identityNum.substring(1);
        }
        else 
            return "error";
    }
    
    public String generatePwd(){
        int pwdLen = 10;
        SecureRandom rnd = new SecureRandom();

        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(pwdLen);
        for( int i = 0; i < pwdLen; i++ ) 
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
    
    public String hashPwd(String pwd){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pwd.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
             sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        }
        catch(NoSuchAlgorithmException ex){
            return pwd;
        }
    }
}
