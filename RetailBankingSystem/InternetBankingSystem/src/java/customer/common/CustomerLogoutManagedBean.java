/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import java.io.Serializable;
import javax.faces.context.FacesContext;
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

    /**
     * Creates a new instance of CustomerLogoutManagedBean
     */
    public CustomerLogoutManagedBean() {
    }
    
    public void logout(){
        HttpSession session = SessionUtils.getSession();
        if(session!=null)
            session.invalidate();
        RedirectUtils.redirect("../index.xhtml");
    }
}
