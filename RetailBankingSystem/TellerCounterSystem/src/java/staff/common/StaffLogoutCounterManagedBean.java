/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.common;

import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "staffLogoutCounterManagedBean")
@ViewScoped
public class StaffLogoutCounterManagedBean implements Serializable {
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    /**
     * Creates a new instance of StaffLogoutManagedBean
     */
    public StaffLogoutCounterManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        
        AuditLog a = new AuditLog();
        a.setActivityLog("Counter user enter logot.xhtml");
        a.setFunctionName("StaffLogoutCounterManagedBean @PostConstruct init()");
        a.setFunctionInput("Logging out");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);

    }
    
    public void logout(){
        HttpSession session = SessionUtils.getSession();
        if(session!=null)
            session.invalidate();
        RedirectUtils.redirect("../index.xhtml");
    }
}
