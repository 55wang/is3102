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
public class LoanPaymentBreakdown{
    private Date schedulePaymentDate;
    private Integer period;
    private Double principalPayment;
    private Double interestPayment;
    private Double outstandingPrincipalPayment;
    
   public LoanPaymentBreakdown() {
   }
   
   public LoanPaymentBreakdown(Date date, Integer period, Double prinPayment, Double intPayment, Double outPrinPayment) {
       this.schedulePaymentDate = date;
       this.period=period;
       this.principalPayment = prinPayment;
       this.interestPayment = intPayment;
       this.outstandingPrincipalPayment=outPrinPayment;

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
   
   public Integer getPeriod(){
       return period;
   }
   
   public void setPeriod(Integer period){
       this.period=period;
   }

}
