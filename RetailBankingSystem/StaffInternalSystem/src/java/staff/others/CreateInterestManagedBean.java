/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.others;

import ejb.session.dams.AccountRuleSessionBeanLocal;
import entity.dams.rules.ConditionInterest;
import entity.dams.rules.Interest;
import entity.dams.rules.RangeInterest;
import entity.dams.rules.TimeRangeInterest;
import interceptor.audit.staff.Audit;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.EnumUtils.InterestConditionType;
import utils.EnumUtils.InterestType;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "createInterestManagedBean")
@ViewScoped
public class CreateInterestManagedBean implements Serializable {

    @EJB
    private AccountRuleSessionBeanLocal interestSessionBean;

    private String interestType;
    private Interest normalInterest = new Interest();
    private RangeInterest rangeInterest = new RangeInterest();
    private TimeRangeInterest timeRangeInterest = new TimeRangeInterest();
    private ConditionInterest conditionInterest = new ConditionInterest();
    private List<Interest> normalInterests = new ArrayList<>();
    private List<RangeInterest> rangeInterests = new ArrayList<>();
    private List<TimeRangeInterest> timeRangeInterests = new ArrayList<>();
    private List<ConditionInterest> conditionInterests = new ArrayList<>();
    private String INTEREST_TYPE_NORMAL = InterestType.NORMAL.toString();
    private String INTEREST_TYPE_RANGE = InterestType.RANGE.toString();
    private String INTEREST_TYPE_TIME_RANGE = InterestType.TIMERANGE.toString();
    private String INTEREST_TYPE_CONDITION = InterestType.CONDITION.toString();
    private String CONDITION_TYPE_BILL = InterestConditionType.BILL.toString();
    private String CONDITION_TYPE_CCSPENDING = InterestConditionType.CCSPENDING.toString();
    private String CONDITION_TYPE_SALARY = InterestConditionType.SALARY.toString();
    private String CONDITION_TYPE_INVEST = InterestConditionType.INVEST.toString();
    private String CONDITION_TYPE_INCREASE = InterestConditionType.INCREASE.toString();

    /**
     * Creates a new instance of CreateInterestManagedBean
     */
    public CreateInterestManagedBean() {
    }

    @PostConstruct
    public void init() {
        interestType = INTEREST_TYPE_NORMAL;
        List<Interest> interests = interestSessionBean.showAllInterests();
        for (Interest i : interests) {
            if (i instanceof TimeRangeInterest) {
                timeRangeInterests.add((TimeRangeInterest) i);
            } else if (i instanceof RangeInterest) {
                rangeInterests.add((RangeInterest) i);
            } else if (i instanceof ConditionInterest) {
                conditionInterests.add((ConditionInterest) i);
            } else {
                normalInterests.add(i);
            }
        }
    }

    @Audit(activtyLog = "Add Interest")
    public void addInterest(ActionEvent event) {
        if (interestType.equals(INTEREST_TYPE_NORMAL)) {
            if (interestSessionBean.addInterest(normalInterest)) {
                normalInterests.add(normalInterest);
                MessageUtils.displayInfo("Normal Interest Created");
            } else {
                MessageUtils.displayError("This Interest Exists");
            }
        } else if (interestType.equals(INTEREST_TYPE_RANGE)) {
            if (interestSessionBean.addInterest(rangeInterest)) {
                rangeInterests.add(rangeInterest);
                MessageUtils.displayInfo("Range Interest Created");
            } else {
                MessageUtils.displayError("This Interest Exists");
            }
        } else if (interestType.equals(INTEREST_TYPE_TIME_RANGE)) {
            if (interestSessionBean.addInterest(timeRangeInterest)) {
                timeRangeInterests.add(timeRangeInterest);
                MessageUtils.displayInfo("Time Range Interest Created");
            } else {
                MessageUtils.displayError("This Interest Exists");
            }
        } else if (interestType.equals(INTEREST_TYPE_CONDITION)) {
            if (interestSessionBean.addInterest(conditionInterest)) {
                conditionInterests.add(conditionInterest);
                MessageUtils.displayInfo("Condition Interest Created");
            } else {
                MessageUtils.displayError("This Interest Exists");
            }
        } else {
            MessageUtils.displayError("There's some error when creating interest");
        }
    }

