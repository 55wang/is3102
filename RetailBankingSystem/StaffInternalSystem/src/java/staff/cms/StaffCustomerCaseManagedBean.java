/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.cms;

import ejb.session.cms.CustomerCaseSessionBeanLocal;
import entity.customer.CustomerCase;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "staffCustomerCaseManagedBean")
@ViewScoped
public class StaffCustomerCaseManagedBean implements Serializable{
    @EJB
    private CustomerCaseSessionBeanLocal customerCaseSessionBean;
    
    private List<CustomerCase> cases;

    
    /**
     * Creates a new instance of StaffCutomerCaseManagedBean
     */
    public StaffCustomerCaseManagedBean() {
    }
    
    @PostConstruct
    public void setCases() {
        this.cases = customerCaseSessionBean.getAllCaseUnderCertainStaff(SessionUtils.getStaff());
    }
    
    public List<CustomerCase> getCases() {
        return cases;
    }
}
