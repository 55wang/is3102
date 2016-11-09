/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.message.AnnouncementSessionBeanLocal;
import entity.staff.Announcement;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author VIN-S
 */
@Stateless
@LocalBean
public class EntityAnnouncementBuilderBean {
    @EJB
    private AnnouncementSessionBeanLocal announcementSessionBean;
    
    public void initAnnouncement() {
        Announcement a = new Announcement();
        a.setTitle("MBS Credit Card Promotion");
        a.setContent("Dear Customer, we are pleased to tell you that we are going to introduce a new product, namely MBS Gold Card, in the end of this year."
                + "The new product will give more benefits in managing your credit transaction and investment."
                + "If you are interested, please pay attention to our announcement. Thanks!");
        a.setIsForStaff(Boolean.FALSE);
        a.setCreationDate(new Date());
        announcementSessionBean.createAnnouncement(a);
    }
}
