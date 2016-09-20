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
import ejb.session.message.ConversationSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.staff.Conversation;
import entity.staff.StaffAccount;
import interceptor.permission.RoleAllowed;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import utils.ColorUtils;
import utils.EnumUtils;
import utils.LoggingUtils;
import utils.RedirectUtils;
import utils.SessionUtils;

@Named(value = "chatViewManagedBean")
@ViewScoped
@RoleAllowed(role = EnumUtils.Permission.WEALTH)
public class ChatViewManagedBean implements Serializable {

    @EJB
    private StaffAccountSessionBeanLocal staffBean;
    @EJB
    private ConversationSessionBeanLocal conversationBean;
    
    
    private String searchText;
    private String conversationId;
    private List<StaffAccount> staffs = new ArrayList<>();
    private List<Conversation> conversations = new ArrayList<>();
    private Conversation currentConversation;
    
    public ChatViewManagedBean() {
        LoggingUtils.StaffMessageLog(ChatViewManagedBean.class, "ChatViewManagedBean() Created");
    }
    
    @PostConstruct
    public void init() {
        LoggingUtils.StaffMessageLog(ChatViewManagedBean.class, "@PostConstruct init() Created");
        showall();
        removeSelf();
        setConversations(conversationBean.getAllConversationForStaff(SessionUtils.getStaffUsername()));
        LoggingUtils.StaffMessageLog(ChatViewManagedBean.class, "Conversations: " + conversations.size());
    }
    
    @PreDestroy 
    public void deinit() {
        LoggingUtils.StaffMessageLog(ChatViewManagedBean.class, "@PreDestroy deinit() Called");
    }
    
    public void search() {
        staffs = staffBean.searchStaffByUsernameOrName(searchText);
        removeSelf();
    }
    
    public void showall() {
        setStaffs(staffBean.getAllStaffs());
        removeSelf();
    }
    
    public void newConversation(StaffAccount staff) {
        LoggingUtils.StaffMessageLog(ChatViewManagedBean.class, "New Conversation with staff " + staff.getUsername());
        StaffAccount sa = SessionUtils.getStaff();
        Conversation conversation = checkIfSenderConversationExists(sa);
        if (conversation == null) {
            conversation = new Conversation();
            conversation.setReceiver(staff);
            conversation.setSender(SessionUtils.getStaff());
            conversationBean.createConversation(conversation);
        }
        setCurrentConversation(conversation);
        // Go to Message View
        Map<String, String> map = new HashMap<>();
        map.put("conversationId", conversation.getId().toString());
        String params = RedirectUtils.generateParameters(map);
        RedirectUtils.redirect("message.xhtml" + params);
    }
    
    private Conversation checkIfSenderConversationExists(StaffAccount sa) {
        List<Conversation> cs = sa.getSenderConversation();
        for (Conversation c : cs) {
            if (c.getSender().equals(SessionUtils.getStaff()) && c.getReceiver().equals(sa)) {
                LoggingUtils.StaffMessageLog(ChatViewManagedBean.class, "Found Existing Conversation, not creating new one");
                return c;
            }
        }
        return null;
    }
    
    public Boolean isReceiver(Conversation conversation) {
        StaffAccount sa = SessionUtils.getStaff();
        for (Conversation c : sa.getReceiverConversation()) {
            if (c.equals(conversation)) {
                return true;
            }
        }
        return false;
    }
    
    public void removeSelf() {
        List<StaffAccount> result = new ArrayList<>();
        StaffAccount self = SessionUtils.getStaff();
        for (StaffAccount sa : staffs) {
            if (!sa.equals(self)) {
                result.add(sa);
            }
        }
        staffs = result;
    }
    
    public String randColor() {
        return ColorUtils.randomColor();
    }
     
    // Getter and Setters
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

    /**
     * @return the conversations
     */
    public List<Conversation> getConversations() {
        return conversations;
    }

    /**
     * @param conversations the conversations to set
     */
    public void setConversations(List<Conversation> conversations) {
        this.conversations = conversations;
    }

    /**
     * @return the currentConversation
     */
    public Conversation getCurrentConversation() {
        return currentConversation;
    }

    /**
     * @param currentConversation the currentConversation to set
     */
    public void setCurrentConversation(Conversation currentConversation) {
        this.currentConversation = currentConversation;
    }

    /**
     * @return the conversationId
     */
    public String getConversationId() {
        return conversationId;
    }

    /**
     * @param conversationId the conversationId to set
     */
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