    public void onInterestCellEdit(Interest i) {
        if (interestSessionBean.updateInterest(i)) {
            MessageUtils.displayInfo(i.getName() + " Changed!");
        } else {
            MessageUtils.displayError("There's some error when creating interest");
        }
    }

    /**
     * @return the interestType
     */
    public String getInterestType() {
        return interestType;
    }

    /**
     * @param interestType the interestType to set
     */
    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    /**
     * @return the normalInterest
     */
    public Interest getNormalInterest() {
        return normalInterest;
    }

    /**
     * @param normalInterest the normalInterest to set
     */
    public void setNormalInterest(Interest normalInterest) {
        this.normalInterest = normalInterest;
    }

    /**
     * @return the rangeInterest
     */
    public RangeInterest getRangeInterest() {
        return rangeInterest;
    }

    /**
     * @param rangeInterest the rangeInterest to set
     */
    public void setRangeInterest(RangeInterest rangeInterest) {
        this.rangeInterest = rangeInterest;
    }

    /**
     * @return the conditionInterest
     */
    public ConditionInterest getConditionInterest() {
        return conditionInterest;
    }

    /**
     * @param conditionInterest the conditionInterest to set
     */
    public void setConditionInterest(ConditionInterest conditionInterest) {
        this.conditionInterest = conditionInterest;
    }

    /**
     * @return the INTEREST_TYPE_NORMAL
     */
    public String getINTEREST_TYPE_NORMAL() {
        return INTEREST_TYPE_NORMAL;
    }

    /**
     * @param INTEREST_TYPE_NORMAL the INTEREST_TYPE_NORMAL to set
     */
    public void setINTEREST_TYPE_NORMAL(String INTEREST_TYPE_NORMAL) {
        this.INTEREST_TYPE_NORMAL = INTEREST_TYPE_NORMAL;
    }

    /**
     * @return the INTEREST_TYPE_RANGE
     */
    public String getINTEREST_TYPE_RANGE() {
        return INTEREST_TYPE_RANGE;
    }

    /**
     * @param INTEREST_TYPE_RANGE the INTEREST_TYPE_RANGE to set
     */
    public void setINTEREST_TYPE_RANGE(String INTEREST_TYPE_RANGE) {
        this.INTEREST_TYPE_RANGE = INTEREST_TYPE_RANGE;
    }

    /**
     * @return the INTEREST_TYPE_CONDITION
     */
    public String getINTEREST_TYPE_CONDITION() {
        return INTEREST_TYPE_CONDITION;
    }

    /**
     * @param INTEREST_TYPE_CONDITION the INTEREST_TYPE_CONDITION to set
     */
    public void setINTEREST_TYPE_CONDITION(String INTEREST_TYPE_CONDITION) {
        this.INTEREST_TYPE_CONDITION = INTEREST_TYPE_CONDITION;
    }

    /**
     * @return the CONDITION_TYPE_BILL
     */
    public String getCONDITION_TYPE_BILL() {
        return CONDITION_TYPE_BILL;
    }

    /**
     * @param CONDITION_TYPE_BILL the CONDITION_TYPE_BILL to set
     */
    public void setCONDITION_TYPE_BILL(String CONDITION_TYPE_BILL) {
        this.CONDITION_TYPE_BILL = CONDITION_TYPE_BILL;
    }

    /**
     * @return the CONDITION_TYPE_CCSPENDING
     */
    public String getCONDITION_TYPE_CCSPENDING() {
        return CONDITION_TYPE_CCSPENDING;
    }

    /**
     * @param CONDITION_TYPE_CCSPENDING the CONDITION_TYPE_CCSPENDING to set
     */
    public void setCONDITION_TYPE_CCSPENDING(String CONDITION_TYPE_CCSPENDING) {
        this.CONDITION_TYPE_CCSPENDING = CONDITION_TYPE_CCSPENDING;
    }

