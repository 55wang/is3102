/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.wealth;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SlideEndEvent;
import utils.RedirectUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "wealthAdvertismentManagedBean")
@ViewScoped
public class wealthAdvertismentManagedBean implements Serializable{
    private Integer investAmount = 500;
    private Double chargeFee = 0.0;
    /**
     * Creates a new instance of wealthAdvertismentManagedBean
     */
    @PostConstruct
    private void init(){
    }
    
    public wealthAdvertismentManagedBean() {
    }
    
    public void investNow(){
        RedirectUtils.redirect("request_new_investment_plan.xhtml");
    }
    
    public void onSlideChange(SlideEndEvent event){
        int amount = event.getValue();
        if(amount<10000)
            chargeFee = 0.0;
        else{
            chargeFee = (amount-10000)*0.0025*30/365;
        }
    }

    public Integer getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(Integer investAmount) {
        this.investAmount = investAmount;
    }

    public Double getChargeFee() {
        return chargeFee;
    }

    public void setChargeFee(Double chargeFee) {
        this.chargeFee = chargeFee;
    }
    
}
