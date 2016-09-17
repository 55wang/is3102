/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.message;

import entity.staff.Announcement;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface AnnouncementSessionBeanLocal {
    public Boolean createAnnouncement(Announcement a);
    public List<Announcement> getAllAnnouncements();
}
