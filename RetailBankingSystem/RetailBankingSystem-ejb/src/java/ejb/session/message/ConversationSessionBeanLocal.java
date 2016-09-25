/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.message;

import entity.staff.Conversation;
import entity.staff.Message;
import entity.staff.StaffAccount;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface ConversationSessionBeanLocal {
    public Conversation createConversation(Conversation c);
    public Boolean updateConversation(Conversation c);
    public Boolean addMessage(Conversation c, Message m);
    public Conversation getConversationById(Long id);
    public List<Conversation> getAllConversationForStaff(String username);
    public String checkIfConversationExists(StaffAccount a, StaffAccount b);
}
