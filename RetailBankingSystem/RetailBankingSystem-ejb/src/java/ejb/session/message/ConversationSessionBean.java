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
import javax.persistence.Query;

/**
 *
 * @author leiyang
 */
@Stateless
public class ConversationSessionBean implements ConversationSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public Conversation createConversation(Conversation c) {
        try {
            em.persist(c);
            StaffAccount sender = c.getSender();
            StaffAccount receiver = c.getReceiver();
            em.merge(sender);
            em.merge(receiver);
            return c;
        } catch (EntityExistsException e) {
            return null;
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
    
    @Override
    public String checkIfConversationExists(StaffAccount sender, StaffAccount receiver) {
        try {
            System.out.println("SELECT c FROM Conversation c WHERE (c.receiver = AND c.sender = :receiver) OR (c.sender = :sender AND c.receiver = :receiver)");
            Query q = em.createQuery("SELECT c FROM Conversation c WHERE (c.receiver.username = :sender AND c.sender.username = :receiver) OR (c.sender.username = :sender AND c.receiver.username = :receiver)");
            q.setParameter("sender", sender.getUsername());
            q.setParameter("receiver", receiver.getUsername());
            List<Conversation> result = q.getResultList();
            System.out.println("after  q.getResultList()");
            System.out.println(result);
            if (result != null && !result.isEmpty() && result.size() == 1) {
                System.out.println("Conversation:" + result.size());
                return result.get(0).getId().toString();
            }
            System.out.println("Return null");
            return "NOT_FOUND";
        } catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
            return "EXCEPTION";
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
