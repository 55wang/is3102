/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.audit;

import entity.common.AuditLog;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wang
 */
@Local
public interface AuditSessionBeanLocal {
    public List<AuditLog> getAuditLogByCustomerID(String userID);
    public List<AuditLog> getAuditLogByStaffUsername(String username);
    public Boolean insertAuditLog(AuditLog auditLog);
}
