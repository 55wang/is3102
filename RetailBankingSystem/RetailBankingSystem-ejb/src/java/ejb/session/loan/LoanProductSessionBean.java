/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.loan;

import entity.loan.LoanCommonInterest;
import entity.loan.LoanInterest;
import entity.loan.LoanProduct;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author qiuxiaqing
 */
@Stateless
public class LoanProductSessionBean implements LoanProductSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public LoanProduct createLoanProduct(LoanProduct loanProduct) {

        Query q = em.createQuery("SELECT lp FROM LoanProduct lp WHERE lp.productName = :productName");
        q.setParameter("productName", loanProduct.getProductName());
        System.out.println("createLoanProduct: " + loanProduct.getProductName());

        if (q.getResultList().isEmpty()) {
            try {

                for (LoanInterest interest : loanProduct.getLoanInterests()) {
                    interest.setLoanProduct(loanProduct);
                    em.persist(interest);
                }
                em.persist(loanProduct);
                System.out.println("createLoanProduct after persisit: " + loanProduct.getProductName());
                return loanProduct;
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }

    }

    @Override
    public LoanProduct getLoanProductByProductName(String productName) {
        Query q = em.createQuery("SELECT lp FROM LoanProduct lp WHERE lp.productName = :productName");

        q.setParameter("productName", productName);

        try {
            return (LoanProduct) q.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public LoanProduct updateLoanProduct(LoanProduct loanProduct) {

        try {

            for (LoanInterest interest : loanProduct.getLoanInterests()) {
                interest.setLoanProduct(loanProduct);
                em.merge(interest);
            }
            em.merge(loanProduct);

            return loanProduct;
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public LoanCommonInterest createCommonInterest(LoanCommonInterest lci) {
        try {
            em.persist(lci);
            return lci;
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public LoanCommonInterest getCommonInterestByName(String name) {
        Query q = em.createQuery("SELECT lci FROM LoanCommonInterest lci WHERE lci.name = :name");

        q.setParameter("name",name);

        try {
            return (LoanCommonInterest) q.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
    

    @Override
    public LoanCommonInterest updateCommonInterest(LoanCommonInterest lci) {

        try {
            em.merge(lci);
            return lci;
        } catch (Exception e) {
            return null;
        }

    }

}
