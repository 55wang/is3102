/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.message;

import entity.Announcement;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    public List<Announcement> getAllAnnouncements() {
        Query q = em.createQuery("SELECT a FROM Announcement a ORDER BY a.creationDate DESC");
        return q.getResultList();
    }
}
