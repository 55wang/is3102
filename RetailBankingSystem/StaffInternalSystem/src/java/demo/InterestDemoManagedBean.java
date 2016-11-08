/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import BatchProcess.InterestAccrualSessionBeanLocal;
import ejb.session.dams.InterestSessionBeanLocal;
import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.CustomerFixedDepositAccount;
import entity.dams.account.DepositAccount;
import entity.dams.account.DepositAccountProduct;
import entity.dams.rules.ConditionInterest;
import entity.dams.rules.Interest;
import entity.dams.rules.RangeInterest;
import entity.dams.rules.TimeRangeInterest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "interestDemoManagedBean")
@ViewScoped
public class InterestDemoManagedBean implements Serializable {

    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;
    @EJB
    private InterestSessionBeanLocal interestSessionBean;
    @EJB
    private InterestAccrualSessionBeanLocal interestAccrualSessionBean;

    private List<Integer> colIndex = new ArrayList<>();
    private List<Integer> rowIndex = new ArrayList<>();
    private TimeRangeInterest[][] formatedInterests;
    private Integer colSize;
    
    private MainAccount demoAccount;
    private DepositAccount showingAccount;
    private String selectedDepositAccount;
    private Map<String, String> availableDepositAccount = new HashMap<>();
    
    
    // Display Current interests with the account
    private List<Interest> normalInterests;
    private List<RangeInterest> rangeInterests;
    private List<ConditionInterest> conditionInterests;
    private List<TimeRangeInterest> timeRangeInterests = new ArrayList<>();

    public InterestDemoManagedBean() {
    }

    @PostConstruct
    public void init() {
        initDemoAccount();
        if (!demoAccount.getBankAcounts().isEmpty()) {
            setShowingAccount(getDemoAccount().getBankAcounts().get(0));
        }
        initInterests();
    }
    
    public void changeAccount() {
        showingAccount = getSelectedAccount();
        initInterests();
    }
    
    private void initDemoAccount() {
        setDemoAccount(mainAccountSessionBean.getMainAccountByUserId(ConstantUtils.DEMO_MAIN_ACCOUNT_USER_ID));
        for (DepositAccount da : demoAccount.getBankAcounts()) {
            availableDepositAccount.put(da.getAccountNumber(), da.getAccountNumber());
        }
    }
    
    private DepositAccount getSelectedAccount() {
            for (DepositAccount da : demoAccount.getBankAcounts()) {
                if (da.getAccountNumber().equals(selectedDepositAccount)) {
                    return da;
                }
        }
        return null;
    }

    public BigDecimal getTotalInterest(DepositAccount account) {
        System.out.println("getTotalInterest starting: account balance:" + account.getBalance());
        // TODO: Seperate from fixed deposit account
        BigDecimal totalInterest = BigDecimal.ZERO.setScale(4, RoundingMode.UP);
        
        System.out.println("Current cumulated interest amount:" + account.getCumulatedInterest().getCummulativeAmount());
        for (int i = 0; i < 30; i ++) {
            account = interestAccrualSessionBean.calculateDailyInterestForDepositAccount(account);
            System.out.println("Current cumulated interest amount:" + account.getCumulatedInterest().getCummulativeAmount());
        }
        
        // End of month
        totalInterest = account.getCumulatedInterest().getCummulativeAmount();
        showingAccount = interestAccrualSessionBean.calculateMonthlyInterestForDepositAccount(account);
        
        return totalInterest;
    }

    private void initInterests() {
        rangeInterests = new ArrayList<>();
        conditionInterests = new ArrayList<>();
        normalInterests = new ArrayList<>();
        timeRangeInterests = new ArrayList<>();
        
        if (showingAccount instanceof CustomerDepositAccount) {

            List<Interest> interests = ((DepositAccountProduct) showingAccount.getProduct()).getInterestRules();

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
        } else if (showingAccount instanceof CustomerFixedDepositAccount) {
            timeRangeInterests = ((CustomerFixedDepositAccount)showingAccount).getInterestRules();
            initTimeRangeDisplay();
        }

    }
    
    private void initTimeRangeDisplay() {
        Set<Integer> set = new HashSet<>();
        for (TimeRangeInterest i : timeRangeInterests) {
            System.out.println("ID: " + i.getId() + " StartMonth: " + i.getStartMonth());
            set.add(i.getStartMonth());
        }
        System.out.println("Before sort");
        Collections.sort(timeRangeInterests);
        System.out.println("Set is size: " + set.size());
        Integer col = set.size();
        Integer row = timeRangeInterests.size() / col;
        System.out.println("Col: " + col + " Row: " + row);
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
    
    public String getDisplayCell(Integer row, Integer col) {
        System.out.println("Row is: " + row + " Col is: " + col);
        if (row == 0 && col == 0) {
            return "";
        } else if (row == 0) {
            TimeRangeInterest cell = getFormatedInterests()[row][col - 1];
            return cell.getStartMonth() + "~" + cell.getEndMonth() + "mth";
        } else if (col == 0) {
            TimeRangeInterest cell = getFormatedInterests()[row - 1][col];
            if (cell.getMinimum().equals(BigDecimal.ZERO)) {
                return "First $" + cell.getMaximum();
            }
            return "$" + cell.getMinimum().intValue() + " ~ $" + cell.getMaximum().intValue();
        } else {
            TimeRangeInterest cell = getFormatedInterests()[row - 1][col - 1];
            return cell.getPercentage().toString().substring(0, 6);
        }
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
     * @return the demoAccount
     */
    public MainAccount getDemoAccount() {
        return demoAccount;
    }

    /**
     * @param demoAccount the demoAccount to set
     */
    public void setDemoAccount(MainAccount demoAccount) {
        this.demoAccount = demoAccount;
    }

    /**
     * @return the showingAccount
     */
    public DepositAccount getShowingAccount() {
        return showingAccount;
    }

    /**
     * @param showingAccount the showingAccount to set
     */
    public void setShowingAccount(DepositAccount showingAccount) {
        this.showingAccount = showingAccount;
    }

    /**
     * @return the selectedDepositAccount
     */
    public String getSelectedDepositAccount() {
        return selectedDepositAccount;
    }

    /**
     * @param selectedDepositAccount the selectedDepositAccount to set
     */
    public void setSelectedDepositAccount(String selectedDepositAccount) {
        this.selectedDepositAccount = selectedDepositAccount;
    }

    /**
     * @return the availableDepositAccount
     */
    public Map<String, String> getAvailableDepositAccount() {
        return availableDepositAccount;
    }

    /**
     * @param availableDepositAccount the availableDepositAccount to set
     */
    public void setAvailableDepositAccount(Map<String, String> availableDepositAccount) {
        this.availableDepositAccount = availableDepositAccount;
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
     * @return the formatedInterests
     */
    public TimeRangeInterest[][] getFormatedInterests() {
        return formatedInterests;
    }

    /**
     * @param formatedInterests the formatedInterests to set
     */
    public void setFormatedInterests(TimeRangeInterest[][] formatedInterests) {
        this.formatedInterests = formatedInterests;
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
