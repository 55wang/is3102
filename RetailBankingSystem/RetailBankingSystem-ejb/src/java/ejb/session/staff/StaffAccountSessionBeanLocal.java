/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.staff;

import entity.staff.StaffAccount;
import java.util.List;
import javax.ejb.Local;
import util.exception.common.DuplicateStaffAccountException;
import util.exception.common.NoStaffAccountsExistException;
import util.exception.common.StaffAccountNotExistException;
import util.exception.common.UpdateStaffAccountException;

/**
 *
 * @author leiyang
 */
@Local
public interface StaffAccountSessionBeanLocal {
    
    public StaffAccount getAdminStaff();
    public StaffAccount loginAccount(String username, String password);
    public StaffAccount getAccountByUsername(String username);
    public StaffAccount getAccountByEmail(String email);
    public StaffAccount createAccount(StaffAccount sa) throws DuplicateStaffAccountException;
    public StaffAccount updateAccount(StaffAccount sa) throws UpdateStaffAccountException;
    public List<StaffAccount> getAllStaffs() throws NoStaffAccountsExistException;
    public StaffAccount getStaffById(String id) throws StaffAccountNotExistException ;
    public List<StaffAccount> searchStaffByUsernameOrName(String searchText);
    public StaffAccount removeStaffAccount(String id);
    
}
