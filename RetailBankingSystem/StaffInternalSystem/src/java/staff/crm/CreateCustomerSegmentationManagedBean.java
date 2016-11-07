/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.crm;

import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.crm.CustomerSegmentationSessionBeanLocal;
import entity.crm.CustomerGroup;
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
@Named(value = "createCustomerSegmentationManagedBean")
@ViewScoped
public class CreateCustomerSegmentationManagedBean implements Serializable {

    @EJB
    private CustomerSegmentationSessionBeanLocal customerSegmentationSessionBean;
    @EJB
    private NewCustomerSessionBeanLocal newCustomerSessionBean;

    private List<CustomerGroup> customerGroups;
    private CustomerGroup customerGroup;
    private List<Customer> filterCustomers;
    private List<Customer> setHashTagCustomers;

    public CreateCustomerSegmentationManagedBean() {
    }

    @PostConstruct
    public void init() {
        customerGroups = customerSegmentationSessionBean.getListCustomerGroup();
        customerGroup = new CustomerGroup();
    }

    public void setHashTag() {
        setHashTagCustomers = customerSegmentationSessionBean.getCustomersByOptions(
                customerGroup.getDepositRecency(),
                customerGroup.getDepositFrequency(),
                customerGroup.getDepositMonetary(),
                customerGroup.getCardRecency(),
                customerGroup.getCardFrequency(),
                customerGroup.getCardMonetary(),
                customerGroup.getActualIncome()
        );

        for (Customer c : setHashTagCustomers) {
            c.setHashTag(customerGroup.getHashTag());
            newCustomerSessionBean.updateCustomer(c);
        }

    }

    public void createGroup() {

        if (customerGroup.getHashTag() == null) {
            filterCustomers = customerSegmentationSessionBean.getListFilterCustomersByRFM(
                    customerGroup.getDepositRecency(),
                    customerGroup.getDepositFrequency(),
                    customerGroup.getDepositMonetary(),
                    customerGroup.getCardRecency(),
                    customerGroup.getCardFrequency(),
                    customerGroup.getCardMonetary(),
                    customerGroup.getActualIncome()
            );
        } else {
            filterCustomers = customerSegmentationSessionBean.getListFilterCustomersByRFMAndHashTag(
                    customerGroup.getDepositRecency(),
                    customerGroup.getDepositFrequency(),
                    customerGroup.getDepositMonetary(),
                    customerGroup.getCardRecency(),
                    customerGroup.getCardFrequency(),
                    customerGroup.getCardMonetary(),
                    customerGroup.getHashTag(),
                    customerGroup.getActualIncome()
            );
        }

        customerGroup.setCustomer(filterCustomers);

        try {
            customerSegmentationSessionBean.createCustomerGroup(customerGroup);
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    public List<CustomerGroup> getCustomerGroups() {
        return customerGroups;
    }

    public void setCustomerGroups(List<CustomerGroup> customerGroups) {
        this.customerGroups = customerGroups;
    }

    public CustomerGroup getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(CustomerGroup customerGroup) {
        this.customerGroup = customerGroup;
    }

    public List<Customer> getFilterCustomers() {
        return filterCustomers;
    }

    public void setFilterCustomers(List<Customer> filterCustomers) {
        this.filterCustomers = filterCustomers;
    }

    public List<Customer> getSetHashTagCustomers() {
        return setHashTagCustomers;
    }

    public void setSetHashTagCustomers(List<Customer> setHashTagCustomers) {
        this.setHashTagCustomers = setHashTagCustomers;
    }

}
