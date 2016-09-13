/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.message;

/**
 *
 * @author leiyang
 */
import org.primefaces.push.EventBus;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.OnClose;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.OnOpen;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;
import org.primefaces.push.annotation.Singleton;
 
import javax.inject.Inject;
import javax.servlet.ServletContext;
 
@PushEndpoint("/{room}/{user}")
@Singleton
public class ChatResource {
 
    @PathParam("room")
    private String room;
 
    @PathParam("user")
    private String username;
 
    @Inject
    private ServletContext ctx;
 
    @OnOpen
    public void onOpen(RemoteEndpoint r, EventBus eventBus) {
        // User Online
        System.out.println(String.format("ChatResource: @OnOpen: Room is %s and user is %s", room, username));
        eventBus.publish(room + "/*", new Message(String.format("%s has entered the room '%s'",  username, room), true));
    }
 
    @OnClose
    public void onClose(RemoteEndpoint r, EventBus eventBus) {
        // User Offline
        System.out.println(String.format("ChatResource: @OnClose: Room is %s and user is %s", room, username));
        eventBus.publish(room + "/*", new Message(String.format("%s has left the room '%s'",  username, room), true));
    }
 
    // Decoder/Encoder Intercept
    @OnMessage(decoders = {MessageDecoder.class}, encoders = {MessageEncoder.class})
    public Message onMessage(Message message) {
        // In the middle of sending message
        System.out.println(String.format("ChatResource: @OnMessage: Room is %s and user is %s", room, username));
        System.out.println(String.format("ChatResource: @OnMessage: Message is %s", message.getText()));
        return message;
    }
 
}