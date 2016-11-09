/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.cms;

import entity.customer.CustomerCase;
import entity.staff.StaffAccount;
import java.util.List;
import javax.ejb.Remote;
import util.exception.cms.AllCustomerCaseException;
import util.exception.cms.CancelCustomerCaseException;
import util.exception.cms.CustomerCaseNotFoundByTitleException;
import util.exception.cms.CustomerCaseNotFoundException;
import util.exception.cms.DuplicateCaseExistException;
import util.exception.cms.UpdateCaseException;

/**
 *
 * @author VIN-S
 */

@Remote
public interface CustomerCaseSessionBeanRemote {
    public CustomerCase createCase(CustomerCase customerCase) throws DuplicateCaseExistException ;
    public CustomerCase updateCase(CustomerCase customerCase) throws UpdateCaseException;
    public CustomerCase getCaseById(String id) throws CustomerCaseNotFoundException ;
    public List<CustomerCase> getCaseByTitle(String title) throws CustomerCaseNotFoundByTitleException ;
    public CustomerCase removeCase(String id) throws CancelCustomerCaseException;
    public List<CustomerCase> getAllCase() throws AllCustomerCaseException ;
    public List<CustomerCase> getAllCaseUnderCertainStaff(StaffAccount staffAccount);
}
