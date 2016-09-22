/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.message;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.atmosphere.config.service.Singleton;
import org.primefaces.push.EventBus;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.OnClose;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.OnOpen;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.impl.JSONEncoder;

/**
 *
 * @author leiyang
 */
@PushEndpoint("/role_notification/{role}")
@Singleton
public class RoleNotificationResource {
    @PathParam("role")
    private String role;
 
    @Inject
    private ServletContext ctx;
 
    @OnOpen
    public void onOpen(RemoteEndpoint r, EventBus eventBus) {
        // User Online
        System.out.println(String.format("RoleNotificationResource: @OnOpen: Receiver is %s", role));
    }
 
    @OnClose
    public void onClose(RemoteEndpoint r, EventBus eventBus) {
        // User Offline
        System.out.println(String.format("RoleNotificationResource: @OnClose: Receiver is %s", role));
    }
 
    // Decoder/Encoder Intercept
    @OnMessage(encoders = {JSONEncoder.class})
    public FacesMessage onMessage(FacesMessage message) {
        return message;
    }
}
