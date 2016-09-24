/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "customerInfoManagedBean")
@SessionScoped
public class CustomerInfoManagedBean implements Serializable {

    /**
     * Creates a new instance of CustomerInfoManagedBean
     */
    public CustomerInfoManagedBean() {
    }
    
    public String getUserName() {
        return SessionUtils.getUserName();
    }
    
    public String getUserId() {
        return SessionUtils.getUserId();
    }
}
