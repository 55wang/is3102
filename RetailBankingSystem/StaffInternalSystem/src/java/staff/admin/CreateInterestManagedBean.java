/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.admin;

import ejb.session.dams.InterestSessionBeanLocal;
import entity.dams.rules.ConditionInterest;
import entity.dams.rules.Interest;
import entity.dams.rules.RangeInterest;
import entity.dams.rules.TimeRangeInterest;
import entity.dto.TimeRangeInterestCollectionDTO;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils.InterestConditionType;
import server.utilities.EnumUtils.InterestType;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "createInterestManagedBean")
@ViewScoped
public class CreateInterestManagedBean implements Serializable {
    
    @EJB
    private InterestSessionBeanLocal interestSessionBean;
    
    private List<TimeRangeInterestCollectionDTO> collectionDTOs = new ArrayList<>();
    private List<Integer> colIndex = new ArrayList<>();
    private List<Integer> rowIndex = new ArrayList<>();
    private TimeRangeInterest[][] formatedInterests;
    private Integer colSize;
    
    private String interestType;
    private Interest normalInterest = new Interest();
    private RangeInterest rangeInterest = new RangeInterest();
    private String timeRangeInterestName;
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
    private String CONDITION_TYPE_NOWITHDRAW = InterestConditionType.NOWITHDRAW.toString();

    /**
     * Creates a new instance of CreateInterestManagedBean
     */
    public CreateInterestManagedBean() {
        
    }

    @PostConstruct
    public void init() {
        initCollection();
        interestType = INTEREST_TYPE_NORMAL;
        List<Interest> interests = interestSessionBean.showAllInterests();
        for (Interest i : interests) {
            if (i instanceof TimeRangeInterest) {
            } else if (i instanceof RangeInterest) {
                rangeInterests.add((RangeInterest) i);
            } else if (i instanceof ConditionInterest) {
                conditionInterests.add((ConditionInterest) i);
            } else {
                normalInterests.add(i);
            }
        }
        timeRangeInterests = interestSessionBean.getFixedDepositAccountDefaultInterests();
        if (!timeRangeInterests.isEmpty()) {
            initTimeRangeDisplay();
        }
    }
    
    private void initTimeRangeDisplay() {
        Set<Integer> set = new HashSet<>();
        for (TimeRangeInterest i : timeRangeInterests) {
            System.out.println("ID: " + i.getId() + " StartMonth: " + i.getStartMonth());
            set.add(i.getStartMonth());
        }
//        System.out.println("Before sort");
        Collections.sort(timeRangeInterests);
//        System.out.println("Set is size: " + set.size());
        Integer col = set.size();
        Integer row = timeRangeInterests.size() / col;
//        System.out.println("Col: " + col + " Row: " + row);
        formatedInterests = new TimeRangeInterest[row][col];
        
        Integer counter = 0;
        for (int i = 0; i < formatedInterests.length; i++) {
            for (int j = 0; j < formatedInterests[i].length; j++) {
                formatedInterests[i][j] = timeRangeInterests.get(counter);
                counter++;
            }
        }
        
        System.out.println(formatedInterests);
        
        for (int i = 0; i <= col; i++) {
            getColIndex().add(i);
        }
//        getColIndex().add(getColIndex().size());
        for (int i = 0; i <= row; i++) {
            getRowIndex().add(i);
        }
        setColSize((Integer) getColIndex().size());
    }

