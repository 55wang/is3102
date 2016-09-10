/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.message;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

import javax.faces.application.FacesMessage;
import org.apache.commons.lang.StringEscapeUtils;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

/**
 *
 * @author leiyang
 */
@Named(value = "notificationViewManagedBean")
@ViewScoped
public class NotificationViewManagedBean  implements Serializable {

    private final static String CHANNEL = "/notify";
     
    private String summary;
     
    private String detail;
    /**
     * Creates a new instance of NotificationViewManagedBean
     */
    public NotificationViewManagedBean() {
    }
     
    public void send() {
        EventBus eventBus = EventBusFactory.getDefault().eventBus();
        eventBus.publish(CHANNEL, new FacesMessage(StringEscapeUtils.escapeHtml(summary), StringEscapeUtils.escapeHtml(detail)));
    }
    
    // Getter and Setters
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
     
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
}
