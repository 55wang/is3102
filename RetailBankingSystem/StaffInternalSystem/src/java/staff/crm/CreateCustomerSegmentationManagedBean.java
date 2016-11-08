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
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;

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
    
    private String functionType = "SET_HASH_TAG";
    private String SET_HASH_TAG = "SET_HASH_TAG";
    private String CREATE_CUSTOMER_GROUP = "CREATE_CUSTOMER_GROUP";
    
    private TagCloudModel model;

    public CreateCustomerSegmentationManagedBean() {
    }

    @PostConstruct
    public void init() {
        customerGroups = customerSegmentationSessionBean.getListCustomerGroup();
        customerGroup = new CustomerGroup();
        createTagCloudModel();
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
    
    private void createTagCloudModel(){
        model = new DefaultTagCloudModel();
        model.addTag(new DefaultTagCloudItem("Transformers", 1));
        model.addTag(new DefaultTagCloudItem("RIA", "#", 3));
        model.addTag(new DefaultTagCloudItem("AJAX", 2));
        model.addTag(new DefaultTagCloudItem("jQuery", "#", 5));
        model.addTag(new DefaultTagCloudItem("NextGen", 4));
        model.addTag(new DefaultTagCloudItem("JSF 2.0", "#", 2));
        model.addTag(new DefaultTagCloudItem("FCB", 5));
        model.addTag(new DefaultTagCloudItem("Mobile",  3));
        model.addTag(new DefaultTagCloudItem("Themes", "#", 4));
        model.addTag(new DefaultTagCloudItem("Rocks", "#", 1));
    }
    
    public TagCloudModel getModel() {
        return model;
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

    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    public String getSET_HASH_TAG() {
        return SET_HASH_TAG;
    }

    public void setSET_HASH_TAG(String SET_HASH_TAG) {
        this.SET_HASH_TAG = SET_HASH_TAG;
    }

    public String getCREATE_CUSTOMER_GROUP() {
        return CREATE_CUSTOMER_GROUP;
    }

    public void setCREATE_CUSTOMER_GROUP(String CREATE_CUSTOMER_GROUP) {
        this.CREATE_CUSTOMER_GROUP = CREATE_CUSTOMER_GROUP;
    }

}
