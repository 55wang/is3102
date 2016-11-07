/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.crm;

import entity.crm.CustomerGroup;
import entity.customer.Customer;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wang
 */
@Local
public interface CustomerSegmentationSessionBeanLocal {

    public List<Customer> getListFilterCustomersByRFMAndHashTag(Long depositRecency, Long depositFrequency, Long depositMonetary, Long cardRecency, Long cardFrequency, Long cardMonetary, String hashTag, Double actualIncome);    
    public List<Customer> getCustomersByOptions(Long depositRecency, Long depositFrequency, Long depositMonetary, Long cardRecency, Long cardFrequency, Long cardMonetary, Double actualIncome);
    public List<Customer> getListFilterCustomersByRFM(Long depositRecency, Long depositFrequency, Long depositMonetary, Long cardRecency, Long cardFrequency, Long cardMonetary, Double actualIncome);
    public List<CustomerGroup> getListCustomerGroup();
    public CustomerGroup getCustomerGroup(Long Id);
    public CustomerGroup createCustomerGroup(CustomerGroup customerGroup);
    public CustomerGroup updateCustomerGroup(CustomerGroup customerGroup);
    
}
