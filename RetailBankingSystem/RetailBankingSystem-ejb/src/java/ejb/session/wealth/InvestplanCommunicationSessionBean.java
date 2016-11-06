/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

import entity.customer.WealthManagementSubscriber;
import entity.staff.StaffAccount;
import entity.wealth.InvestmentPlan;
import entity.wealth.InvestplanCommunication;
import entity.wealth.InvestplanMessage;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author VIN-S
 */
@Stateless
public class InvestplanCommunicationSessionBean implements InvestplanCommunicationSessionBeanLocal {
    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public InvestplanCommunication getCommunicationById(Long id){
        if (id != null) {
            InvestplanCommunication c = em.find(InvestplanCommunication.class, id);
            if (c != null) {
                em.refresh(c);
            }
            return c;
        } else {
            return null;
        }
    }
    
    
    @Override
    public InvestplanCommunication createCommunication(InvestplanCommunication ic) {
        try {
            em.persist(ic);
            WealthManagementSubscriber wms = ic.getWms();
            StaffAccount rm = ic.getRm();
            InvestmentPlan ip = ic.getIp();
            em.merge(wms);
            em.merge(rm);
            em.merge(ip);
            
            return ic;
        } catch (EntityExistsException e) {
            return null;
        }
    }
    
    @Override
    public InvestplanCommunication updateCommunication(InvestplanCommunication ic) {
    try {           
            em.merge(ic);
            return ic;
        } catch (EntityExistsException e) {
            return null;
        }
    }
    
    @Override
    public Boolean addMessage(InvestplanCommunication ic, InvestplanMessage m){
        try {
            System.out.println("Message going to be persist: " + m.getMessage());
            m.setInvestplanCommunication(ic);
            em.persist(m);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public String checkIfConversationExists(InvestmentPlan ip){
        try {
            Query q = em.createQuery("SELECT c FROM InvestplanCommunication c WHERE c.ip.id =:id");
            
            q.setParameter("id", ip.getId());
            InvestplanCommunication ic = (InvestplanCommunication) q.getSingleResult();
            if (ic != null) {
                return ic.getId().toString();
            }
            System.out.println("Return null");
            return "NOT_FOUND";
        } catch (Exception e) {
            return "EXCEPTION";
        }
    }
}
