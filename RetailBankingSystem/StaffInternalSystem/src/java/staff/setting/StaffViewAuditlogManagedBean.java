/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.setting;

import ejb.session.audit.AuditSessionBeanLocal;
import entity.common.AuditLog;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import utils.SessionUtils;

/**
 *
 * @author leiyang
 */
@Named(value = "staffViewAuditlogManagedBean")
@ViewScoped
public class StaffViewAuditlogManagedBean implements Serializable {

    @EJB
    private AuditSessionBeanLocal auditBean;
    
    private List<AuditLog> auditLogs;
    
    /**
     * Creates a new instance of StaffViewAuditlogManagedBean
     */
    public StaffViewAuditlogManagedBean() {
    }
    
    @PostConstruct
    public void init() {
        auditLogs = auditBean.getAuditLogByStaffUsername(SessionUtils.getStaffUsername());
    }

    /**
     * @return the auditLogs
     */
    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }

    /**
     * @param auditLogs the auditLogs to set
     */
    public void setAuditLogs(List<AuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }
    
}
