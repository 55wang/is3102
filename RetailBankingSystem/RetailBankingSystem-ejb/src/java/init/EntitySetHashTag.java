/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.crm.CustomerSegmentationSessionBeanLocal;
import entity.crm.HashTagProperties;
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
public class EntitySetHashTag {

    @EJB
    CustomerSegmentationSessionBeanLocal customerSegmentationSessionBean;
    @EJB
    NewCustomerSessionBeanLocal newCustomerSessionBean;

    public void initHashTag() {
        HashTagProperties htp = new HashTagProperties();
        htp.setDepositRecency(3L);
        htp.setDepositFrequency(3L);
        htp.setDepositMonetary(3L);
        htp.setCardRecency(3L);
        htp.setCardFrequency(3L);
        htp.setCardMonetary(3L);
        
        setHashTagName(htp, "#SexyLady");
    }
    
    private void setHashTagName(HashTagProperties htp,String hashTag) {

        List<Customer> setHashTagCustomers;
        setHashTagCustomers = customerSegmentationSessionBean.getListFilterCustomersByRFMAndIncome(
                htp.getDepositRecency(),
                htp.getDepositFrequency(),
                htp.getDepositMonetary(),
                htp.getCardRecency(),
                htp.getCardFrequency(),
                htp.getCardMonetary(),
                htp.getActualIncome()
        );

        for (Customer c : setHashTagCustomers) {
            c.setHashTag(hashTag);
            newCustomerSessionBean.updateCustomer(c);
        }

    }
}
