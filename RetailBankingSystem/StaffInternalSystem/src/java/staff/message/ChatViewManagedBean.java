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
import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.StaffAccount;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import javax.faces.view.ViewScoped;
import utils.SessionUtils;

@Named(value = "chatViewManagedBean")
@ViewScoped
public class ChatViewManagedBean implements Serializable {

    @EJB
    private StaffAccountSessionBeanLocal staffBean;
    
    private final EventBus eventBus = EventBusFactory.getDefault().eventBus();
    private String newMessage;
    private String searchText;
    private String username = SessionUtils.getStaffUsername();
    private final boolean loggedIn = SessionUtils.loggedIn();
    private final static String CHANNEL = "/{room}/";
    private List<StaffAccount> staffs = new ArrayList<>();
    
    public ChatViewManagedBean() {
        System.out.println("ChatViewManagedBean() Created");
    }
    
    @PostConstruct
    public void init() {
        System.out.println("@PostConstruct init() Created");
        setStaffs(staffBean.getAllStaffs());
        // conversation list
    }
    
    public void search() {
        // TODO:
    }
    
    public void newConversation(StaffAccount staff) {
        System.out.println("New Conversation with staff " + staff.getUsername());
    }
    
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

    /**
     * @return the staffs
     */
    public List<StaffAccount> getStaffs() {
        return staffs;
    }

    /**
     * @param staffs the staffs to set
     */
    public void setStaffs(List<StaffAccount> staffs) {
        this.staffs = staffs;
    }

    /**
     * @return the searchText
     */
    public String getSearchText() {
        return searchText;
    }

    /**
     * @param searchText the searchText to set
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
