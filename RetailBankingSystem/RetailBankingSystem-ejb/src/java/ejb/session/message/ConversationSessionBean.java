/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.message;

import entity.staff.Conversation;
import entity.staff.Message;
import entity.staff.StaffAccount;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            StaffAccount sender = c.getSender();
            StaffAccount receiver = c.getReceiver();
            em.merge(sender);
            em.merge(receiver);
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
            Conversation c = em.find(Conversation.class, id);
            if (c != null) {
                em.refresh(c);
            }
            return c;
        } else {
            return null;
        }
    }
    
    @Override
    public List<Conversation> getAllConversationForStaff(String username) {
        if (username != null) {
            StaffAccount sa = em.find(StaffAccount.class, username);
            if (sa != null){
                em.refresh(sa);
            }
            System.out.println("Found StaffAccount: " + sa.getFullName());
            List<Conversation> conversations = sa.getReceiverConversation();
            System.out.println("Found StaffAccount: with receiver conversations: " + conversations.size());
            conversations.addAll(sa.getSenderConversation());
            System.out.println("Found StaffAccount: with sender conversations: " + sa.getSenderConversation().size());
            conversations = dedupConversation(conversations);
            Collections.sort(conversations);
            System.out.println("Found StaffAccount: with conversations: " + conversations.size());
            return conversations;
        } else {
            return null;
        }
    }
    
    private List<Conversation> dedupConversation(List<Conversation> conversations) {
        Map<Long, Conversation> map = new HashMap<>();
        for (Conversation c : conversations) {
            System.out.println("dedup: puting c.id:" + c.getId());
            map.put(c.getId(), c);
        }
        System.out.println("dedup: map size:" + map.size());
        return new ArrayList<Conversation>(map.values());
    }
}
