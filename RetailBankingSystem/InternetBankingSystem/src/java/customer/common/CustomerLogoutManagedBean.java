/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.customer.MainAccount;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import utils.RedirectUtils;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "customerLogoutManagedBean")
@ViewScoped
public class CustomerLogoutManagedBean implements Serializable{

    @EJB
    private UtilsSessionBeanLocal utilsBean;
    
    /**
     * Creates a new instance of CustomerLogoutManagedBean
     */
    public CustomerLogoutManagedBean() {
    }
    
    public void logout(){
        AuditLog a = new AuditLog();
        a.setActivityLog("Log off at: " + new Date());
        a.setFunctionName("CustomerLogoutManagedBean logoutCustomer()");
        a.setMainAccount((MainAccount)utilsBean.find(MainAccount.class, Long.parseLong(SessionUtils.getUserId())));
        utilsBean.persist(a);
        
        HttpSession session = SessionUtils.getSession();
        if(session!=null)
            session.invalidate();
        RedirectUtils.redirect("../index.xhtml");
    }
}
