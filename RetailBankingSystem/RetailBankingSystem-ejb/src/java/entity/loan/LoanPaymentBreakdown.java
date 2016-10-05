/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.loan;

import java.util.*;

/**
 *
 * @author litong
 */
public class LoanPaymentBreakdown {
    private Date schedulePaymentDate;
    private Double principalPayment;
    private Double interestPayment;
    
   public LoanPaymentBreakdown() {
   }
   
   public LoanPaymentBreakdown(Date date, Double prinPayment, Double intPayment) {
       this.schedulePaymentDate = date;
       this.principalPayment = prinPayment;
       this.interestPayment = intPayment;
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
}
