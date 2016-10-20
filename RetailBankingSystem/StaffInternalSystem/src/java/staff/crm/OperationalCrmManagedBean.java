/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.crm;

import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author wang
 */
@Named(value = "operationalCrmManagedBean")
@Dependent
public class OperationalCrmManagedBean implements Serializable{

    /**
     * Creates a new instance of OperationalCrmManagedBean
     */
    public OperationalCrmManagedBean() {
    }

    public void viewEmailCampaign() {
    }
    
    public void sendEmailCampaign() {
    }

}
