/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.utils;

import entity.customer.Customer;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface UtilsSessionBeanLocal {
    public Object find(Class type, Long id);
    public List<Object> findAll(String entityName);
    public Object persist(Object object);
    public Object merge(Object object);
    
    
    
    
    public Boolean checkIdentityNumberIsUnique(String identityNumber);
    public Boolean checkEmailIsUnique(String email);
    public Boolean checkPhoneIsUnique(String phone);
    public Boolean checkUpdatedEmailIsUnique(Customer customer);
    public Boolean checkUpdatedPhoneIsUnique(Customer customer);

}
