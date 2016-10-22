/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dto;

import java.io.Serializable;

/**
 *
 * @author leiyang
 */
public class LoanRangeInterestDTO implements Serializable {
    
    private Integer startMonth;
    private Integer endMonth;
    private Double interestRate;
    private String externalInteret;

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

    /**
     * @return the interestRate
     */
    public Double getInterestRate() {
        return interestRate;
    }

    /**
     * @param interestRate the interestRate to set
     */
    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * @return the externalInteret
     */
    public String getExternalInteret() {
        return externalInteret;
    }

    /**
     * @param externalInteret the externalInteret to set
     */
    public void setExternalInteret(String externalInteret) {
        this.externalInteret = externalInteret;
    }
}
