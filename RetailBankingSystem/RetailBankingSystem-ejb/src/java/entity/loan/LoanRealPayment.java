/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.loan;

import java.util.Date;

/**
 *
 * @author litong
 */
public class LoanRealPayment {
    
    private Integer period;
    private Date transactionDate;
    private Double payment;
    
    public LoanRealPayment(){     
    }
    
    public LoanRealPayment(Integer period, Date transDate, Double payment){
        this.period=period;
        this.transactionDate=transDate;
        this.payment=payment;
    }
    
    public Integer getPeriod(){
        return period;
    }
    
    public void setPeriod(Integer period){
        this.period=period;       
    }
    
    public Date getTransactioDate(){
        return transactionDate;
    }
    
    public void setTransactionDate(Date transDate){
        this.transactionDate=transDate;
    }
    public Double getPayment(){
        return payment;
    }
    public void setPayment(Double payment){
        this.payment=payment;
    }
}
