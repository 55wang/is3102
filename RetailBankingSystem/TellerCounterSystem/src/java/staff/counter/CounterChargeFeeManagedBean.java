/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.counter;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.counter.TellerCounterSessionBeanLocal;
import entity.counter.ServiceChargeTransaction;
import entity.counter.TellerCounter;
import entity.customer.MainAccount;
import entity.staff.ServiceCharge;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "counterChargeFeeManagedBean")
@ViewScoped
public class CounterChargeFeeManagedBean implements Serializable {

    @EJB
    private TellerCounterSessionBeanLocal counterBean;
    @EJB
    private LoginSessionBeanLocal loginBean;
    
    private String customerICNumber;
    private String selectedService;
    private List<ServiceCharge> charges = new ArrayList<>();
    
    public CounterChargeFeeManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        charges = counterBean.getAllServiceCharges();
    }
    
    public void chargeFee() {
        ServiceChargeTransaction newServiceFee = new ServiceChargeTransaction();
        MainAccount ma = loginBean.getMainAccountByUserIC(customerICNumber);
        ServiceCharge sc = counterBean.getServiceChargeByName(selectedService);
        newServiceFee.setServiceCharge(sc);
        newServiceFee.setMainAccount(ma);
        newServiceFee.setCustomerIC(customerICNumber);
        newServiceFee.setStaffAccount(SessionUtils.getStaff());
        
        ServiceChargeTransaction result = counterBean.createServiceChargeTransaction(newServiceFee);
        
        if (result == null) {
            MessageUtils.displayError("Service Fee Charge Failed! Check with IT administrator!");
        } else {
            // tc logics
            TellerCounter tc = SessionUtils.getTellerCounter();
            tc = counterBean.getTellerCounterById(tc.getId());
            tc.addCash(sc.getCharges());
            tc = counterBean.updateTellerCounter(tc);
            SessionUtils.setTellerCounter(tc);
            
            MessageUtils.displayInfo("Service Fee Charge Recorded!");
        }
    }

    /**
     * @return the customerICNumber
     */
    public String getCustomerICNumber() {
        return customerICNumber;
    }

    /**
     * @param customerICNumber the customerICNumber to set
     */
    public void setCustomerICNumber(String customerICNumber) {
        this.customerICNumber = customerICNumber;
    }

    /**
     * @return the selectedService
     */
    public String getSelectedService() {
        return selectedService;
    }

    /**
     * @param selectedService the selectedService to set
     */
    public void setSelectedService(String selectedService) {
        this.selectedService = selectedService;
    }

    /**
     * @return the charges
     */
    public List<ServiceCharge> getCharges() {
        return charges;
    }

    /**
     * @param charges the charges to set
     */
    public void setCharges(List<ServiceCharge> charges) {
        this.charges = charges;
    }
    
}
