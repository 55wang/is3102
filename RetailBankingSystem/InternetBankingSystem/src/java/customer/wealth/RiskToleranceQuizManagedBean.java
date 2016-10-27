/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.wealth;

import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import ejb.session.wealth.WealthManegementSubscriberSessionBeanLocal;
import entity.customer.MainAccount;
import entity.customer.WealthManagementSubscriber;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "riskToleranceQuizManagedBean")
@ViewScoped
public class RiskToleranceQuizManagedBean implements Serializable {
    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;
    @EJB
    private WealthManegementSubscriberSessionBeanLocal wealthManegementSubscriberSessionBean;
    
    
    private WealthManagementSubscriber wms;
    private MainAccount mainAccount;
    private Boolean retakeOrNot = false;
    private int questionNumber = 1;
    private List<Integer> riskScores = new ArrayList<Integer>();
    private int selectValue = 0;
    private Double savingAmount;
    /**
     * Creates a new instance of RiskToleranceQuizManagedBean
     */
    public RiskToleranceQuizManagedBean() {
    }
    
    // Followed by @PostConstruct
    @PostConstruct
    public void init() {
        setMainAccount(mainAccountSessionBean.getMainAccountByUserId(SessionUtils.getUserName()));
        setWms(mainAccount.getWealthManagementSubscriber());
    }
    
    public void retakeQuiz(){
        setRetakeOrNot(true);   
    }
    
    public void back(){
        questionNumber--;
        riskScores.remove(riskScores.size()-1);
        selectValue = 0;
    }
    
    public void next(){
        questionNumber++;
        riskScores.add(selectValue);
        selectValue = 0;
    }
    
    public void submit(){
        System.out.println("submit button pressed: ");
        System.out.println(savingAmount);
        wms.getMainAccount().getCustomer().setSavingPerMonth(savingAmount);
        wms.setRiskToleranceScore(totalScore());
        if(totalScore() < 18)
            wms.setRiskToleranceLevel(EnumUtils.RiskToleranceLevel.LOW_RISK_TOLERANCE);
        else if(totalScore() < 22)
            wms.setRiskToleranceLevel(EnumUtils.RiskToleranceLevel.BELOW_AVERAGE_RISK_TOLERANCE);
        else if(totalScore() < 28)
            wms.setRiskToleranceLevel(EnumUtils.RiskToleranceLevel.AVERAGE_RISK_TOLERANCE);
        else if(totalScore() < 32)
            wms.setRiskToleranceLevel(EnumUtils.RiskToleranceLevel.ABOVE_AVERAGE_RISK_TOLERANCE);
        else
            wms.setRiskToleranceLevel(EnumUtils.RiskToleranceLevel.HIGH_RISK_ROLERANCE);
        
        setWms(wealthManegementSubscriberSessionBean.updateWealthManagementSubscriber(wms));
        
        setRetakeOrNot(false);
    }   
    
    private Integer totalScore(){
        System.out.println("riskscore size: "+riskScores.size());
        int total = 0;
        for(int i = 0; i < riskScores.size(); i++)
            total+=riskScores.get(i);
        return total;
    }
    
    public WealthManagementSubscriber getWms() {
        return wms;
    }

    public void setWms(WealthManagementSubscriber wms) {
        this.wms = wms;
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    public Boolean getRetakeOrNot() {
        return retakeOrNot;
    }

    public void setRetakeOrNot(Boolean retakeOrNot) {
        this.retakeOrNot = retakeOrNot;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public int getSelectValue() {
        return selectValue;
    }

    public void setSelectValue(int selectValue) {
        this.selectValue = selectValue;
    }

    public Double getSavingAmount() {
        return savingAmount;
    }

    public void setSavingAmount(Double savingAmount) {
        this.savingAmount = savingAmount;
    }
}
