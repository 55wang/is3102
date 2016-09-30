/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.cms;

import entity.customer.CustomerCase;
import entity.customer.MainAccount;
import entity.staff.StaffAccount;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author VIN-S
 */
@Remote
public interface CustomerCaseSessionBeanRemote {
    public Boolean saveCase(CustomerCase customerCase);
    public Boolean updateCase(CustomerCase customerCase);
    public CustomerCase searchCaseByID(String id);
    public List<CustomerCase> searchCaseByTitle(String title);
    public Boolean cancelCase(Long id);
    public List<CustomerCase> getAllCase();
    public List<CustomerCase> getAllCaseUnderCertainStaff(StaffAccount staffAccount);
}
