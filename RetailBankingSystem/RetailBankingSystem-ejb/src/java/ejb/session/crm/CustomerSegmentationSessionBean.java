/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.crm;

import entity.card.account.CreditCardAccount;
import entity.card.product.CreditCardProduct;
import entity.crm.CustomerGroup;
import entity.customer.Customer;
import entity.dams.account.DepositAccount;
import entity.dams.account.DepositProduct;
import entity.loan.LoanAccount;
import entity.loan.LoanProduct;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class CustomerSegmentationSessionBean implements CustomerSegmentationSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<String> getListCustomersHashTag() {
        Query q = em.createQuery("SELECT DISTINCT(c.hashTag) FROM Customer c");
        return q.getResultList();
    }

    @Override
    public List<Customer> getListFilterCustomersByRFMAndIncomeAndAntecedent(Long depositRecency, Long depositFrequency, Long depositMonetary, Long cardRecency, Long cardFrequency, Long cardMonetary, Double actualIncome, String antecedent) {
        Query q = em.createQuery("Select c from Customer c WHERE c.depositRecency >=:inDepositRecency AND c.depositFrequency >=:inDepositFrequency AND c.depositMonetary >=:inDepositMonetary AND c.cardRecency >=:inCardRecency AND c.cardFrequency >=:inCardFrequency AND c.cardMonetary >=:inCardMonetary AND c.actualIncome >=:inIncome");
        q.setParameter("inDepositRecency", depositRecency);
        q.setParameter("inDepositFrequency", depositFrequency);
        q.setParameter("inDepositMonetary", depositMonetary);
        q.setParameter("inCardRecency", cardRecency);
        q.setParameter("inCardFrequency", cardFrequency);
        q.setParameter("inCardMonetary", cardMonetary);
        q.setParameter("inIncome", actualIncome);
        
        
        List<Customer> customers = q.getResultList();
        List<Customer> resultCustomers = q.getResultList();
        if(!customers.isEmpty()){
            System.out.println("Customer size: "+customers.size());
            for(int i = 0; i < customers.size(); i++){
                Customer c = checkCustomerHasAndOnlyHasAntecedent(customers.get(i), antecedent);
                if(c != null)
                   resultCustomers.add(c);
            }
        }
        
        return resultCustomers;
    }
    
    private Customer checkCustomerHasAndOnlyHasAntecedent(Customer c, String antecedent){
        Boolean hasTheProduct = false;
        List<DepositAccount> das = c.getMainAccount().getBankAcounts();
            for(int j = 0; j < das.size(); j++){
                DepositProduct dp = das.get(j).getProduct();
                if(dp.getName().equals(antecedent))
                    hasTheProduct = true;
                else
                    return null;
            }
            List<LoanAccount> las = c.getMainAccount().getLoanAccounts();
            for(int j = 0; j < las.size(); j++){
                LoanProduct lp = las.get(j).getLoanProduct();
                if(lp.getProductName().equals(antecedent))
                    hasTheProduct = true;
                else
                    return null;
            }
            List<CreditCardAccount> ccas = c.getMainAccount().getCreditCardAccounts();
            for(int j = 0; j < ccas.size(); j++){
                CreditCardProduct ccp = ccas.get(j).getCreditCardProduct();
                if(ccp.getProductName().equals(antecedent))
                    hasTheProduct = true;
                else
                    return null;
            }
        if(hasTheProduct)
            return c;
        else
            return null;
    }
    
    @Override
    public List<Customer> getListFilterCustomersByRFMAndIncome(Long depositRecency, Long depositFrequency, Long depositMonetary, Long cardRecency, Long cardFrequency, Long cardMonetary, Double actualIncome) {
        Query q = em.createQuery("Select c from Customer c WHERE c.depositRecency >=:inDepositRecency AND c.depositFrequency >=:inDepositFrequency AND c.depositMonetary >=:inDepositMonetary AND c.cardRecency >=:inCardRecency AND c.cardFrequency >=:inCardFrequency AND c.cardMonetary >=:inCardMonetary AND c.actualIncome >=:inIncome");
        q.setParameter("inDepositRecency", depositRecency);
        q.setParameter("inDepositFrequency", depositFrequency);
        q.setParameter("inDepositMonetary", depositMonetary);
        q.setParameter("inCardRecency", cardRecency);
        q.setParameter("inCardFrequency", cardFrequency);
        q.setParameter("inCardMonetary", cardMonetary);
        q.setParameter("inIncome", actualIncome);
        return q.getResultList();
    }

    @Override
    public List<Customer> getListFilterCustomersByRFMAndHashTag(Long depositRecency, Long depositFrequency, Long depositMonetary, Long cardRecency, Long cardFrequency, Long cardMonetary, String hashTag, Double actualIncome) {
        Query q = em.createQuery("Select c from Customer c WHERE c.depositRecency >=:inDepositRecency AND c.depositFrequency >=:inDepositFrequency AND c.depositMonetary >=:inDepositMonetary AND c.cardRecency >=:inCardRecency AND c.cardFrequency >=:inCardFrequency AND c.cardMonetary >=:inCardMonetary AND c.hashTag like :inHashTag AND c.actualIncome >=:inIncome");
        q.setParameter("inDepositRecency", depositRecency);
        q.setParameter("inDepositFrequency", depositFrequency);
        q.setParameter("inDepositMonetary", depositMonetary);
        q.setParameter("inCardRecency", cardRecency);
        q.setParameter("inCardFrequency", cardFrequency);
        q.setParameter("inCardMonetary", cardMonetary);
        q.setParameter("inHashTag", "%" + hashTag + "%");
        q.setParameter("inIncome", actualIncome);
        return q.getResultList();
    }

    @Override
    public List<CustomerGroup> getListCustomerGroup() {
        Query q = em.createQuery("Select m from CustomerGroup m");
        return q.getResultList();
    }

    @Override
    public CustomerGroup getCustomerGroup(Long Id) {
        Query q = em.createQuery("Select m from CustomerGroup m Where m.id =:inId");
        q.setParameter("inId", Id);
        return (CustomerGroup) q.getSingleResult();
    }

    @Override
    public CustomerGroup createCustomerGroup(CustomerGroup customerGroup) {
        em.persist(customerGroup);
        return customerGroup;
    }

    @Override
    public CustomerGroup updateCustomerGroup(CustomerGroup customerGroup) {
        em.merge(customerGroup);
        return customerGroup;
    }

}
