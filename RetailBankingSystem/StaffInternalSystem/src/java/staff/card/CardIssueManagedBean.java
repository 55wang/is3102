/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.card;

import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "cardIssueManagedBean")
@Dependent
public class CardIssueManagedBean {
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    /**
     * Creates a new instance of CardIssueManagedBean
     */
    public CardIssueManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter issue_card.xhtml");
        a.setFunctionName("CardIssueManagedBean @PostConstruct init()");
        a.setInput("Getting all card applications");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }
    
}
