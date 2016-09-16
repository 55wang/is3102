/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.common;

import ejb.session.token.TokenSecurityLocal;
import java.io.Serializable;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.SessionUtils;
import utils.TokenUtils;

/**
 *
 * @author wang
 */
@Named(value = "customerTokenManagedBean")
@ViewScoped

public class CustomerTokenManagedBean implements Serializable {

    @EJB
    private TokenSecurityLocal tokenSessionBean;
    private String uid;
    private String pinCode;
    /**
     * Creates a new instance of CustomerTokenManagedBean
     */
    public CustomerTokenManagedBean() {
        uid = SessionUtils.getUserName();
    }

    public String generatePinCode() {
        pinCode = TokenUtils.generateRandom(true, 5);
        return pinCode;
    }
    
    public Boolean verifyPincode(String TokenString) {
        return tokenSessionBean.verifyTokens(TokenString, uid, Integer.parseInt(pinCode));
    }

}
