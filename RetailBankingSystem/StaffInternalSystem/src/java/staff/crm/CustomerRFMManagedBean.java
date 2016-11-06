/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.crm;

import ejb.session.cms.CustomerProfileSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.crm.RFMSessionBeanLocal;
import entity.customer.Customer;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author wang
 */
@Named(value = "customerRFMManagedBean")
@ViewScoped
public class CustomerRFMManagedBean implements Serializable {

    @EJB
    RFMSessionBeanLocal rFMSessionBean;
    @EJB
    CustomerProfileSessionBeanLocal customerProfileSessionBean;
    @EJB
    NewCustomerSessionBeanLocal customerSessionBean;

    private List<Customer> customers;

    public CustomerRFMManagedBean() {
    }

    @PostConstruct
    public void init() {
        customers = customerProfileSessionBean.getListCustomers();
    }

    public void refreshCustomerRFM() {
        for (Customer c : customers) {
            Long depositRecency = rFMSessionBean.getDepositRecencyByCustomerId(c.getId());
            Long depositFrequency = rFMSessionBean.getDepositFrequencyByCustomerId(c.getId());
            Long depositMonetary = rFMSessionBean.getDepositMonetaryByCustomerId(c.getId());
            c.setDepositRecency(depositRecency);
            c.setDepositFrequency(depositFrequency);
            c.setDepositMonetary(depositMonetary);
            c.setDepositRFMScore((double) (depositRecency + depositFrequency + depositMonetary));

            Long cardRecency = rFMSessionBean.getCardRecencyByCustomerId(c.getId());
            Long cardFrequency = rFMSessionBean.getCardFrequencyByCustomerId(c.getId());
            Long cardMonetary = rFMSessionBean.getCardMonetaryByCustomerId(c.getId());

            c.setCardRecency(cardRecency);
            c.setCardFrequency(cardFrequency);
            c.setCardMonetary(cardMonetary);
            c.setCardRFMScore((double) (cardRecency + cardFrequency + cardMonetary));

            c.setOverallRFMScore( (c.getDepositRFMScore() + c.getCardRFMScore()) / 2 );
            customerSessionBean.updateCustomer(c);
        }
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

}