    private void initCollection() {
        TimeRangeInterestCollectionDTO i1 = new TimeRangeInterestCollectionDTO();
        i1.setMinAmount(new BigDecimal(5000));
        i1.setMaxAmount(new BigDecimal(20000));
        i1.setAmount1(new BigDecimal(0.0005));
        i1.setAmount2(new BigDecimal(0.001));
        i1.setAmount3(new BigDecimal(0.0015));
        i1.setAmount4(new BigDecimal(0.002));
        i1.setAmount5(new BigDecimal(0.0025));
        i1.setAmount6(new BigDecimal(0.005));
        i1.setAmount7(new BigDecimal(0.0055));
        i1.setAmount8(new BigDecimal(0.0065));
        getCollectionDTOs().add(i1);
        
        TimeRangeInterestCollectionDTO i2 = new TimeRangeInterestCollectionDTO();
        i2.setMinAmount(new BigDecimal(20000));
        i2.setMaxAmount(new BigDecimal(50000));
        i2.setAmount1(new BigDecimal(0.0005));
        i2.setAmount2(new BigDecimal(0.001));
        i2.setAmount3(new BigDecimal(0.0015));
        i2.setAmount4(new BigDecimal(0.002));
        i2.setAmount5(new BigDecimal(0.0025));
        i2.setAmount6(new BigDecimal(0.005));
        i2.setAmount7(new BigDecimal(0.0055));
        i2.setAmount8(new BigDecimal(0.0065));
        getCollectionDTOs().add(i2);
        
        TimeRangeInterestCollectionDTO i3 = new TimeRangeInterestCollectionDTO();
        i3.setMinAmount(new BigDecimal(50000));
        i3.setMaxAmount(new BigDecimal(99999));
        i3.setAmount1(new BigDecimal(0.0005));
        i3.setAmount2(new BigDecimal(0.001));
        i3.setAmount3(new BigDecimal(0.0015));
        i3.setAmount4(new BigDecimal(0.002));
        i3.setAmount5(new BigDecimal(0.0025));
        i3.setAmount6(new BigDecimal(0.005));
        i3.setAmount7(new BigDecimal(0.0055));
        i3.setAmount8(new BigDecimal(0.0065));
        getCollectionDTOs().add(i3);
        
        TimeRangeInterestCollectionDTO i4 = new TimeRangeInterestCollectionDTO();
        i4.setMinAmount(new BigDecimal(100000));
        i4.setMaxAmount(new BigDecimal(249999));
        i4.setAmount1(new BigDecimal(0.0005));
        i4.setAmount2(new BigDecimal(0.001));
        i4.setAmount3(new BigDecimal(0.0015));
        i4.setAmount4(new BigDecimal(0.002));
        i4.setAmount5(new BigDecimal(0.0025));
        i4.setAmount6(new BigDecimal(0.005));
        i4.setAmount7(new BigDecimal(0.0055));
        i4.setAmount8(new BigDecimal(0.0065));
        getCollectionDTOs().add(i4);
        
        TimeRangeInterestCollectionDTO i5 = new TimeRangeInterestCollectionDTO();
        i5.setMinAmount(new BigDecimal(250000));
        i5.setMaxAmount(new BigDecimal(499999));
        i5.setAmount1(new BigDecimal(0.0005));
        i5.setAmount2(new BigDecimal(0.001));
        i5.setAmount3(new BigDecimal(0.0015));
        i5.setAmount4(new BigDecimal(0.002));
        i5.setAmount5(new BigDecimal(0.0025));
        i5.setAmount6(new BigDecimal(0.005));
        i5.setAmount7(new BigDecimal(0.0055));
        i5.setAmount8(new BigDecimal(0.0065));
        getCollectionDTOs().add(i5);
        
        TimeRangeInterestCollectionDTO i6 = new TimeRangeInterestCollectionDTO();
        i6.setMinAmount(new BigDecimal(500000));
        i6.setMaxAmount(new BigDecimal(999999));
        i6.setAmount1(new BigDecimal(0.0005));
        i6.setAmount2(new BigDecimal(0.001));
        i6.setAmount3(new BigDecimal(0.0015));
        i6.setAmount4(new BigDecimal(0.002));
        i6.setAmount5(new BigDecimal(0.0025));
        i6.setAmount6(new BigDecimal(0.005));
        i6.setAmount7(new BigDecimal(0.0055));
        i6.setAmount8(new BigDecimal(0.0065));
        getCollectionDTOs().add(i6);
    }
//    @Audit(activtyLog = "Add Interest")
    public void addInterest(ActionEvent event) {
        if (interestType.equals(INTEREST_TYPE_NORMAL)) {
            if (interestSessionBean.addInterest(normalInterest) != null) {
                normalInterests.add(normalInterest);
                MessageUtils.displayInfo("Normal Interest Created");
            } else {
                MessageUtils.displayError("This Interest Exists");
            }
        } else if (interestType.equals(INTEREST_TYPE_RANGE)) {
            if (interestSessionBean.addInterest(rangeInterest) != null) {
                rangeInterests.add(rangeInterest);
                MessageUtils.displayInfo("Range Interest Created");
            } else {
                MessageUtils.displayError("This Interest Exists");
            }
        } else if (interestType.equals(INTEREST_TYPE_TIME_RANGE)) {
            List<TimeRangeInterest> tobeSaved = new ArrayList<>();
            for (TimeRangeInterestCollectionDTO i : getCollectionDTOs()) {
                TimeRangeInterest i1 = new TimeRangeInterest();
                i1.setName(timeRangeInterestName + i.getMinAmount() + i.getMaxAmount() + "1-2");
                i1.setMinimum(i.getMinAmount());
                i1.setMaximum(i.getMaxAmount());
                i1.setStartMonth(1);
                i1.setEndMonth(2);
                i1.setPercentage(i.getAmount1());
                tobeSaved.add(i1);
                
                TimeRangeInterest i2 = new TimeRangeInterest();
                i2.setName(timeRangeInterestName + i.getMinAmount() + i.getMaxAmount() + "3-5");
                i2.setMinimum(i.getMinAmount());
                i2.setMaximum(i.getMaxAmount());
                i2.setStartMonth(3);
                i2.setEndMonth(5);
                i2.setPercentage(i.getAmount2());
                tobeSaved.add(i2);
                
                TimeRangeInterest i3 = new TimeRangeInterest();
                i3.setName(timeRangeInterestName + i.getMinAmount() + i.getMaxAmount() + "6-8");
                i3.setMinimum(i.getMinAmount());
                i3.setMaximum(i.getMaxAmount());
                i3.setStartMonth(6);
                i3.setEndMonth(8);
                i3.setPercentage(i.getAmount3());
                tobeSaved.add(i3);
                
                TimeRangeInterest i4 = new TimeRangeInterest();
                i4.setName(timeRangeInterestName + i.getMinAmount() + i.getMaxAmount() + "9-11");
                i4.setMinimum(i.getMinAmount());
                i4.setMaximum(i.getMaxAmount());
                i4.setStartMonth(9);
                i4.setEndMonth(11);
                i4.setPercentage(i.getAmount4());
                tobeSaved.add(i4);
                
                TimeRangeInterest i5 = new TimeRangeInterest();
                i5.setName(timeRangeInterestName + i.getMinAmount() + i.getMaxAmount() + "12-15");
                i5.setMinimum(i.getMinAmount());
                i5.setMaximum(i.getMaxAmount());
                i5.setStartMonth(12);
                i5.setEndMonth(15);
                i5.setPercentage(i.getAmount5());
                tobeSaved.add(i5);
                
                TimeRangeInterest i6 = new TimeRangeInterest();
                i6.setName(timeRangeInterestName + i.getMinAmount() + i.getMaxAmount() + "16-18");
                i6.setMinimum(i.getMinAmount());
                i6.setMaximum(i.getMaxAmount());
                i6.setStartMonth(16);
                i6.setEndMonth(18);
                i6.setPercentage(i.getAmount6());
                tobeSaved.add(i6);
                
                TimeRangeInterest i7 = new TimeRangeInterest();
                i7.setName(timeRangeInterestName + i.getMinAmount() + i.getMaxAmount() + "19-24");
                i7.setMinimum(i.getMinAmount());
                i7.setMaximum(i.getMaxAmount());
                i7.setStartMonth(19);
                i7.setEndMonth(24);
                i7.setPercentage(i.getAmount7());
                tobeSaved.add(i7);
                
                TimeRangeInterest i8 = new TimeRangeInterest();
                i8.setName(timeRangeInterestName + i.getMinAmount() + i.getMaxAmount() + "25-36");
                i8.setMinimum(i.getMinAmount());
                i8.setMaximum(i.getMaxAmount());
                i8.setStartMonth(25);
                i8.setEndMonth(36);
                i8.setPercentage(i.getAmount8());
                tobeSaved.add(i8);
            }
            
            System.out.println("New Set size: " + tobeSaved.size());
            List<TimeRangeInterest> result = interestSessionBean.addAllTimeRangeInterest(tobeSaved);
            if (result != null) {
                timeRangeInterests = result;
                MessageUtils.displayInfo("Time Range Interest Created");
            } else {
                MessageUtils.displayError("This Interest Exists");
            }
        } else if (interestType.equals(INTEREST_TYPE_CONDITION)) {
            if (interestSessionBean.addInterest(conditionInterest) != null) {
                conditionInterests.add(conditionInterest);
                MessageUtils.displayInfo("Condition Interest Created");
            } else {
                MessageUtils.displayError("This Interest Exists");
            }
        } else {
            MessageUtils.displayError("There's some error when creating interest");
        }
    }
    
