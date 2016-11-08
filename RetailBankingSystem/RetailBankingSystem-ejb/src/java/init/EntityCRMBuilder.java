/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.cms.CustomerProfileSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.crm.RFMSessionBeanLocal;
import entity.customer.Customer;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author wang
 */
@Stateless
@LocalBean
public class EntityCRMBuilder {

    @EJB
    CustomerProfileSessionBeanLocal customerProfileSessionBean;
    @EJB
    RFMSessionBeanLocal rFMSessionBean;
    @EJB
    NewCustomerSessionBeanLocal newCustomerSessionBean;
    
    public void initCustomerRFM() {
        List<Customer> customers = customerProfileSessionBean.getListCustomers();

        for (Customer c : customers) {
            Long depositRecency = rFMSessionBean.getDepositRecencyByCustomerId(c.getId());
            Long depositFrequency = rFMSessionBean.getDepositFrequencyByCustomerId(c.getId());
            Long depositMonetary = rFMSessionBean.getDepositMonetaryByCustomerId(c.getId());
            c.setDepositRecency(depositRecency);
            c.setDepositFrequency(depositFrequency);
            c.setDepositMonetary(depositMonetary);

            Long cardRecency = rFMSessionBean.getCardRecencyByCustomerId(c.getId());
            Long cardFrequency = rFMSessionBean.getCardFrequencyByCustomerId(c.getId());
            Long cardMonetary = rFMSessionBean.getCardMonetaryByCustomerId(c.getId());

            c.setCardRecency(cardRecency);
            c.setCardFrequency(cardFrequency);
            c.setCardMonetary(cardMonetary);

            newCustomerSessionBean.updateCustomer(c);
        }

    }

}
