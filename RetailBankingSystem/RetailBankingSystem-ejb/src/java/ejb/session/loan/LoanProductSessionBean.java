/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import entity.loan.LoanExternalInterest;
import entity.loan.LoanInterest;
import entity.loan.LoanInterestCollection;
import entity.loan.LoanProduct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author leiyang
 */
@Stateless
public class LoanProductSessionBean implements LoanProductSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public LoanProduct createLoanProduct(LoanProduct loanProduct) {
        em.persist(loanProduct);
        return loanProduct;
    }

    @Override
    public LoanProduct getLoanProductByProductName(String productName) {
        Query q = em.createQuery("SELECT lp FROM LoanProduct lp WHERE lp.productName = :productName");
        q.setParameter("productName", productName);
        return (LoanProduct) q.getSingleResult();
    }

    @Override
    public LoanProduct updateLoanProduct(LoanProduct loanProduct) {
        em.merge(loanProduct);
        return loanProduct;
    }

    @Override
    public LoanExternalInterest createCommonInterest(LoanExternalInterest lci) {
        em.persist(lci);
        return lci;
    }

    @Override
    public LoanExternalInterest getCommonInterestByName(String name) {
        Query q = em.createQuery("SELECT lci FROM LoanCommonInterest lci WHERE lci.name = :name");
        q.setParameter("name", name);
        return (LoanExternalInterest) q.getSingleResult();
    }

    @Override
    public LoanExternalInterest updateCommonInterest(LoanExternalInterest lci) {
        em.merge(lci);
        return lci;
    }
    
    @Override
    public LoanInterest createLoanInterest(LoanInterest li) {
        em.persist(li);
        return li;
    }
    
    @Override
    public LoanInterest updateLoanInterest(LoanInterest li) {
        em.merge(li);
        return li;
    }

    @Override
    public LoanInterestCollection createInterestCollection(LoanInterestCollection lic) {
        em.persist(lic);
        return lic;
    }
    
    @Override
    public LoanInterestCollection updateInterestCollection(LoanInterestCollection lic) {
        em.merge(lic);
        return lic;
    }
}
