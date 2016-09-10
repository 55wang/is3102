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
import javax.inject.Named;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import javax.faces.view.ViewScoped;
import utils.SessionUtils;

@Named(value = "chatViewManagedBean")
@ViewScoped
public class ChatViewManagedBean implements Serializable {

    /**
     * Creates a new instance of ChatViewManagedBean
     */
    public ChatViewManagedBean() {
        System.out.println("ChatViewManagedBean() Created");
    }
    
    @PostConstruct
    public void init() {
        System.out.println("@PostConstruct init() Created");
    }
    
    // TODO: PushContext for observer pattern
    //private final PushContext pushContext = PushContextFactory.getDefault().getPushContext();
 
//    private final EventBus eventBus = EventBusFactory.getDefault().eventBus();
 
    // TODO: Conversations list
    // Current messages list
    // current new message
    // Get update
    private String newMessage;
     
    private String username = SessionUtils.getStaffUsername();
     
    private final boolean loggedIn = SessionUtils.loggedIn();
     
    private final static String CHANNEL = "/{room}/";
    
    public void sendMessage() {
        // Send message action
        System.out.println(String.format("ChatViewManagedBean: SendMessage(): User is %s and newMessage is %s", username, newMessage));
//        if (eventBus != null) {
//            eventBus.publish(CHANNEL + "Conversation_ID", "[PM] " + username + ": " + newMessage);
//        }
    }
     
    // Getter and Setters
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
     
    public boolean isLoggedIn() {
        return loggedIn;
    }

    /**
     * @return the newMessage
     */
    public String getNewMessage() {
        return newMessage;
    }

    /**
     * @param newMessage the newMessage to set
     */
    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }
}
