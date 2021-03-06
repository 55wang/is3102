/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.account;

import entity.dams.rules.TimeRangeInterest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author leiyang
 */
@Entity
public class CustomerFixedDepositAccount extends DepositAccount {
    
    // Counter to decrease
    // if this is true, no interest will be credited
    private Boolean earlyWithdrewed = false;
    @Temporal(value = TemporalType.DATE) //precision by date is sufficient
    private Date maturityDate;
    
    // mapping
    @OneToMany(cascade = {CascadeType.MERGE})
    private List<TimeRangeInterest> interestRules = new ArrayList<>();

    /**
     * @return the earlyWithdrewed
     */
    public Boolean getEarlyWithdrewed() {
        return earlyWithdrewed;
    }

    /**
     * @param earlyWithdrewed the earlyWithdrewed to set
     */
    public void setEarlyWithdrewed(Boolean earlyWithdrewed) {
        this.earlyWithdrewed = earlyWithdrewed;
    }

    /**
     * @return the maturityDate
     */
    public Date getMaturityDate() {
        return maturityDate;
    }

    /**
     * @return the interestRules
     */
    public List<TimeRangeInterest> getInterestRules() {
        return interestRules;
    }

    /**
     * @param interestRules the interestRules to set
     */
    public void setInterestRules(List<TimeRangeInterest> interestRules) {
        this.interestRules = interestRules;
    }

    /**
     * @param maturityDate the maturityDate to set
     */
    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }
}
