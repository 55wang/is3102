/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.crm;

import CRM.AprioriFrequentItemsetGenerator;
import CRM.AssociationRule;
import CRM.AssociationRuleGenerator;
import CRM.FrequentItemsetData;
import ejb.session.cms.CustomerProfileSessionBeanLocal;
import ejb.session.common.NewCustomerSessionBean;
import ejb.session.common.NewCustomerSessionBeanLocal;
import entity.crm.AssociationRuleEntity;
import entity.customer.Customer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wang
 */
@Stateless
public class MarketBasketAnalysisSessionBean implements MarketBasketAnalysisSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @EJB
    private CustomerProfileSessionBeanLocal customerProfileSessionBean;
    @EJB
    private MarketBasketAnalysisSessionBeanLocal marketBasketAnalysisSessionBean;

    //get all product name from a single customer
    @Override
    public Set<String> getListProductNameByCustomerId(Long Id) {
        Set<String> productNames = new HashSet<>();
        //use it as if it is an arraylist
        //it would automatically store unique items, add duplicated items, would 
        //automatically handled by itself, and return a list of unique items

        //add into the productNames Set
        //return a list of product name owned by this customer
        //e.g. productA, productB, productC
        //return null if no products used by this customer
        return null;
    }

    @Override
    public void generateAssociationRules() {

        AprioriFrequentItemsetGenerator<String> generator = new AprioriFrequentItemsetGenerator<>();

        List<Set<String>> itemsetList = new ArrayList<>();
        Set<String> productsOfCustomer;

        //get all customers
        List<Customer> customers = customerProfileSessionBean.getListCustomers();
        for (Customer c : customers) {

            productsOfCustomer = getListProductNameByCustomerId(c.getId());
            //add into itemsetList if it is not null
            if (productsOfCustomer != null) {
                itemsetList.add(productsOfCustomer);
            }

        }

        if (!itemsetList.isEmpty()) {

            long startTime = System.nanoTime();
            FrequentItemsetData<String> data = generator.generate(itemsetList, 0.0);
            long endTime = System.nanoTime();

            int i = 1;

            System.out.println("--- Frequent itemsets ---");

            for (Set<String> itemset : data.getFrequentItemsetList()) {
                System.out.printf("%2d: %9s, support: %1.1f\n",
                        i++,
                        itemset,
                        data.getSupport(itemset));
            }

            System.out.printf("Mined frequent itemset in %d milliseconds.\n",
                    (endTime - startTime) / 1_000_000);

            startTime = System.nanoTime();
            List<AssociationRule<String>> associationRuleList
                    = new AssociationRuleGenerator<String>()
                    .mineAssociationRules(data, 0.0);
            endTime = System.nanoTime();

            i = 1;

            System.out.println();

            System.out.println("### Delete Association Rules Table ###");
            deleteAssociationrules();

            System.out.println("--- Association rules ---");

            for (AssociationRule<String> rule : associationRuleList) {
                System.out.printf("%2d: %s\n", i++, rule);
                //save the rule into entity
                AssociationRuleEntity ruleEntity = new AssociationRuleEntity();
                ruleEntity.setAntecedent(rule.getAntecedent());
                ruleEntity.setConsequent(rule.getConsequent());
                ruleEntity.setConfidence(rule.getConfidence());
                ruleEntity.setLift(rule.getLift());

                marketBasketAnalysisSessionBean.createAssociationRule(ruleEntity);

            }

            System.out.printf("Mined association rules in %d milliseconds.\n",
                    (endTime - startTime) / 1_000_000);
        }

    }

    @Override
    public List<AssociationRuleEntity> getListAssociationRules() {
        Query q = em.createQuery("SELECT a FROM AssociationRuleEntity a");
        return q.getResultList();
    }

    @Override
    public AssociationRuleEntity getAssociationRule(Long Id) {
        Query q = em.createQuery("SELECT a FROM AssociationRuleEntity a WHERE a.id =:inId");
        q.setParameter("inId", Id);
        return (AssociationRuleEntity) q.getSingleResult();
    }

    @Override
    public AssociationRuleEntity updateAssociationRule(AssociationRuleEntity rule) {
        em.merge(rule);
        return rule;
    }

    @Override
    public Integer deleteAssociationrules() {
        Query q = em.createQuery("DELETE FROM AssociationRuleEntity");
        return q.executeUpdate();
    }

    @Override
    public AssociationRuleEntity createAssociationRule(AssociationRuleEntity rule) {
        em.persist(rule);
        return rule;
    }

}
