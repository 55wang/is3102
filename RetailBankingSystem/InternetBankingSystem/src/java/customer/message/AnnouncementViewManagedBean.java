/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.message;

import ejb.session.message.AnnouncementSessionBeanLocal;
import entity.staff.Announcement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author leiyang
 */
@Named(value = "announcementViewManagedBean")
@ViewScoped
public class AnnouncementViewManagedBean implements Serializable {

    @EJB
    private AnnouncementSessionBeanLocal announcementBean;
    private List<Announcement> announcements = new ArrayList<>();
    /**
     * Creates a new instance of AnnouncementViewManagedBean
     */
    public AnnouncementViewManagedBean() {
        
    }
    @PostConstruct
    public void init() {
        setAnnouncements(announcementBean.getAllAnnouncements(null));
    }

    /**
     * @return the announcements
     */
    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    /**
     * @param announcements the announcements to set
     */
    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
    }
    
}
