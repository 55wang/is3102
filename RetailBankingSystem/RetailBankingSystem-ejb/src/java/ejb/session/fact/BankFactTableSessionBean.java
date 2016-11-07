/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.fact;

import entity.fact.bank.BankFactTable;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wang
 */
@Stateless
public class BankFactTableSessionBean implements BankFactTableSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<BankFactTable> getListBankFactTables() {
        Query q = em.createQuery("SELECT r FROM BankFactTable r ORDER BY r.creationDate");
        return q.getResultList();
    }

    @Override
    public BankFactTable createBankFactTable(BankFactTable r) {
        em.persist(r);
        return r;
    }

    @Override
    public BankFactTable updateBankFactTable(BankFactTable r) {
        em.merge(r);
        return r;
    }

    @Override
    public BankFactTable getBankFactTableById(Long Id) {
        return em.find(BankFactTable.class, Id);
    }

    @Override
    public BankFactTable getBankFactTableByCreationDate(Date date) {
        try{
            Query q = em.createQuery("SELECT r FROM BankFactTable r WHERE r.creationDate =:inDate");
            q.setParameter("inDate", date);
            return (BankFactTable) q.getSingleResult();
        }catch(Exception ex){
            return new BankFactTable();
        }
    }

}
