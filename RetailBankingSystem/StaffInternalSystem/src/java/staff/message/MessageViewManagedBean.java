/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.message;

import ejb.session.message.ConversationSessionBeanLocal;
import entity.Conversation;
import entity.Message;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.json.JSONObject;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import utils.LoggingUtil;

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
    
    public MessageViewManagedBean() {}
    
    public void init() {
        LoggingUtil.StaffMessageLog(MessageViewManagedBean.class, "@PostConstruct, retriving conversation from id:" + conversationId);
        setCurrentConversation(conversationBean.getConversationById(Long.parseLong(conversationId)));
    }
    
    public void sendMessage() {
        // Send message action
        LoggingUtil.StaffMessageLog(MessageViewManagedBean.class, String.format("ChatViewManagedBean: SendMessage(): newMessage is %s",getNewMessage()));
        newMessage.setReceiver(currentConversation.getReceiver().getUsername());
        newMessage.setSender(currentConversation.getSender().getUsername());
        if (conversationBean.addMessage(currentConversation, newMessage)) {
            LoggingUtil.StaffMessageLog(MessageViewManagedBean.class, "Created New Message:" + newMessage.getMessage());
            newMessage = new Message();
        } else {
            LoggingUtil.StaffMessageLog(MessageViewManagedBean.class, "Created New Message FAILED");
        }
        LoggingUtil.StaffMessageLog(MessageViewManagedBean.class, new JSONObject(newMessage).toString());
        eventBus.publish(CHANNEL + currentConversation.getReceiver().getUsername(), newMessage);
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
