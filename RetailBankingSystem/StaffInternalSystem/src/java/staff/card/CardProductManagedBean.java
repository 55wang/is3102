/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.card.NewCardProductSessionBeanLocal;
import entity.card.account.CashBackCardProduct;
import entity.card.account.CreditCardOrder;
import entity.card.account.MileCardProduct;
import entity.card.account.RewardCardProduct;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author wang
 */
@Named(value = "cardProductManagedBean")
@ViewScoped
public class CardProductManagedBean implements Serializable {

    @EJB
    private NewCardProductSessionBeanLocal newProductSessionBean;

    /**
     * Creates a new instance of CardProductManagedBean
     */
    public CardProductManagedBean() {
    }

    //staff input credit score from Credit Bureau
    //which can be AA, BB, CC, DD, EE, FF, GG, HH, HX, HZ, GX, BX, CX
    public double calculateCreditLvl(CreditCardOrder cco, String creditBureauScore) {

        double creditScore = 0;
        //check annual gross income
        double income = cco.getIncome();
        if (income < 10000) {
            creditScore += 5;
        } else if (income >= 10000 && income < 20000) {
            creditScore += 15;
        } else if (income >= 20000 && income < 40000) {
            creditScore += 30;
        } else if (income >= 40000 && income < 60000) {
            creditScore += 45;
        } else if (income > 60000) {
            creditScore += 60;
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

        String martialStatus = cco.getMartialStatus();
        if (martialStatus.equals("SINGLE")) {
            creditScore += 25;
        } else if (martialStatus.equals("MARRIED")) {
            creditScore += 10;
        }

        int dependents = cco.getNumOfDependents();
        if (dependents < 3) {
            creditScore += 25;
        } else if (dependents >= 3 && dependents < 5) {
            creditScore += 10;
        } else if (dependents >= 6) {
            creditScore += 0;
        }

        String eduLevel = cco.getEduLevel();
        if (eduLevel.equals("UNIVERSITY_GRAD")) {
            creditScore += 30;
        } else if (eduLevel.equals("DIPLOMA_HOLDER")) {
            creditScore += 15;
        } else if (eduLevel.equals("TECHNICAL")) {
            creditScore += 5;
        } else if (eduLevel.equals("A_LEVEL")) {
            creditScore += 5;
        } else if (eduLevel.equals("SECONDARY")) {
            creditScore += 0;
        } else if (eduLevel.equals("PRIMARY")) {
            creditScore += 0;
        } else if (eduLevel.equals("OTHERS")) {
            creditScore += 0;
        }
        return creditScore;
    }

    public String addNewMileCreditCard(String productName, double minSpendingAmount,
            boolean minSpending, double overseaMileRate, double localMileRate) {
        try {
            MileCardProduct mcp = new MileCardProduct();
            mcp.setProductName(productName);
            mcp.setMinSpendingAmount(minSpendingAmount);
            mcp.setMinSpending(minSpending);
            mcp.setOverseaMileRate(overseaMileRate);
            mcp.setLocalMileRate(localMileRate);

            newProductSessionBean.createMileProduct(mcp);

            return "SUCCESS"; //set the return to webpage path
        } catch (Exception ex) {
            System.out.println("CardProductManagedBean.addNewMileCreditCard Error");
            System.out.println(ex);
            return "FAIL";
        }
    }

    public String addNewRewardCreditCard(String productName, double minSpendingAmount,
            boolean minSpending, double localMileRate, double localPointRate) {
        try {
            RewardCardProduct rcp = new RewardCardProduct();
            rcp.setProductName(productName);
            rcp.setMinSpendingAmount(minSpendingAmount);
            rcp.setMinSpending(minSpending);
            rcp.setLocalMileRate(localMileRate);
            rcp.setLocalPointRate(localPointRate);

            newProductSessionBean.createRewardProduct(rcp);

            return "SUCCESS"; //set the return to webpage path
        } catch (Exception ex) {
            System.out.println("CardProductManagedBean.addNewRewardCreditCard Error");
            System.out.println(ex);
            return "FAIL";
        }
    }

    public String addNewCashBackCreditCard(String productName, double minSpendingAmount,
            boolean minSpending, double petrolCashBackRate, double groceryCashBackRate,
            double diningCashBackRate) {
        try {
            CashBackCardProduct cbcp = new CashBackCardProduct();
            cbcp.setProductName(productName);
            cbcp.setMinSpendingAmount(minSpendingAmount);
            cbcp.setMinSpending(minSpending);
            cbcp.setPetrolCashBackRate(petrolCashBackRate);
            cbcp.setGroceryCashBackRate(groceryCashBackRate);
            cbcp.setDiningCashBackRate(diningCashBackRate);

            newProductSessionBean.createCashBackProduct(cbcp);

            return "SUCCESS"; //set the return to webpage path
        } catch (Exception ex) {
            System.out.println("CardProductManagedBean.addNewCashBackCreditCard Error");
            System.out.println(ex);
            return "FAIL";
        }
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
