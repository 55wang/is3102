/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import entity.card.account.CreditCardAccount;
import entity.card.account.CreditCardOrder;
import entity.customer.Customer;
import entity.customer.MainAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import server.utilities.EnumUtils.*;
import server.utilities.CommonHelper;
import server.utilities.GenerateAccountAndCCNumber;

/**
 *
 * @author wang
 */
@Named(value = "cardApplicationManagedBean")
@ViewScoped
public class CardApplicationManagedBean implements Serializable {

    private final static String STAFF_NOTIFY_CHANNEL = "/staff_notify";
    private final static String CUSTOMER_NOTIFY_CHANNEL = "/customer_notify";
    private final static String ROLE_NOTIFY_CHANNEL = "/role_notification/";

    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;
    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private NewCustomerSessionBeanLocal newCustomerSessionBean;

    /**
     * Creates a new instance of CardApplicationManagedBean
     */
    public CardApplicationManagedBean() {
    }
    
    public String processChargeBack(String notification, String creditCard, String status) {
        //reverse transaction amount
        

        //send notification to specific customer based on their creditcard number
        
        
        //update chargeback status at the case management
        
        return null;
    }

    //get all applications
    public List<CreditCardOrder> displayCreditCardOrders() {
        return cardAcctSessionBean.showAllCreditCardOrder();
    }

    public String writeHardwareCard(String cardNumber) {

        return null;
    }

    //D.4.1.3
    //create card account
    public CreditCardAccount issueCreditCard(String creditCardOrder) {
        CreditCardOrder cco = cardAcctSessionBean.getCardOrderFromId(Long.parseLong(creditCardOrder));

        updateApplicationStatus(creditCardOrder, ApplicationStatus.APPROVED);
        //generate card number
        String cardNumber = GenerateAccountAndCCNumber.generateMasterCardNumber();

        //create card account;
        CreditCardAccount cca = new CreditCardAccount();
        cca.setCreditCardNum(cardNumber);
        cca.setCardStatus(CardAccountStatus.PENDING);
        Calendar cal = Calendar.getInstance();
        cca.setCreationDate(cal.getTime());

//        CreditCardProduct cp = null;
//        if (cco.getCreditType().getValue().equals("MILE")) {
//            cp = new MileCardProduct();
//        } else if (cco.getCreditType().getValue().equals("REWARD")) {
//            cp = new RewardCardProduct();
//        } else if (cco.getCreditType().getValue().equals("CASHBACK")) {
//            cp = new CashBackCardProduct();
//        }
//        cca.setCreditCardProduct(cp);
        cca.setCvv(Integer.parseInt(CommonHelper.generateRandom(true, 3)));
        cal.set(Calendar.YEAR, 2);
        cca.setValidDate(cal.getTime());

        //new main account and customer
        Customer c = new Customer();
        c.setAddress(cco.getAddress());
        c.setBirthDay(cco.getBirthDay());
        c.setCitizenship(cco.getCitizenship());
        c.setCreditScore(cco.getCreditScore());
        c.setEducation(cco.getEduLevel());
        c.setEmail(cco.getEmail());
        c.setFirstname(cco.getFirstName());
        c.setGender(cco.getGender());
        c.setIdentityNumber(cco.getIdentityNumber());
        c.setIdentityType(cco.getIdentityType());
        c.setIncome(cco.getIncome());
        c.setLastname(cco.getLastName());
        c.setMaritalStatus(cco.getMaritalStatus());
        c.setNationality(cco.getNationality());
        c.setOccupation(cco.getOccupation());
        c.setPhone(cco.getPhone());
        c.setPostalCode(cco.getPostalCode());

        MainAccount ma = new MainAccount();
        ma.setUserID("CUST" + CommonHelper.generateRandom(true, 7));
        ma.setPassword(CommonHelper.generateRandom(false, 8));
        ma.setStatus(StatusType.PENDING);
        ma.setCustomer(c);
        List al = new ArrayList<>();
        al.add(cca);
        ma.setCreditCardAccounts(al);
        c.setMainAccount(ma);
        newCustomerSessionBean.createCustomer(c);
        //email to the customer about new account
        emailServiceSessionBean.sendActivationGmailForCustomer(cco.getEmail(), ma.getPassword());

        //send a notification to staff to write card info to mifare card
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish(STAFF_NOTIFY_CHANNEL, "New Card Printing Order" + cca.getCreditCardNum());

        return cca;
    }

