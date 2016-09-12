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
import entity.Message;
import org.primefaces.push.EventBus;
import org.primefaces.push.RemoteEndpoint;
import org.primefaces.push.annotation.OnClose;
import org.primefaces.push.annotation.OnMessage;
import org.primefaces.push.annotation.OnOpen;
import org.primefaces.push.annotation.PathParam;
import org.primefaces.push.annotation.PushEndpoint;
 
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.atmosphere.config.service.Singleton;
 
@PushEndpoint("/chat/{receiver}")
@Singleton
public class ChatResource {
 
    @PathParam("receiver")
    private String receiver;
 
    @Inject
    private ServletContext ctx;
 
    @OnOpen
    public void onOpen(RemoteEndpoint r, EventBus eventBus) {
        // User Online
        System.out.println(String.format("ChatResource: @OnOpen: Receiver is %s", receiver));
    }
 
    @OnClose
    public void onClose(RemoteEndpoint r, EventBus eventBus) {
        // User Offline
        System.out.println(String.format("ChatResource: @OnClose: Receiver is %s", receiver));
    }
 
    // Decoder/Encoder Intercept
    @OnMessage(decoders = {MessageDecoder.class}, encoders = {MessageEncoder.class})
    public MessageDTO onMessage(MessageDTO message) {
        // In the middle of sending message
        System.out.println(String.format("ChatResource: @OnMessage: Receiver is %s", receiver));
        System.out.println(String.format("ChatResource: @OnMessage: Message is %s", message.getMessage()));
        return message;
    }
 
}