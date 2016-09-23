/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.CardAcctSessionBeanLocal;
import entity.card.account.CreditCardOrder;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils.*;
import utils.generateCardNumber;

/**
 *
 * @author wang
 */
@Named(value = "cardApplicationManagedBean")
@ViewScoped
public class CardApplicationManagedBean implements Serializable{

    @EJB
    private CardAcctSessionBeanLocal cardAcctSessionBean;

    /**
     * Creates a new instance of CardApplicationManagedBean
     */
    public CardApplicationManagedBean() {
    }
    
    //D.4.1.3
    //approve, create card account and issue, 
    public String issueCreditCard(String creditCardOrder) {
        String result = updateApplicationStatus(creditCardOrder, ApplicationStatus.APPROVED);
        //generate card number
        String cardNumber = generateCardNumber.generateMasterCardNumber();

        //create card account;
        //hardware writing card number into the mifare card.
        return null;
    }
    
    //D.4.1.2 verify card application + additional info
    //editable
    public String needAdditionalInfo(String creditCardOrder, String msg) {
      CreditCardOrder cco = cardAcctSessionBean.getCardOrderFromId(Long.parseLong(creditCardOrder));
      String result = cardAcctSessionBean.updateCardOrderStatus(cco, ApplicationStatus.EDITABLE);
      
      
      //notify customer. push msg here
      return null;
    }

    //pending, reject, cancel,
    public String updateApplicationStatus(String creditCardOrder, ApplicationStatus status) {
        CreditCardOrder cco = cardAcctSessionBean.getCardOrderFromId(Long.parseLong(creditCardOrder));

        String result = cardAcctSessionBean.updateCardOrderStatus(cco, status);

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