    //D.4.1.2 verify card application + additional info
    //editable
    public String needAdditionalInfo(String creditCardOrder, String msg) {
        try {

            CreditCardOrder cco = cardAcctSessionBean.getCardOrderFromId(Long.parseLong(creditCardOrder));
            cardAcctSessionBean.updateCardOrderStatus(cco, ApplicationStatus.PENDING);

            //email the customer
            String email = cco.getEmail();
            emailServiceSessionBean.sendRequireAdditionalInfo(email, msg);

        } catch (Exception ex) {
            System.out.println("CardApplicationManagedBean.needAdditionalInfo ERROR");
            return "FAIL";
        }
        return "SUCCESS";
    }

    //pending, reject,
    public String updateApplicationStatus(String creditCardOrder, ApplicationStatus status) {
        CreditCardOrder cco = cardAcctSessionBean.getCardOrderFromId(Long.parseLong(creditCardOrder));

        String result = cardAcctSessionBean.updateCardOrderStatus(cco, status);

        if (status.equals(ApplicationStatus.REJECT)) {
            //email the customer about the result
            String msg = "We are sorry that your application is not successful.";
            emailServiceSessionBean.sendRequireAdditionalInfo(cco.getEmail(), msg);
        }

        return result;
    }

    /* D.4.1.1
     staff input credit score from Credit Bureau
     which can be AA, BB, CC, DD, EE, FF, GG, HH, HX, HZ, GX, BX, CX
     if result creditscore > 200 = approve
     display both creditbureascore and own system calculated score.
     */
    public double calculateCreditLvl(CreditCardOrder cco, String creditBureauScore) {

        double creditScore = 0;
        //check annual gross income
        Income income = cco.getIncome();
        if (income.equals(Income.BELOW_2000)) {
            creditScore += 5;
        } else if (income.equals(Income.FROM_2000_TO_4000)) {
            creditScore += 10;
        } else if (income.equals(Income.FROM_4000_TO_6000)) {
            creditScore += 20;
        } else if (income.equals(Income.FROM_6000_TO_8000)) {
            creditScore += 40;
        } else if (income.equals(Income.FROM_8000_TO_10000)) {
            creditScore += 60;
        } else if (income.equals(Income.OVER_10000)) {
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

        MaritalStatus martialStatus = cco.getMaritalStatus();
        if (martialStatus.equals(MaritalStatus.DIVORCED)) {
            creditScore += 5;
        } else if (martialStatus.equals(MaritalStatus.MARRIED)) {
            creditScore += 10;
        } else if (martialStatus.equals(MaritalStatus.SINGLE)) {
            creditScore += 30;
        } else if (martialStatus.equals(MaritalStatus.OTHERS)) {
            creditScore += 0;
        }

        int dependents = cco.getNumOfDependents();
        if (dependents < 3) {
            creditScore += 25;
        } else if (dependents >= 3 && dependents < 5) {
            creditScore += 10;
        } else if (dependents >= 6) {
            creditScore += 0;
        }

        Education education = cco.getEduLevel();
        if (education.equals(Education.POSTGRAD)) {
            creditScore += 50;
        } else if (education.equals(Education.UNIVERSITY)) {
            creditScore += 40;
        } else if (education.equals(Education.DIPLOMA)) {
            creditScore += 30;
        } else if (education.equals(Education.A_LEVEL)) {
            creditScore += 25;
        } else if (education.equals(Education.TECHNICAL)) {
            creditScore += 10;
        } else if (education.equals(Education.SECONDARY)) {
            creditScore += 5;
        } else if (education.equals(Education.OTHERS)) {
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
    }

    public long getAge(Date dateOne, Date dateTwo) {
        long timeOne = dateOne.getTime();
        long timeTwo = dateTwo.getTime();
        long oneDay = 1000 * 60 * 60 * 24;
        long delta = (timeTwo - timeOne) / oneDay;

        delta = delta / 365;
        return delta;
    }
}
