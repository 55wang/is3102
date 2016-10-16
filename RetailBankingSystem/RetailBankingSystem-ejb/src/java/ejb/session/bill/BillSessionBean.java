/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bill;

import entity.bill.BankEntity;
import entity.bill.Organization;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils.StatusType;

/**
 *
 * @author leiyang
 */
@Stateless
public class BillSessionBean implements BillSessionBeanLocal {
    
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public Organization createOrganization(Organization o) {
        em.persist(o);
        return o;
    }

    @Override
    public Organization updateOrganization(Organization o) {
        em.merge(o);
        return o;
    }
    
    @Override
    public BankEntity createBankEntity(BankEntity o) {
        em.persist(o);
        return o;
    }

    @Override
    public BankEntity updateBankEntity(BankEntity o) {
        em.merge(o);
        return o;
    }
    
    @Override
    public BankEntity getBankEntityByCode(String code){
        Query q = em.createQuery("SELECT b FROM BankEntity b WHERE b.status = :inStatus AND b.bankCode =:code");
        q.setParameter("inStatus", StatusType.ACTIVE);
        q.setParameter("code", code);
        return (BankEntity)q.getSingleResult();
    }
    
    @Override
    public BankEntity getBankEntityById(Long id){
        return em.find(BankEntity.class, id);
    }
    
    @Override
    public List<Organization> getActiveListOrganization() {
        Query q = em.createQuery("SELECT o FROM Organization o WHERE o.status = :inStatus");
        q.setParameter("inStatus", StatusType.ACTIVE);
        return q.getResultList();
    }
    
    @Override
    public List<BankEntity> getActiveListBankEntities(){
        Query q = em.createQuery("SELECT b FROM BankEntity b WHERE b.status = :inStatus");
        q.setParameter("inStatus", StatusType.ACTIVE);
        return q.getResultList();
    }
}