    public String getDisplayCell(Integer row, Integer col) {
        System.out.println("Row is: " + row + " Col is: " + col);
        if (row == 0 && col == 0) {
            return "";
        } else if (row == 0) {
            TimeRangeInterest cell = formatedInterests[row][col - 1];
            return cell.getStartMonth() + "~" + cell.getEndMonth() + "mth";
        } else if (col == 0) {
            TimeRangeInterest cell = formatedInterests[row - 1][col];
            if (cell.getMinimum().equals(BigDecimal.ZERO)) {
                return "First $" + cell.getMaximum();
            }
            return "$" + cell.getMinimum().intValue() + " ~ $" + cell.getMaximum().intValue();
        } else {
            TimeRangeInterest cell = formatedInterests[row - 1][col - 1];
            return cell.getPercentage().toString().substring(0, 6);
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

    /**
     * @return the CONDITION_TYPE_NOWITHDRAW
     */
    public String getCONDITION_TYPE_NOWITHDRAW() {
        return CONDITION_TYPE_NOWITHDRAW;
    }

    /**
     * @param CONDITION_TYPE_NOWITHDRAW the CONDITION_TYPE_NOWITHDRAW to set
     */
    public void setCONDITION_TYPE_NOWITHDRAW(String CONDITION_TYPE_NOWITHDRAW) {
        this.CONDITION_TYPE_NOWITHDRAW = CONDITION_TYPE_NOWITHDRAW;
    }

    /**
     * @return the collectionDTOs
     */
    public List<TimeRangeInterestCollectionDTO> getCollectionDTOs() {
        return collectionDTOs;
    }

    /**
     * @param collectionDTOs the collectionDTOs to set
     */
    public void setCollectionDTOs(List<TimeRangeInterestCollectionDTO> collectionDTOs) {
        this.collectionDTOs = collectionDTOs;
    }

    /**
     * @return the timeRangeInterestName
     */
    public String getTimeRangeInterestName() {
        return timeRangeInterestName;
    }

    /**
     * @param timeRangeInterestName the timeRangeInterestName to set
     */
    public void setTimeRangeInterestName(String timeRangeInterestName) {
        this.timeRangeInterestName = timeRangeInterestName;
    }

    /**
     * @return the colIndex
     */
    public List<Integer> getColIndex() {
        return colIndex;
    }

    /**
     * @param colIndex the colIndex to set
     */
    public void setColIndex(List<Integer> colIndex) {
        this.colIndex = colIndex;
    }

    /**
     * @return the rowIndex
     */
    public List<Integer> getRowIndex() {
        return rowIndex;
    }

    /**
     * @param rowIndex the rowIndex to set
     */
    public void setRowIndex(List<Integer> rowIndex) {
        this.rowIndex = rowIndex;
    }

    /**
     * @return the colSize
     */
    public Integer getColSize() {
        return colSize;
    }

    /**
     * @param colSize the colSize to set
     */
    public void setColSize(Integer colSize) {
        this.colSize = colSize;
    }
}
