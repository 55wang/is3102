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
@Named(value = "staffLogoutManagedBean")
@ViewScoped
public class StaffLogoutManagedBean implements Serializable {
    @EJB
    private UtilsSessionBeanLocal utilsBean;
    /**
     * Creates a new instance of StaffLogoutManagedBean
     */
    public StaffLogoutManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter logou.xhtml");
        a.setFunctionName("StaffLogoutManagedBean @PostConstruct init()");
        a.setFunctionInput("Getting all customer information");
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
