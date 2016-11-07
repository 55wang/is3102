/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.crm;

import ejb.session.cms.CustomerProfileSessionBeanLocal;
import ejb.session.crm.MarketBasketAnalysisSessionBeanLocal;
import entity.crm.AssociationRuleEntity;
import entity.customer.Customer;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author wang
 */
@Named(value = "marketBasketAnalysisManagedBean")
@ViewScoped
public class MarketBasketAnalysisManagedBean implements Serializable {

    @EJB
    private MarketBasketAnalysisSessionBeanLocal marketBasketAnalysisSessionBean;
    @EJB
    private CustomerProfileSessionBeanLocal customerProfileSessionBean;

    private List<AssociationRuleEntity> rules;

    public MarketBasketAnalysisManagedBean() {
    }
    
//    public static void main(String[] args) {
//        Set<String> a = new HashSet<>();
//        a.add("a");
//        a.add("b");
//        a.add("a");
//        System.out.println(a.toString());
//    }

    @PostConstruct
    public void init() {
        rules = marketBasketAnalysisSessionBean.getListAssociationRules();
    }

    public void testGetListProductName() {
        System.out.println("testGetListProductName()");

        List<Customer> allCustomers = customerProfileSessionBean.getListCustomers();

        for (Customer c : allCustomers) {
            Set<String> productsOfCustomer = marketBasketAnalysisSessionBean.getListProductNameByCustomerId(c.getId());
            System.out.println("customer "+c.getId()+": "+productsOfCustomer.toString());
        }

    }

    public void refreshAssociationRules() {
        System.out.println("refreshAssociationRules()");
        //drop table and recreate new set of rules
        marketBasketAnalysisSessionBean.generateAssociationRules();
        rules = marketBasketAnalysisSessionBean.getListAssociationRules();
    }

    public List<AssociationRuleEntity> getRules() {
        return rules;
    }

    public void setRules(List<AssociationRuleEntity> rules) {
        this.rules = rules;
    }

}
