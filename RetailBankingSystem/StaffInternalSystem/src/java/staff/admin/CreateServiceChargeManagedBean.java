/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.admin;

import ejb.session.utils.UtilsSessionBeanLocal;
import entity.common.AuditLog;
import entity.staff.ServiceCharge;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.ConstantUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "createServiceChargeManagedBean")
@ViewScoped
public class CreateServiceChargeManagedBean implements Serializable {

    @EJB 
    private UtilsSessionBeanLocal utilsBean;
    
    private ServiceCharge newCharge = new ServiceCharge();
    private List<ServiceCharge> charges = new ArrayList<>();
    /**
     * Creates a new instance of CreateServiceChargeManagedBean
     */
    public CreateServiceChargeManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        List<Object> temp = utilsBean.findAll(ConstantUtils.SERVICE_CHARGE_ENTITY);
        for (Object o : temp) {
            if (o instanceof ServiceCharge) {
                getCharges().add((ServiceCharge) o);
            }
        }
        AuditLog a = new AuditLog();
        a.setActivityLog("System user enter create_service_charge.xhtml");
        a.setFunctionName("CreateServiceChargeProductManagedBean @PostConstruct init()");
        a.setFunctionInput("Getting all service charges");
        a.setStaffAccount(SessionUtils.getStaff());
        utilsBean.persist(a);
    }
    
    public void addCharge() {
        ServiceCharge result = (ServiceCharge) utilsBean.persist(getNewCharge());
        
        AuditLog a = new AuditLog();
        a.setActivityLog("System user add charge");
        a.setFunctionName("CreateServiceChargeManagedBean addCharge()");
        if (result != null) {
            getCharges().add(result);
            setNewCharge(new ServiceCharge());
            
            a.setFunctionOutput("SUCCESS");
            MessageUtils.displayInfo("New Charges Added");
        } else {
            a.setFunctionOutput("FAIL");
            MessageUtils.displayInfo("Charges already Added");
        }
    }
    
    public void onCellEdit(ServiceCharge sc) {
        ServiceCharge result = (ServiceCharge) utilsBean.merge(sc);
        if (result != null) {
            MessageUtils.displayInfo(result.getName() + " Edited");
        } else {
            MessageUtils.displayInfo(result.getName() + " Not Edited");
        }
    }

    /**
     * @return the newCharge
     */
    public ServiceCharge getNewCharge() {
        return newCharge;
    }

    /**
     * @param newCharge the newCharge to set
     */
    public void setNewCharge(ServiceCharge newCharge) {
        this.newCharge = newCharge;
    }

    /**
     * @return the charges
     */
    public List<ServiceCharge> getCharges() {
        return charges;
    }

    /**
     * @param charges the charges to set
     */
    public void setCharges(List<ServiceCharge> charges) {
        this.charges = charges;
    }
}
