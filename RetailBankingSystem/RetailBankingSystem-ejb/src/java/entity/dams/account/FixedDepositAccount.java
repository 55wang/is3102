/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.dams.account;

import entity.dams.rules.Rule;
import entity.dams.rules.Interest;
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
public class FixedDepositAccount extends DepositAccount {
    // if this is true, no interest will be credited
    // I
    private Boolean earlyWithdrewed = false;
    private Integer periodInMonth;
    @Temporal(value = TemporalType.TIMESTAMP)
    private final Date maturityDate = new Date();
    //Assume interest will be calculated at the maturity date
    @OneToMany(cascade = CascadeType.MERGE)
    private List<Rule> interests = new ArrayList<>();
    
    public void addInterestsRules(List<Interest> interests) {
        interests.addAll(interests);
    }
    
    public void removeInterestsRules(List<Interest> interests) {
        interests.removeAll(interests);
    }
    
    /**
     * @return the interests
     */
    public List<Rule> getInterests() {
        return interests;
    }

    /**
     * @param interests the interests to set
     */
    public void setInterests(List<Rule> interests) {
        this.interests = interests;
    }

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
     * @return the periodInMonth
     */
    public Integer getPeriodInMonth() {
        return periodInMonth;
    }

    /**
     * @param periodInMonth the periodInMonth to set
     */
    public void setPeriodInMonth(Integer periodInMonth) {
        this.periodInMonth = periodInMonth;
    }

    /**
     * @return the maturityDate
     */
    public Date getMaturityDate() {
        return maturityDate;
    }
}
