/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.message;

import ejb.session.message.AnnouncementSessionBeanLocal;
import entity.staff.Announcement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import javax.faces.view.ViewScoped;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "notificationViewCounterManagedBean")
@ViewScoped
public class NotificationViewCounterManagedBean implements Serializable {

    @EJB
    private AnnouncementSessionBeanLocal announcementBean;
    
    private List<Announcement> announcements = new ArrayList<>();
    /**
     * Creates a new instance of NotificationViewManagedBean
     */
    public NotificationViewCounterManagedBean() {
    }

    @PostConstruct
    public void init() {
        announcements = announcementBean.getAllAnnouncements(SessionUtils.getStaff().getRoles(), true);
    }

    // Getter and Setters
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
