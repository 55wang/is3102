/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.message;

import entity.staff.Announcement;
import entity.staff.Role;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils.UserRole;

/**
 *
 * @author leiyang
 */
@Stateless
public class AnnouncementSessionBean implements AnnouncementSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public Boolean createAnnouncement(Announcement a) {
        try {
            em.persist(a);
            return true;
        } catch (EntityExistsException e) {
            return false;
        }
    }

    @Override
    public List<Announcement> getAllAnnouncements(List<Role> roles, Boolean isforStaff) {
        Query q = null;
        // customer side
        if (roles == null) {
            if (isforStaff) {
                q = em.createQuery("SELECT a FROM Announcement a WHERE "
                        + "a.isForStaff = true ORDER BY a.creationDate DESC"
                );
            } else {
                q = em.createQuery("SELECT a FROM Announcement a WHERE "
                        + "a.isForStaff = false ORDER BY a.creationDate DESC"
                );
            }

        } else {
            Boolean isSuperAdmin = false;
            for (Role r : roles) {
                if (r.getRoleName().equals(UserRole.SUPER_ADMIN.toString())) {
                    isSuperAdmin = true;
                }
            }
            if (isSuperAdmin) {
                // retrieve all
                q = em.createQuery("SELECT a FROM Announcement a ORDER BY a.creationDate DESC");
            } else {
                String queryString = "SELECT a FROM Announcement a WHERE a.isForStaff = true AND (";
                for (int i = 0; i < roles.size(); i++) {
                    if (i == 0) {
                        queryString += " a.role.roleName = " + roles.get(i).getRoleName();
                    } else {
                        queryString += " OR a.role.roleName = " + roles.get(i).getRoleName();
                    }
                }
                queryString += ") ORDER BY a.creationDate DESC";
                q = em.createQuery(queryString);
            }
        }

        return q.getResultList();
    }
}
