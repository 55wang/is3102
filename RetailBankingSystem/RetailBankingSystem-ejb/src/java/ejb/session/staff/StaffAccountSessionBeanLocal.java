/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.staff;

import entity.staff.StaffAccount;
import java.util.List;
import javax.ejb.Local;

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
    public StaffAccount createAccount(StaffAccount sa);
    public StaffAccount updateAccount(StaffAccount sa);
    public List<StaffAccount> getAllStaffs();
    public StaffAccount getStaffById(String id);
    public List<StaffAccount> searchStaffByUsernameOrName(String searchText);
}
