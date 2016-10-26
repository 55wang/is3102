/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.message;

import ejb.session.message.AnnouncementSessionBeanLocal;
import ejb.session.staff.StaffRoleSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.staff.Announcement;
import entity.staff.Role;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import org.apache.commons.lang.StringEscapeUtils;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "notificationViewManagedBean")
@ViewScoped
public class NotificationViewManagedBean implements Serializable {

    @EJB
    private AnnouncementSessionBeanLocal announcementBean;
    @EJB
    private StaffRoleSessionBeanLocal staffRoleSessionBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;

    private final static String STAFF_NOTIFY_CHANNEL = "/staff_notify";
    private final static String CUSTOMER_NOTIFY_CHANNEL = "/customer_notify";
    private final static String ROLE_NOTIFY_CHANNEL = "/role_notification/";

    private String annocementTarget = "forStaff";
    private String annocementForStaff = "forStaff";
    private String annocementForCustomer = "forCustomer";
    private Boolean forAllStaff = true;
    private Boolean forAllCustomer = true;
    private Announcement newAnnouncement = new Announcement();
    private List<Announcement> announcements = new ArrayList<>();
    private List<Role> roles = new ArrayList<>();
    private Map<String, String> rolesOption = new HashMap<>();
    private String selectedRoleName;
    /**
     * Creates a new instance of NotificationViewManagedBean
     */
    public NotificationViewManagedBean() {
    }

    @PostConstruct
    public void init() {
        announcements = announcementBean.getAllAnnouncements(SessionUtils.getStaff().getRoles(), true);
        setRoles(staffRoleSessionBean.getAllRoles());
        for (Role r : roles) {
            rolesOption.put(r.getRoleName(), r.getRoleName());
        }
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter NotificationViewManagedBean");
        a.setFunctionName("NotificationViewManagedBean @PostConstruct init()");
        a.setFunctionInput("Getting all NotificationViewManagedBean information");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }

    public void send() { 
        
        if (isForStaff() && !forAllStaff && selectedRoleName!= null) {
            newAnnouncement.setRole(staffRoleSessionBean.findRoleByName(selectedRoleName));
        }
            
        newAnnouncement.setIsForStaff(isForStaff());   

        
        if (announcementBean.createAnnouncement(getNewAnnouncement())) {
            announcements.add(0, newAnnouncement);
            MessageUtils.displayInfo("New Announcement Added");
        } else {
            MessageUtils.displayInfo("Announcement already Added");
        }
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        FacesMessage m = new FacesMessage(StringEscapeUtils.escapeHtml(newAnnouncement.getTitle()), StringEscapeUtils.escapeHtml(newAnnouncement.getContent()));
        
        if (!isForStaff()) {
            System.out.println("Runs here and pushed to:" + CUSTOMER_NOTIFY_CHANNEL);
            eventBus.publish(CUSTOMER_NOTIFY_CHANNEL, m);
//            eventBus.publish(ROLE_NOTIFY_CHANNEL + UserRole.SUPER_ADMIN.toString(), m);
        } else {
            if (getForAllStaff() == false) {
                eventBus.publish(ROLE_NOTIFY_CHANNEL + selectedRoleName, m);
            } else {
                eventBus.publish(STAFF_NOTIFY_CHANNEL, m);
            }
        }

        setNewAnnouncement(new Announcement());
        selectedRoleName = null;
    }
    
    public Boolean isForStaff(){
        if(annocementTarget.equals(annocementForStaff))
            return true;
        else
            return false;
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

    /**
     * @return the roles
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
     * @return the rolesOption
     */
    public Map<String, String> getRolesOption() {
        return rolesOption;
    }

    /**
     * @param rolesOption the rolesOption to set
     */
    public void setRolesOption(Map<String, String> rolesOption) {
        this.rolesOption = rolesOption;
    }

    /**
     * @return the selectedRoleName
     */
    public String getSelectedRoleName() {
        return selectedRoleName;
    }

    /**
     * @param selectedRoleName the selectedRoleName to set
     */
    public void setSelectedRoleName(String selectedRoleName) {
        this.selectedRoleName = selectedRoleName;
    }

    /**
     * @return the forAllStaff
     */
    public Boolean getForAllStaff() {
        return forAllStaff;
    }

    /**
     * @param forAllStaff the forAllStaff to set
     */
    public void setForAllStaff(Boolean forAllStaff) {
        this.forAllStaff = forAllStaff;
    }

    public String getAnnocementTarget() {
        return annocementTarget;
    }

    public void setAnnocementTarget(String annocementTarget) {
        this.annocementTarget = annocementTarget;
    }

    public String getAnnocementForStaff() {
        return annocementForStaff;
    }

    public void setAnnocementForStaff(String annocementForStaff) {
        this.annocementForStaff = annocementForStaff;
    }

    public String getAnnocementForCustomer() {
        return annocementForCustomer;
    }

    public void setAnnocementForCustomer(String annocementForCustomer) {
        this.annocementForCustomer = annocementForCustomer;
    }

    public Boolean getForAllCustomer() {
        return forAllCustomer;
    }

    public void setForAllCustomer(Boolean forAllCustomer) {
        this.forAllCustomer = forAllCustomer;
    }
    
}
