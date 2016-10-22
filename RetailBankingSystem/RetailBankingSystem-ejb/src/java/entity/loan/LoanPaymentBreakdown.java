/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.loan;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author leiyang
 */
@Entity
public class LoanPaymentBreakdown implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date schedulePaymentDate;//Next payment date
    
    // month length
    private Integer nthMonth;
    // amount to be paid for principal
    private Double principalPayment;
    // amount to be paid for interest
    private Double interestPayment;
    // amount left to be paid for principal
    private Double outstandingPrincipalPayment;
    
    @ManyToOne(cascade = {CascadeType.MERGE})
    private LoanAccount loanAccount;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LoanPaymentBreakdown)) {
            return false;
        }
        LoanPaymentBreakdown other = (LoanPaymentBreakdown) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "entity.loan.LoanPaymentBreakdown[ id=" + id + " ]";
    }
    
    public LoanAccount getLoanAccount() {
        return loanAccount;
    }

    public void setLoanAccount(LoanAccount loanAccount) {
        this.loanAccount = loanAccount;
    }
   
   public Date getSchedulePaymentDate(){
       return schedulePaymentDate;
   }
   
   public void setSchedulePaymentDate(Date date){
       this.schedulePaymentDate=date;
   }
   
   public Double getPrincipalPayment(){
       return principalPayment;
   }
   
   public void setPrincipalPayment(Double prinPayment){
       this.principalPayment=prinPayment;
   }
   
   public Double getInterestPayment(){
       return interestPayment;
   }
   
   public void setInterestPayment(Double intPayment){
       this.interestPayment=intPayment;
   }
   
   public Double getOutstandingPrincipalPayment(){
       return outstandingPrincipalPayment;
   }
   
   public void setOutstandingPrincipalPayment(Double outPrinPayment){
       this.outstandingPrincipalPayment=outPrinPayment;
   }
   
   public Long getId(){
       return id;
   }
   
   public void setId(Long id){
       this.id=id;
   }

    /**
     * @return the nthMonth
     */
    public Integer getNthMonth() {
        return nthMonth;
    }

    /**
     * @param nthMonth the nthMonth to set
     */
    public void setNthMonth(Integer nthMonth) {
        this.nthMonth = nthMonth;
    }

}
