/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.audit;

import entity.AuditLog;
import entity.MainAccount;
import entity.StaffAccount;
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

    public List<AuditLog> getAuditLogByCustomerID(String userID) {
        Query q = em.createQuery("SELECT a FROM MainAccount a WHERE a.userID = :inUserID");

        q.setParameter("inUserID", userID);

        MainAccount mainAccount = null;

        try {
            mainAccount = (MainAccount) q.getSingleResult();
            return mainAccount.getAuditLog();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public List<AuditLog> getAuditLogByStaffID(String staffID) {
        Query q = em.createQuery("SELECT a FROM StaffAccount a WHERE a.username = :inUserID");

        q.setParameter("inUserID", staffID);

        StaffAccount staffAccount = null;

        try {
            staffAccount = (StaffAccount) q.getSingleResult();
            return staffAccount.getAuditLog();
        } catch (NoResultException ex) {
            return null;
        }
    }

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
