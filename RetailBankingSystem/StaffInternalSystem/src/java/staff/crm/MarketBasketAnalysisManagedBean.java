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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
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

    @PostConstruct
    public void init() {
        rules = marketBasketAnalysisSessionBean.getListAssociationRules();
        for (Iterator<AssociationRuleEntity> iterator = rules.iterator(); iterator.hasNext();) {
            AssociationRuleEntity rule = iterator.next();
            if (rule.getAntecedent().size() > 2 || rule.getConsequent().size() > 2) {
                // Remove the current element from the iterator and the list.
                iterator.remove();
            }
        }
    }

    public void testGetListProductName() {
        System.out.println("testGetListProductName()");

        List<Customer> allCustomers = customerProfileSessionBean.getListCustomers();

        for (Customer c : allCustomers) {
            Set<String> productsOfCustomer = marketBasketAnalysisSessionBean.getListProductNameByCustomerId(c.getId());
            if (productsOfCustomer != null) {
                System.out.println("customer " + c.getId() + ": " + productsOfCustomer.toString());
            }

        }

    }

    @Asynchronous
    public void refreshAssociationRules() {
        System.out.println("refreshAssociationRules()");
        //drop table and recreate new set of rules
        marketBasketAnalysisSessionBean.generateAssociationRules();
        rules = marketBasketAnalysisSessionBean.getListAssociationRules();

        for (Iterator<AssociationRuleEntity> iterator = rules.iterator(); iterator.hasNext();) {
            AssociationRuleEntity rule = iterator.next();
            if (rule.getAntecedent().size() > 2 || rule.getConsequent().size() > 2) {
                // Remove the current element from the iterator and the list.
                iterator.remove();
            }
        }

    }

    public List<AssociationRuleEntity> getRules() {
        return rules;
    }

    public void setRules(List<AssociationRuleEntity> rules) {
        this.rules = rules;
    }

}
