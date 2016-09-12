/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.message;

import ejb.session.message.ConversationSessionBeanLocal;
import entity.Conversation;
import entity.Message;
import entity.StaffAccount;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import utils.LoggingUtil;
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

    private final EventBus eventBus = EventBusFactory.getDefault().eventBus();
    private final static String CHANNEL = "/chat/";

    private String conversationId;
    private Conversation currentConversation;
    private Message newMessage = new Message();

    public MessageViewManagedBean() {
    }

    public void init() {
        LoggingUtil.StaffMessageLog(MessageViewManagedBean.class, "@PostConstruct, retriving conversation from id:" + conversationId);
        setCurrentConversation(conversationBean.getConversationById(Long.parseLong(conversationId)));
    }

    public void sendMessage() {
        // Send message action
        newMessage.setReceiver(getReceiverUsername());
        newMessage.setSender(SessionUtils.getStaffUsername());
        LoggingUtil.StaffMessageLog(MessageViewManagedBean.class, String.format("MessageViewManagedBean: SendMessage(): newMessage is %s", getNewMessage()));
        if (conversationBean.addMessage(currentConversation, newMessage)) {
            LoggingUtil.StaffMessageLog(MessageViewManagedBean.class, "Created New Message:" + newMessage.getMessage() + " And send to channel: " + CHANNEL + getReceiverUsername());
            MessageDTO mDTO = new MessageDTO();
            mDTO.setCreateDate(newMessage.getCreateDate().toString());
            mDTO.setLabel(getMessageLabel(newMessage));
            mDTO.setMessage(newMessage.getMessage());
            mDTO.setSenderName(SessionUtils.getStaff().getFullName());
            eventBus.publish(CHANNEL + getReceiverUsername(), mDTO);
            newMessage = new Message();
        } else {
            LoggingUtil.StaffMessageLog(MessageViewManagedBean.class, "Created New Message FAILED");
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

}
