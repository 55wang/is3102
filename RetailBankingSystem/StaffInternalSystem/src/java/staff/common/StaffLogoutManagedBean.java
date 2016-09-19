/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.common;

import java.io.Serializable;
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

    /**
     * Creates a new instance of StaffLogoutManagedBean
     */
    public StaffLogoutManagedBean() {
    }
    
    public void logout(){
        HttpSession session = SessionUtils.getSession();
        if(session!=null)
            session.invalidate();
        RedirectUtils.redirect("../index.xhtml");
    }
}
