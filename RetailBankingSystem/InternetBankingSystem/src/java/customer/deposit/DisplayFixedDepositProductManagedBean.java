/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.deposit;

import ejb.session.dams.InterestSessionBeanLocal;
import entity.dams.rules.TimeRangeInterest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author leiyang
 */
@Named(value = "displayFixedDepositProductManagedBean")
@ViewScoped
public class DisplayFixedDepositProductManagedBean implements Serializable {

    @EJB
    private InterestSessionBeanLocal interestBean;
    
    private List<TimeRangeInterest> timeRangeInterests;
    private TimeRangeInterest[][] formatedInterests;
    private List<Integer> colIndex = new ArrayList<>();
    private List<Integer> rowIndex = new ArrayList<>();
    
    /**
     * Creates a new instance of DisplayFixedDepositProductManagedBean
     */
    public DisplayFixedDepositProductManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        System.out.println("@PostConstruct: ");
        setTimeRangeInterests(interestBean.getFixedDepositAccountDefaultInterests());
        System.out.println("timeRangeInterests: " + timeRangeInterests.size());
        Set<Integer> set = new HashSet<>();
        for (TimeRangeInterest i : timeRangeInterests) {
            System.out.println("ID: " + i.getId() + " StartMonth: " + i.getStartMonth());
            set.add(i.getStartMonth());
        }
        System.out.println("Before sort");
        Collections.sort(timeRangeInterests);
        for (TimeRangeInterest i : timeRangeInterests) {
            System.out.println("ID: " + i.getId() + " StartMonth: " + i.getStartMonth());
        }
        
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
        System.out.println(getColIndex());
        System.out.println(getRowIndex());
//        getRowIndex().add(getRowIndex().size());
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
//            DecimalFormat format = new DecimalFormat("0.00");
//            long percent = cell.getPercentage().longValue() / 10;
//            return format.format(percent) + "%";
            return cell.getPercentage().toString();
        }
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

}
