/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

import entity.wealth.InvestmentPlan;
import entity.wealth.InvestplanCommunication;
import entity.wealth.InvestplanMessage;
import javax.ejb.Local;

/**
 *
 * @author VIN-S
 */
@Local
public interface InvestplanCommunicationSessionBeanLocal {
    public InvestplanCommunication getCommunicationById(Long id); 
    public InvestplanCommunication createCommunication(InvestplanCommunication ic) ;
    public InvestplanCommunication updateCommunication(InvestplanCommunication ic) ;
    public Boolean addMessage(InvestplanCommunication ic, InvestplanMessage m);
    public String checkIfConversationExists(InvestmentPlan ip);
}