    /**
     * @return the CONDITION_TYPE_SALARY
     */
    public String getCONDITION_TYPE_SALARY() {
        return CONDITION_TYPE_SALARY;
    }

    /**
     * @param CONDITION_TYPE_SALARY the CONDITION_TYPE_SALARY to set
     */
    public void setCONDITION_TYPE_SALARY(String CONDITION_TYPE_SALARY) {
        this.CONDITION_TYPE_SALARY = CONDITION_TYPE_SALARY;
    }

    /**
     * @return the CONDITION_TYPE_INVEST
     */
    public String getCONDITION_TYPE_INVEST() {
        return CONDITION_TYPE_INVEST;
    }

    /**
     * @param CONDITION_TYPE_INVEST the CONDITION_TYPE_INVEST to set
     */
    public void setCONDITION_TYPE_INVEST(String CONDITION_TYPE_INVEST) {
        this.CONDITION_TYPE_INVEST = CONDITION_TYPE_INVEST;
    }

    /**
     * @return the normalInterests
     */
    public List<Interest> getNormalInterests() {
        return normalInterests;
    }

    /**
     * @param normalInterests the normalInterests to set
     */
    public void setNormalInterests(List<Interest> normalInterests) {
        this.normalInterests = normalInterests;
    }

    /**
     * @return the rangeInterests
     */
    public List<RangeInterest> getRangeInterests() {
        return rangeInterests;
    }

    /**
     * @param rangeInterests the rangeInterests to set
     */
    public void setRangeInterests(List<RangeInterest> rangeInterests) {
        this.rangeInterests = rangeInterests;
    }

    /**
     * @return the conditionInterests
     */
    public List<ConditionInterest> getConditionInterests() {
        return conditionInterests;
    }

    /**
     * @param conditionInterests the conditionInterests to set
     */
    public void setConditionInterests(List<ConditionInterest> conditionInterests) {
        this.conditionInterests = conditionInterests;
    }

    /**
     * @return the INTEREST_TYPE_TIME_RANGE
     */
    public String getINTEREST_TYPE_TIME_RANGE() {
        return INTEREST_TYPE_TIME_RANGE;
    }

    /**
     * @param INTEREST_TYPE_TIME_RANGE the INTEREST_TYPE_TIME_RANGE to set
     */
    public void setINTEREST_TYPE_TIME_RANGE(String INTEREST_TYPE_TIME_RANGE) {
        this.INTEREST_TYPE_TIME_RANGE = INTEREST_TYPE_TIME_RANGE;
    }

    /**
     * @return the timeRangeInterests
     */
    public List<TimeRangeInterest> getTimeRangeInterests() {
        return timeRangeInterests;
    }

    /**
     * @param timeRangeInterests the timeRangeInterests to set
     */
    public void setTimeRangeInterests(List<TimeRangeInterest> timeRangeInterests) {
        this.timeRangeInterests = timeRangeInterests;
    }

    /**
     * @return the timeRangeInterest
     */
    public TimeRangeInterest getTimeRangeInterest() {
        return timeRangeInterest;
    }

    /**
     * @param timeRangeInterest the timeRangeInterest to set
     */
    public void setTimeRangeInterest(TimeRangeInterest timeRangeInterest) {
        this.timeRangeInterest = timeRangeInterest;
    }

    /**
     * @return the CONDITION_TYPE_INCREASE
     */
    public String getCONDITION_TYPE_INCREASE() {
        return CONDITION_TYPE_INCREASE;
    }

    /**
     * @param CONDITION_TYPE_INCREASE the CONDITION_TYPE_INCREASE to set
     */
    public void setCONDITION_TYPE_INCREASE(String CONDITION_TYPE_INCREASE) {
        this.CONDITION_TYPE_INCREASE = CONDITION_TYPE_INCREASE;
    }
}
