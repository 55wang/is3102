/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.message;

import ejb.session.message.AnnouncementSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.Announcement;
import entity.StaffAccount;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Named;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.view.ViewScoped;
import org.apache.commons.lang.StringEscapeUtils;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import utils.MessageUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "notificationViewManagedBean")
@ViewScoped
public class NotificationViewManagedBean implements Serializable {

    @EJB
    private AnnouncementSessionBeanLocal announcementBean;

    private final static String NOTIFY_CHANNEL = "/notify";

    private Announcement newAnnouncement = new Announcement();
    private List<Announcement> announcements = new ArrayList<>();
    

    /**
     * Creates a new instance of NotificationViewManagedBean
     */
    public NotificationViewManagedBean() {
    }

    @PostConstruct
    public void init() {
        announcements = announcementBean.getAllAnnouncements();
    }

    public void send() {
        if (announcementBean.createAnnouncement(getNewAnnouncement())) {
            MessageUtils.displayInfo("New Announcement Added");
        } else {
            MessageUtils.displayInfo("Announcement already Added");
        }
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        FacesMessage m = new FacesMessage(StringEscapeUtils.escapeHtml(newAnnouncement.getTitle()), StringEscapeUtils.escapeHtml(newAnnouncement.getContent()));
        eventBus.publish(NOTIFY_CHANNEL, m);

        setNewAnnouncement(new Announcement());
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

    /**
     * @return the newAnnouncement
     */
    public Announcement getNewAnnouncement() {
        return newAnnouncement;
    }

    /**
     * @param newAnnouncement the newAnnouncement to set
     */
    public void setNewAnnouncement(Announcement newAnnouncement) {
        this.newAnnouncement = newAnnouncement;
    }
}
