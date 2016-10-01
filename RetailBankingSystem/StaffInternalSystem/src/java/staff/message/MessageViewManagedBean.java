/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.message;

import ejb.session.message.ConversationSessionBeanLocal;
import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.staff.Conversation;
import entity.staff.Message;
import entity.staff.StaffAccount;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import server.utilities.ColorUtils;
import utils.JSUtils;
import server.utilities.LoggingUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "messageViewManagedBean")
@ViewScoped
public class MessageViewManagedBean implements Serializable {

    @EJB
    private ConversationSessionBeanLocal conversationBean;
    @EJB
    private UtilsSessionBeanLocal utilsBean;

    private final EventBus eventBus = EventBusFactory.getDefault().eventBus();
    private final static String CHANNEL = "/chat/";

    private String conversationId;
    private Conversation currentConversation;
    private Message newMessage = new Message();
    private String senderColor = randColor();
    private String receiverColor = randColor();

    public MessageViewManagedBean() {
    }
    
    public void init() {
        LoggingUtils.StaffMessageLog(MessageViewManagedBean.class, "@PostConstruct, retriving conversation from id:" + conversationId);
        setCurrentConversation(conversationBean.getConversationById(Long.parseLong(conversationId)));
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter MessageViewManagedBean");
        a.setFunctionName("MessageViewManagedBean @PostConstruct init()");
        a.setInput("Getting all MessageViewManagedBean information");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }

    public void sendMessage() {
        // Send message action
        newMessage.setReceiver(getReceiverUsername());
        newMessage.setSender(SessionUtils.getStaffUsername());
        LoggingUtils.StaffMessageLog(MessageViewManagedBean.class, String.format("MessageViewManagedBean: SendMessage(): newMessage is %s", getNewMessage()));
        currentConversation.setUnread(Boolean.TRUE);
        currentConversation.setLastMessage(newMessage.getMessage());
        if (conversationBean.addMessage(currentConversation, newMessage)) {
            LoggingUtils.StaffMessageLog(MessageViewManagedBean.class, "Created New Message:" + newMessage.getMessage() + " And send to channel: " + CHANNEL + getReceiverUsername());
            MessageDTO mDTO = new MessageDTO();
            mDTO.setCreateDate(newMessage.getCreateDate().toString());
            mDTO.setLabel(getMessageLabel(newMessage));
            mDTO.setMessage(newMessage.getMessage());
            mDTO.setSenderName(SessionUtils.getStaff().getFullName());
            mDTO.setConversationId(conversationId);
            eventBus.publish(CHANNEL + getReceiverUsername(), mDTO);
            newMessage = new Message();
            init();
            JSUtils.callJSMethod("scrollDown()");
        } else {
            LoggingUtils.StaffMessageLog(MessageViewManagedBean.class, "Created New Message FAILED");
        }
    }

    public String getReceiverUsername() {
        if (currentConversation.getReceiver().getUsername().equals(SessionUtils.getStaffUsername())) {
            return currentConversation.getSender().getUsername();
        } else {
            return currentConversation.getReceiver().getUsername();
        }
    }

    public String getMessageLabel(Message m) {
        if (currentConversation.getReceiver().getUsername().equals(SessionUtils.getStaffUsername())) {
            return isReceiver(m) ? currentConversation.getSender().getNameLabel() : currentConversation.getReceiver().getNameLabel();
        } else {
            return isReceiver(m) ? currentConversation.getReceiver().getNameLabel() : currentConversation.getSender().getNameLabel();
        }
    }

    public Boolean isReceiver(Message m) {
        StaffAccount sa = SessionUtils.getStaff();
        return sa.getUsername().equals(m.getReceiver());
    }
    
    public String randColor() {
        return ColorUtils.randomColor();
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

    /**
     * @return the newMessage
     */
    public Message getNewMessage() {
        return newMessage;
    }

    /**
     * @param newMessage the newMessage to set
     */
    public void setNewMessage(Message newMessage) {
        this.newMessage = newMessage;
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
     * @return the senderColor
     */
    public String getSenderColor() {
        return senderColor;
    }

    /**
     * @param senderColor the senderColor to set
     */
    public void setSenderColor(String senderColor) {
        this.senderColor = senderColor;
    }

    /**
     * @return the receiverColor
     */
    public String getReceiverColor() {
        return receiverColor;
    }

    /**
     * @param receiverColor the receiverColor to set
     */
    public void setReceiverColor(String receiverColor) {
        this.receiverColor = receiverColor;
    }

}
