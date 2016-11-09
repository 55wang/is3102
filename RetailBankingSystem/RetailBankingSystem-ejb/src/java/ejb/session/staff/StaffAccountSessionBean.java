/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.staff;

import entity.staff.StaffAccount;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.ConstantUtils;
import util.exception.common.DuplicateStaffAccountException;
import util.exception.common.NoStaffAccountsExistException;
import util.exception.common.StaffAccountNotExistException;
import util.exception.common.UpdateStaffAccountException;

/**
 *
 * @author leiyang
 */
@Stateless
public class StaffAccountSessionBean implements StaffAccountSessionBeanLocal, StaffAccountSessionBeanRemote {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public StaffAccount getAdminStaff() {

        try {
            StaffAccount user = em.find(StaffAccount.class, ConstantUtils.SUPER_ADMIN_USERNAME);
            return user;
        } catch (NoResultException ex) {
            return null;
        }

    }

    @Override
    public StaffAccount loginAccount(String username, String password) {
        try {
            StaffAccount user = em.find(StaffAccount.class, username);
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }
            return null;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public StaffAccount getAccountByUsername(String username) {
        try {
            StaffAccount user = em.find(StaffAccount.class, username);
            return user;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public StaffAccount getAccountByEmail(String email) {

        Query q = em.createQuery("SELECT sa FROM StaffAccount sa WHERE sa.email = :email");

        q.setParameter("email", email);

        StaffAccount sa = null;

        try {
            List<StaffAccount> accounts = q.getResultList();
            if (accounts != null && !accounts.isEmpty() && accounts.size() == 1) {
                return accounts.get(0);
            } else {
                return null;
            }
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public StaffAccount createAccount(StaffAccount sa) throws DuplicateStaffAccountException {
        try {
            if (sa.getUsername() == null) {
                return null;
            } else {
                StaffAccount result = em.find(StaffAccount.class, sa.getUsername());
                if (result != null) {
                    throw new DuplicateStaffAccountException("Duplicate Staff Account!");
                }
            }

            em.persist(sa);
            return sa;
        } catch (EntityExistsException e) {
            throw new DuplicateStaffAccountException("Duplicate Staff Account!");
        }
    }

    @Override
    public StaffAccount updateAccount(StaffAccount sa) throws UpdateStaffAccountException {

        try {

            if (sa.getUsername() == null) {
                throw new UpdateStaffAccountException("Not an entity!");
            }

            em.merge(sa);
            return sa;
        } catch (IllegalArgumentException e) {
            throw new UpdateStaffAccountException("Not an entity!");
        }

    }

    @Override
    public List<StaffAccount> getAllStaffs() throws NoStaffAccountsExistException {
        Query q = em.createQuery("SELECT sa FROM StaffAccount sa");
        try {

            List<StaffAccount> result = q.getResultList();

            if (result.isEmpty()) {
                throw new NoStaffAccountsExistException("No Staff Account Not Found");
            }

            return result;
        } catch (NoResultException ex) {
            System.out.println("Catch!");
            throw new NoStaffAccountsExistException("No Staff Account Not Found");
        }
    }

    @Override
    public StaffAccount getStaffById(String id) throws StaffAccountNotExistException {

        if (id == null) {
            throw new StaffAccountNotExistException("Staff Account Not Found!");
        }

        try {

            StaffAccount result = em.find(StaffAccount.class, id);
            if (result == null) {
                throw new StaffAccountNotExistException("Staff Account Not Found!");
            }
            // REMARK: See if we need to check cancelstatus
            return result;
        } catch (IllegalArgumentException e) {
            throw new StaffAccountNotExistException("Staff Account Not Found!");
        }

    }

    @Override
    public List<StaffAccount> searchStaffByUsernameOrName(String searchText) {
        Query q = em.createQuery(
                "SELECT sa FROM StaffAccount sa WHERE "
                + "LOWER(sa.username) LIKE :searchText OR "
                + "LOWER(sa.firstName) LIKE :searchText OR "
                + "LOWER(sa.lastName) LIKE :searchText"
        );
        q.setParameter("searchText", "%" + searchText.toLowerCase() + "%");
        return q.getResultList();
    }

    @Override
    public StaffAccount removeStaffAccount(String id) {
        StaffAccount result = em.find(StaffAccount.class, id);
        if (result == null) {
            return null;
        }
        em.remove(result);
        return result;
    }
}
