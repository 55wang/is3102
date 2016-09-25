/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.audit;

import entity.common.AuditLog;
import entity.customer.MainAccount;
import entity.staff.StaffAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wang
 */
@Stateless
public class AuditSessionBean implements AuditSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public List<AuditLog> getAuditLogByCustomerID(String userID) {
        Query q = em.createQuery("SELECT a FROM MainAccount a WHERE a.userID = :inUserID");

        q.setParameter("inUserID", userID);

        MainAccount mainAccount = null;

        try {
            mainAccount = (MainAccount) q.getSingleResult();
            em.refresh(mainAccount);
            return mainAccount.getAuditLog();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<AuditLog> getAuditLogByStaffUsername(String username) {
        
        Query q = em.createQuery("SELECT a FROM AuditLog a WHERE a.staffAccount.username = :username");

        q.setParameter("username", username);
        
        return q.getResultList();
    }

    @Override
    public Boolean insertAuditLog(AuditLog auditLog) {
        try {
            em.merge(auditLog);
            em.flush();
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }
    
}
