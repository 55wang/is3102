/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bill;

import entity.bill.BankEntity;
import entity.bill.BillingOrg;
import entity.bill.GiroArrangement;
import entity.bill.Organization;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils;
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
    public Organization getOrganizationById(Long id) {
        return em.find(Organization.class, id);
    }

    @Override
    public List<Organization> getActiveListOrganization() {
        Query q = em.createQuery("SELECT o FROM Organization o WHERE o.status = :inStatus AND o.type !=:inType");
        q.setParameter("inStatus", StatusType.ACTIVE);
        q.setParameter("inType", EnumUtils.BillType.CARD);
        return q.getResultList();
    }

    @Override
    public List<Organization> getCreditCardOrganization() {
        Query q = em.createQuery("SELECT o FROM Organization o WHERE o.status = :inStatus AND o.type =:inType");
        q.setParameter("inStatus", StatusType.ACTIVE);
        q.setParameter("inType", EnumUtils.BillType.CARD);
        return q.getResultList();
    }

    // bank entity
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
    public BankEntity getBankEntityByCode(String code) {
        Query q = em.createQuery("SELECT b FROM BankEntity b WHERE b.status = :inStatus AND b.bankCode =:code");
        q.setParameter("inStatus", StatusType.ACTIVE);
        q.setParameter("code", code);
        return (BankEntity) q.getSingleResult();
    }

    @Override
    public BankEntity getBankEntityById(Long id) {
        return em.find(BankEntity.class, id);
    }

    @Override
    public List<BankEntity> getActiveListBankEntities() {
        Query q = em.createQuery("SELECT b FROM BankEntity b WHERE b.status = :inStatus");
        q.setParameter("inStatus", StatusType.ACTIVE);
        return q.getResultList();
    }

    // bill org
    @Override
    public BillingOrg createBillingOrganization(BillingOrg o) {
        em.persist(o);
        return o;
    }

    @Override
    public BillingOrg getBillingOrganizationById(Long id) {
        return em.find(BillingOrg.class, id);
    }

    @Override
    public String deleteBillingOrganizationById(Long id) {
        BillingOrg bo = getBillingOrganizationById(id);
        em.remove(bo);
        return "SUCCESS";
    }

    @Override
    public List<BillingOrg> getBillingOrgMainAccountId(Long id) {
        Query q = em.createQuery("SELECT bo FROM BillingOrg bo WHERE bo.mainAccount.id =:mainAccountId AND bo.organization.type !=:inType");
        q.setParameter("mainAccountId", id);
        q.setParameter("inType", EnumUtils.BillType.CARD);
        return q.getResultList();
    }

    @Override
    public List<BillingOrg> getCreditCardBillingMainAccountId(Long id) {
        Query q = em.createQuery("SELECT bo FROM BillingOrg bo WHERE bo.mainAccount.id =:mainAccountId AND bo.organization.type =:inType");
        q.setParameter("mainAccountId", id);
        q.setParameter("inType", EnumUtils.BillType.CARD);
        return q.getResultList();
    }

    // giro
    @Override
    public GiroArrangement createGiroArr(GiroArrangement o) {
        em.persist(o);
        return o;
    }

    @Override
    public GiroArrangement updateGiroArr(GiroArrangement o) {
        em.merge(o);
        return o;
    }

    @Override
    public GiroArrangement getGiroArrById(Long id) {
        return em.find(GiroArrangement.class, id);
    }

    @Override
    public GiroArrangement getGiroArrByReferenceNumberAndOrgCode(String referenceNumber, String shortCode) {
        Query q = em.createQuery("SELECT ga FROM GiroArrangement ga WHERE ga.billReference =:referenceNumber AND ga.organization.shortCode =:shortCode");
        q.setParameter("referenceNumber", referenceNumber);
        q.setParameter("shortCode", shortCode);
        try {
            return (GiroArrangement) q.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String deleteGiroArrById(Long id) {
        GiroArrangement ga = getGiroArrById(id);
        em.remove(ga);
        return "SUCCESS";
    }

    @Override
    public List<GiroArrangement> getGiroArrsByMainAccountId(Long id) {
        Query q = em.createQuery("SELECT ga FROM GiroArrangement ga WHERE ga.mainAccount.id =:mainAccountId");
        q.setParameter("mainAccountId", id);
        return q.getResultList();
    }

}
