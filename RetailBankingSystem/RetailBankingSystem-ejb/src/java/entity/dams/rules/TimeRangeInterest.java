/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.rules;

import javax.persistence.Entity;

/**
 *
 * @author leiyang
 */
@Entity
public class TimeRangeInterest extends RangeInterest {
    // if endMonth is 0, assume > startMonth will apply this interest
    private Integer startMonth;
    private Integer endMonth;

    /**
     * @return the startMonth
     */
    public Integer getStartMonth() {
        return startMonth;
    }

    /**
     * @param startMonth the startMonth to set
     */
    public void setStartMonth(Integer startMonth) {
        this.startMonth = startMonth;
    }

    /**
     * @return the endMonth
     */
    public Integer getEndMonth() {
        return endMonth;
    }

    /**
     * @param endMonth the endMonth to set
     */
    public void setEndMonth(Integer endMonth) {
        this.endMonth = endMonth;
    }
}
