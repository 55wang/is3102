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
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.staff.Conversation;
import entity.staff.StaffAccount;
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
import server.utilities.ColorUtils;
import server.utilities.LoggingUtils;
import utils.RedirectUtils;
import utils.SessionUtils;

@Named(value = "chatViewManagedBean")
@ViewScoped
public class ChatViewManagedBean implements Serializable {

    @EJB
    private StaffAccountSessionBeanLocal staffBean;
    @EJB
    private ConversationSessionBeanLocal conversationBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;

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
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter ChatViewManagedBean");
        a.setFunctionName("ChatViewManagedBean @PostConstruct init()");
        a.setInput("Getting all ChatViewManagedBean");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
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
        String conversationId = conversationBean.checkIfConversationExists(sa, staff);

        System.out.println(conversationId);
        if (conversationId.equals("NOT_FOUND") || conversationId.equals("EXCEPTION")) {
            Conversation conversation = new Conversation();
            conversation.setReceiver(staff);
            conversation.setSender(SessionUtils.getStaff());
            conversation = conversationBean.createConversation(conversation);
            setCurrentConversation(conversation);
            conversationId = conversation.getId().toString();
        }

        // Go to Message View
        Map<String, String> map = new HashMap<>();
        map.put("conversationId", conversationId);
        String params = RedirectUtils.generateParameters(map);
        RedirectUtils.redirect("message.xhtml" + params);
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
