/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bill;

import entity.bill.Organization;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface BillSessionBeanLocal {
    public Organization createOrganization(Organization o);
    public Organization updateOrganization(Organization o);
    public List<Organization> getActiveListOrganization();
}
