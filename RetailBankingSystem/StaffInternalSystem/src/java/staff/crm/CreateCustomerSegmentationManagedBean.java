/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.crm;

import ejb.session.common.NewCustomerSessionBeanLocal;
import ejb.session.crm.CustomerSegmentationSessionBeanLocal;
import ejb.session.crm.MarketBasketAnalysisSessionBeanLocal;
import entity.crm.CustomerGroup;
import entity.customer.Customer;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudItem;
import org.primefaces.model.tagcloud.TagCloudModel;
import utils.MessageUtils;

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
    @EJB
    private MarketBasketAnalysisSessionBeanLocal marketBasketAnalysisSessionBean;

    private List<CustomerGroup> customerGroups;
    private CustomerGroup customerGroup;
    private List<Customer> filterCustomers;
    private List<Customer> setHashTagCustomers;

    private String selectedAntecedent;
    private Set<String> totalUniqueProductName;

    private String functionType = "SET_HASH_TAG";
    private String SET_HASH_TAG = "SET_HASH_TAG";
    private String CREATE_CUSTOMER_GROUP = "CREATE_CUSTOMER_GROUP";

    private HashMap<String, Long> mapHashTagCount = new HashMap<>();
    private TagCloudModel model;

    public CreateCustomerSegmentationManagedBean() {
    }

    @PostConstruct
    public void init() {
        totalUniqueProductName = marketBasketAnalysisSessionBean.getListProductName();
        customerGroups = customerSegmentationSessionBean.getListCustomerGroup();
        customerGroup = new CustomerGroup();
        createTagCloudModel();
    }

    public void setHashTag() {
        System.out.println("setHashTag()");
        System.out.println("selectedAntecedent: " + selectedAntecedent);

        if (selectedAntecedent == null) {
            setHashTagCustomers = customerSegmentationSessionBean.getListFilterCustomersByRFMAndIncome(
                    customerGroup.getDepositRecency(),
                    customerGroup.getDepositFrequency(),
                    customerGroup.getDepositMonetary(),
                    customerGroup.getCardRecency(),
                    customerGroup.getCardFrequency(),
                    customerGroup.getCardMonetary(),
                    customerGroup.getActualIncome()
            );
        } else {
            setHashTagCustomers = customerSegmentationSessionBean.getListFilterCustomersByRFMAndIncomeAndAntecedent(
                    customerGroup.getDepositRecency(),
                    customerGroup.getDepositFrequency(),
                    customerGroup.getDepositMonetary(),
                    customerGroup.getCardRecency(),
                    customerGroup.getCardFrequency(),
                    customerGroup.getCardMonetary(),
                    customerGroup.getActualIncome(),
                    selectedAntecedent
            );
        }

        if (setHashTagCustomers.isEmpty()) {
            MessageUtils.displayError("No Customer Meet the Requirements");
        } else {
            for (Customer c : setHashTagCustomers) {
                c.setHashTag(customerGroup.getHashTag());
                newCustomerSessionBean.updateCustomer(c);
            }

            customerGroup.setHashTag("");

            createTagCloudModel();
        }

    }

    public void createGroup() {
        System.out.println("createGroup()");
        System.out.println("selectedAntecedent: " + selectedAntecedent);

        if (customerGroup.getHashTag() == null) {
            filterCustomers = customerSegmentationSessionBean.getListFilterCustomersByRFMAndIncome(
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

        customerGroup.setCustomers(filterCustomers);

        try {
            customerSegmentationSessionBean.createCustomerGroup(customerGroup);
            MessageUtils.displayInfo("Customer Group Created Successfully!");
        } catch (Exception ex) {
            System.out.println(ex);
            MessageUtils.displayError("Customer Group Created Fail!");
        }

    }

    private void createTagCloudModel() {
        System.out.println("createTagCloudModel()");
        model = new DefaultTagCloudModel();

        mapHashTagCount = generateHashTagAndCount();

        for (HashMap.Entry<String, Long> entry : mapHashTagCount.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            model.addTag(new DefaultTagCloudItem(entry.getKey(), "#", entry.getValue().intValue()));
        }

//        model.addTag(new DefaultTagCloudItem("Transformers", 1));
//        model.addTag(new DefaultTagCloudItem("RIA", "#", 3));
//        model.addTag(new DefaultTagCloudItem("AJAX", 2));
//        model.addTag(new DefaultTagCloudItem("jQuery", "#", 5));
//        model.addTag(new DefaultTagCloudItem("NextGen", 4));
//        model.addTag(new DefaultTagCloudItem("JSF 2.0", "#", 2));
//        model.addTag(new DefaultTagCloudItem("FCB", 5));
//        model.addTag(new DefaultTagCloudItem("Mobile", 3));
//        model.addTag(new DefaultTagCloudItem("Themes", "#", 4));
//        model.addTag(new DefaultTagCloudItem("Rocks", "#", 1));
    }

    public HashMap<String, Long> generateHashTagAndCount() {
        System.out.println("generateHashTagAndCount()");

        List<String> listHashTags = customerSegmentationSessionBean.getListCustomersHashTag();
        System.out.println(listHashTags.size());

        String totalHashTag = "";
        for (String hashTag : listHashTags) {
            totalHashTag += hashTag;
        }
        System.out.println(totalHashTag);
        String[] parts = totalHashTag.split("#");
        HashMap<String, Long> tempMapHashTagCount = new HashMap<>();
        for (String part : parts) {

            if (part.length() != 0) {

                Long value = tempMapHashTagCount.getOrDefault(part, 0L);

                tempMapHashTagCount.put(part, ++value);
                System.out.println(part + " " + value);
            }
        }

        return tempMapHashTagCount;
    }

    public void onSelect(SelectEvent event) {
        if (functionType.equals(CREATE_CUSTOMER_GROUP)) {
            TagCloudItem item = (TagCloudItem) event.getObject();
            if (customerGroup.getHashTag() == null || customerGroup.getHashTag().isEmpty()) {
                customerGroup.setHashTag("#" + item.getLabel());
            } else {
                customerGroup.setHashTag(customerGroup.getHashTag() + "#" + item.getLabel());
            }
        }
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

    public HashMap<String, Long> getMapHashTagCount() {
        return mapHashTagCount;
    }

    public void setMapHashTagCount(HashMap<String, Long> mapHashTagCount) {
        this.mapHashTagCount = mapHashTagCount;
    }

    public String getSelectedAntecedent() {
        return selectedAntecedent;
    }

    public void setSelectedAntecedent(String selectedAntecedent) {
        this.selectedAntecedent = selectedAntecedent;
    }

    public Set<String> getTotalUniqueProductName() {
        return totalUniqueProductName;
    }

    public void setTotalUniqueProductName(Set<String> totalUniqueProductName) {
        this.totalUniqueProductName = totalUniqueProductName;
    }

}
