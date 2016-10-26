/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.loan;

import ejb.session.loan.LoanProductSessionBeanLocal;
import entity.loan.LoanExternalInterest;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.MessageUtils;

/**
 *
 * @author litong
 */
@Named(value = "updateRateManagedBean")
@ViewScoped
public class UpdateRateManagedBean implements Serializable {

    @EJB
    private LoanProductSessionBeanLocal loanProductBean;

    private LoanExternalInterest sb;
    private Double preSibor;
    private Double rate;

    @PostConstruct
    public void init(){
        sb = loanProductBean.getSIBORInterest();
        preSibor = sb.getRate();

    }
    public void updateSibor() {
        sb.setRate(rate);
        System.out.print(sb.getName());

        System.out.print(sb.getRate());
        loanProductBean.updateSIBORInterest(sb);
        MessageUtils.displayInfo("3-month SIBOR has been updated successfully");
           
    }

    public LoanExternalInterest getSb() {
        return sb;
    }

    public void setSb(LoanExternalInterest sb) {
        this.sb = sb;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getPreSibor() {
        return preSibor;
    }

    public void setPreSibor(Double preSibor) {
        this.preSibor = preSibor;
    }

    public LoanProductSessionBeanLocal getLoanProductBean() {
        return loanProductBean;
    }

    public void setLoanProductBean(LoanProductSessionBeanLocal loanProductBean) {
        this.loanProductBean = loanProductBean;
    }

    /**
     * Creates a new instance of UpdateRate
     */
}
