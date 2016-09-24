/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.dams.account.Cheque;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author leiyang
 */
@Stateless
public class CurrentAccountChequeSessionBean implements CurrentAccountChequeSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public List<Cheque> getChequeByMainAccountId(Long mainAccountId) {
        Query q = em.createQuery("SELECT c FROM Cheque c WHERE c.account.mainAccount.id = :mainAccountId");
        q.setParameter("mainAccountId", mainAccountId);
        return q.getResultList();
    }
    
    @Override
    public Cheque createCheque(Cheque c) {
        em.persist(c);
        return c;
    }
    
    @Override
    public Cheque updateCheque(Cheque c) {
        em.merge(c);
        return c;
    }
}
