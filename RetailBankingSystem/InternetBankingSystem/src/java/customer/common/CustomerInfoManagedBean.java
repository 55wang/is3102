/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import server.utilities.ConstantUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "customerInfoManagedBean")
@SessionScoped
public class CustomerInfoManagedBean implements Serializable {

    private final String IP_ADDRESS = ConstantUtils.ipAddress;
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

    /**
     * @return the IP_ADDRESS
     */
    public String getIP_ADDRESS() {
        return IP_ADDRESS;
    }
}
