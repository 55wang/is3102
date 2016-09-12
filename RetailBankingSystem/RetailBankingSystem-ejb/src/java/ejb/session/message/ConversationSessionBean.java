/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.message;

import entity.Conversation;
import entity.Message;
import entity.StaffAccount;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author leiyang
 */
@Stateless
public class ConversationSessionBean implements ConversationSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public Boolean createConversation(Conversation c) {
        try {
            em.persist(c);
            return true;
        } catch (EntityExistsException e) {
            return false;
        }
    }
    
    @Override
    public Boolean updateConversation(Conversation c) {
        try {
            em.merge(c);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Boolean addMessage(Conversation c, Message m) {
        try {
            c.addMessage(m);
            m.setConversation(c);
            em.merge(c);
            em.persist(m);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Conversation getConversationById(Long id) {
        if (id != null) {
            return em.find(Conversation.class, id);
        } else {
            return null;
        }
    }
    
    @Override
    public List<Conversation> getAllConversationForStaff(String username) {
        if (username != null) {
            StaffAccount sa = em.find(StaffAccount.class, username);
            System.out.println("Found StaffAccount: " + sa.getFullName());
            List<Conversation> conversations = sa.getReceiverConversation();
            System.out.println("Found StaffAccount: with receiver conversations: " + conversations.size());
            conversations.addAll(sa.getSenderConversation());
            System.out.println("Found StaffAccount: with sender conversations: " + sa.getSenderConversation().size());
            Collections.sort(conversations);
            System.out.println("Found StaffAccount: with conversations: " + conversations.size());
            return conversations;
        } else {
            return null;
        }
    }
}