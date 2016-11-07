/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.message;

import javax.faces.application.FacesMessage;
import org.primefaces.push.EventBus;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.OnClose;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.OnOpen;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.annotation.Singleton;
import org.primefaces.push.impl.JSONEncoder;
/**
 *
 * @author leiyang
 */
@PushEndpoint("/staff_notify")
@Singleton
public class NotificationResource {
    @OnOpen
    public void onOpen(RemoteEndpoint r, EventBus eventBus) {
        // User Online
        System.out.println("NotificationResource: @OnOpen:");
    }
 
    @OnClose
    public void onClose(RemoteEndpoint r, EventBus eventBus) {
        // User Offline
        System.out.println("NotificationResource: @OnClose:");
    }
    
    @OnMessage(encoders = {JSONEncoder.class})
    public FacesMessage onMessage(FacesMessage message) {
        return message;
    }
}
