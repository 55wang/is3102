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
import entity.card.product.CreditCardProduct;
import entity.crm.AssociationRuleEntity;
import entity.customer.Customer;
import entity.dams.account.DepositProduct;
import entity.loan.LoanProduct;
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
        
        Query queryDepositProduct = em.createQuery("SELECT dp.name FROM DepositProduct dp, Customer c, MainAccount ma, DepositAccount da WHERE da.product = dp AND da.mainAccount = ma AND ma.customer = c AND c.id =:id");
        queryDepositProduct.setParameter("id", Id);
        List<String> dps = queryDepositProduct.getResultList();
        for(int i = 0; i < dps.size(); i++){
            productNames.add(dps.get(i));
        }
        
        Query queryCardProduct = em.createQuery("SELECT ccp.productName FROM CreditCardProduct ccp, Customer c, MainAccount ma, CreditCardAccount cca WHERE cca.creditCardProduct = ccp AND cca.mainAccount = ma AND ma.customer = c AND c.id =:id");
        queryCardProduct.setParameter("id", Id);
        List<String> ccps = queryCardProduct.getResultList();
        for(int i = 0; i < ccps.size(); i++){
            productNames.add(ccps.get(i));
        }
        
        Query queryLoanProduct = em.createQuery("SELECT lp.productName FROM LoanProduct lp, Customer c, MainAccount ma, LoanAccount la WHERE la.loanProduct = lp AND la.mainAccount = ma AND ma.customer = c AND c.id =:id");
        queryLoanProduct.setParameter("id", Id);
        List<String> lps = queryLoanProduct.getResultList();
        for(int i = 0; i < lps.size(); i++){
            productNames.add(lps.get(i));
        }
        
        if(productNames.isEmpty())
            return null;
        else
            return productNames;
        //use it as if it is an arraylist
        //it would automatically store unique items, add duplicated items, would 
        //automatically handled by itself, and return a list of unique items

        //add into the productNames Set
        //return a list of product name owned by this customer
        //e.g. productA, productB, productC
        //return null if no products used by this customer
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
