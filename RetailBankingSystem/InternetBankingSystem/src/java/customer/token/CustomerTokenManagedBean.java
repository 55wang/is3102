/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.token;

import ejb.session.token.TokenSecurityLocal;
import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import utils.MessageUtils;
import utils.SessionUtils;
import utils.PincodeGenerationUtils;
import utils.RedirectUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "customerTokenManagedBean")
@ViewScoped
public class CustomerTokenManagedBean implements Serializable{
    @EJB
    private TokenSecurityLocal tokenSecurity;
    private String pinCode;
    private String inputTokenString;
    @ManagedProperty(value="#{param.target}")
    private String targetPage;
    /**
     * Creates a new instance of CustomerTokenManagedBean
     */
    public CustomerTokenManagedBean() {
    }

    public String getPinCode() {
        return pinCode;
    }
    
    public void verify(){
        Boolean result = tokenSecurity.verifyTokens(inputTokenString, SessionUtils.getUserName(), Integer.valueOf(pinCode));
        
        if(result){
            String msg = "Authentication Success!";
            MessageUtils.displayInfo(msg);
            SessionUtils.setTokenAuthentication(true);
            RedirectUtils.redirect("../" + targetPage);
        }
        else{
            String msg = "Authentication Fail!";
            MessageUtils.displayError(msg);
        }
    }
    
    @PostConstruct
    public void setPinCode() {
        targetPage = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("target");
        this.pinCode = PincodeGenerationUtils.generateRandom(true, 8);
    }

    public String getInputTokenString() {
        return inputTokenString;
    }

    public void setInputTokenString(String inputTokenString) {
        this.inputTokenString = inputTokenString;
    }   
}
