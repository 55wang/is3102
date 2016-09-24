/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.message;

import javax.faces.application.FacesMessage;
import org.primefaces.push.EventBus;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.OnClose;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.OnOpen;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.annotation.Singleton;
import org.primefaces.push.impl.JSONEncoder;

/**
 *
 * @author leiyang
 */
@PushEndpoint("/customer_notify")
@Singleton
public class CustomerAnnouncementResource {
    
    @PathParam("customer")
    private String customer;
    
    @OnOpen
    public void onOpen(RemoteEndpoint r, EventBus eventBus) {
        // User Online
        System.out.println("CustomerAnnouncementResource: @OnOpen:");
    }
 
    @OnClose
    public void onClose(RemoteEndpoint r, EventBus eventBus) {
        // User Offline
        System.out.println("CustomerAnnouncementResource: @OnClose:");
    }
    
    @OnMessage(encoders = {JSONEncoder.class})
    public FacesMessage onMessage(FacesMessage message) {
        return message;
    }
}
