/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.card.NewCardProductSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.card.account.CreditCardOrder;
import entity.common.AuditLog;
import entity.customer.Customer;
import entity.customer.MainAccount;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import utils.MessageUtils;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author wang
 */
@Named(value = "cardViewCreditApplicationManagedBean")
@ViewScoped
public class CardViewCreditApplicationManagedBean implements Serializable {
    
    @EJB
    NewCardProductSessionBeanLocal newCardProductSessionBean;
    @EJB
    CardAcctSessionBeanLocal cardAcctSessionBean;
    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private NewCustomerSessionBeanLocal newCustomerSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    
    private List<CreditCardOrder> ccos;
    private String bureauCreditScore;
    
    public CardViewCreditApplicationManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter view_credit_card.xhtml");
        a.setFunctionName("CardViewCreditApplicationManagedBean @PostConstruct init()");
        a.setInput("Getting all credit card applications");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }
    public void displayCardOrder() {
//        mcps = newCardProductSessionBean.showAllMileProducts();
//        rcps = newCardProductSessionBean.showAllRewardProducts();
//        cbcps = newCardProductSessionBean.showAllCashBackCardProducts();
        ccos = cardAcctSessionBean.showAllCreditCardOrder();
    }
    
    public void approveOrder(CreditCardOrder cco) {
        cardAcctSessionBean.updateCardOrderStatus(cco, EnumUtils.ApplicationStatus.APPROVED);
        // Create Main Account 
        // Create Customer
        // 
        System.out.println("Creating New Customer");
        Customer customer = new Customer(cco);
        System.out.println("Customer created from cco");
        MainAccount mainAccount = new MainAccount();
        mainAccount.setStatus(EnumUtils.StatusType.PENDING);
        mainAccount.setUserID(generateUserID(customer.getIdentityType(), customer.getIdentityNumber()));
        String randomPwd = generatePwd();
        mainAccount.setPassword(randomPwd);
        customer.setMainAccount(mainAccount);
        newCustomerSessionBean.createCustomer(customer);
        System.out.println("Customer Saved");
        try {
            emailServiceSessionBean.sendCreditCardActivationGmailForCustomer(customer.getEmail(), randomPwd);
            MessageUtils.displayInfo("Order Approved!");
        } catch (Exception ex) {
            MessageUtils.displayError("Order Approved! But email send failed");
        }
    }
    
    public void rejectOrder(CreditCardOrder cco) {
        cardAcctSessionBean.updateCardOrderStatus(cco, EnumUtils.ApplicationStatus.REJECT);
        MessageUtils.displayInfo("Order Rejected!");
    }
    
    public void calculateCreditScore(CreditCardOrder cco) {
        try {
            cco.setBureaCreditScore(bureauCreditScore);
            double creditScore = calculateCreditLvl(cco, bureauCreditScore);
            cco.setCreditScore(creditScore);
            cardAcctSessionBean.updateCreditCardOrder(cco);
            MessageUtils.displayInfo("Credit Score Updated!");
        } catch (Exception ex) {
            System.out.println("error");
            MessageUtils.displayError("Error! Not updated!");
        }
        
    }

    /* D.4.1.1
     staff input credit score from Credit Bureau
     which can be AA, BB, CC, DD, EE, FF, GG, HH, HX, HZ, GX, BX, CX
     if result creditscore > 200 = approve
     display both creditbureascore and own system calculated score.
     */
    public double calculateCreditLvl(CreditCardOrder cco, String creditBureauScore) {
        try {
            
            System.out.println("inside calculateCreditLvl");
            System.out.println(creditBureauScore);
            System.out.println(cco.getApplicationStatus());
            double creditScore = 0;
            //check annual gross income
            EnumUtils.Income income = cco.getIncome();
            if (income.equals(EnumUtils.Income.BELOW_2000)) {
                creditScore += 5;
            } else if (income.equals(EnumUtils.Income.FROM_2000_TO_4000)) {
                creditScore += 10;
            } else if (income.equals(EnumUtils.Income.FROM_4000_TO_6000)) {
                creditScore += 20;
            } else if (income.equals(EnumUtils.Income.FROM_6000_TO_8000)) {
                creditScore += 40;
            } else if (income.equals(EnumUtils.Income.FROM_8000_TO_10000)) {
                creditScore += 60;
            } else if (income.equals(EnumUtils.Income.OVER_10000)) {
                creditScore += 80;
            }

            //check annual gross income
            Date birthday = cco.getBirthDay();
            Date now = new Date();
            Long age = getAge(birthday, now);
            if (age < 50) {
                creditScore += 25;
            } else if (age >= 50 && age < 60) {
                creditScore += 10;
            } else if (age >= 60) {
                creditScore += 0;
            }
//
//            EnumUtils.MaritalStatus martialStatus = cco.getMaritalStatus();
//            if (martialStatus.equals(EnumUtils.MaritalStatus.DIVORCED)) {
//                creditScore += 5;
//            } else if (martialStatus.equals(EnumUtils.MaritalStatus.MARRIED)) {
//                creditScore += 10;
//            } else if (martialStatus.equals(EnumUtils.MaritalStatus.SINGLE)) {
//                creditScore += 30;
//            } else if (martialStatus.equals(EnumUtils.MaritalStatus.OTHERS)) {
//                creditScore += 0;
//            }

            int dependents = cco.getNumOfDependents();
            if (dependents < 3) {
                creditScore += 25;
            } else if (dependents >= 3 && dependents < 5) {
                creditScore += 10;
            } else if (dependents >= 6) {
                creditScore += 0;
            }
            
            EnumUtils.Education education = cco.getEduLevel();
            if (education.equals(EnumUtils.Education.POSTGRAD)) {
                creditScore += 50;
            } else if (education.equals(EnumUtils.Education.UNIVERSITY)) {
                creditScore += 40;
            } else if (education.equals(EnumUtils.Education.DIPLOMA)) {
                creditScore += 30;
            } else if (education.equals(EnumUtils.Education.A_LEVEL)) {
                creditScore += 25;
            } else if (education.equals(EnumUtils.Education.TECHNICAL)) {
                creditScore += 10;
            } else if (education.equals(EnumUtils.Education.SECONDARY)) {
                creditScore += 5;
            } else if (education.equals(EnumUtils.Education.OTHERS)) {
                creditScore += 0;
            }
//        AA, BB, CC, DD, EE, FF, GG, HH, HX, HZ, GX, BX, CX
            if (creditBureauScore.equals("AA")) {
                creditScore += 100;
            } else if (creditBureauScore.equals("BB")) {
                creditScore += 90;
            } else if (creditBureauScore.equals("CC")) {
                creditScore += 80;
            } else if (creditBureauScore.equals("DD")) {
                creditScore += 70;
            } else if (creditBureauScore.equals("EE")) {
                creditScore += 50;
            } else if (creditBureauScore.equals("FF")) {
                creditScore += 30;
            } else if (creditBureauScore.equals("GG")) {
                creditScore += 20;
            } else if (creditBureauScore.equals("HH")) {
                creditScore += 10;
            } else {
                creditScore += 0;
            }
            return creditScore;
        } catch (Exception ex) {
            System.out.println("error in credit score");
        }
        return 0;
        
    }
    
    public long getAge(Date dateOne, Date dateTwo) {
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;
        
        delta = delta / 365;
        return delta;
    }
    
    public String generateUserID(EnumUtils.IdentityType identityType, String identityNum) {

        if (identityType.equals(EnumUtils.IdentityType.NRIC)) {
            return "c" + identityNum.substring(1, identityNum.length() - 1);
        } else if (identityType.equals(EnumUtils.IdentityType.PASSPORT)) {
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
    
    public List<CreditCardOrder> getCcos() {
        return ccos;
    }
    
    public void setCcos(List<CreditCardOrder> ccos) {
        this.ccos = ccos;
    }
    
    public String getBureauCreditScore() {
        return bureauCreditScore;
    }
    
    public void setBureauCreditScore(String bureauCreditScore) {
        this.bureauCreditScore = bureauCreditScore;
    }
    
}
