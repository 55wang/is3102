/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.dams.rules.Interest;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface InterestSessionBeanLocal {
    // create new or update interest
    public Interest addInterest(Interest interest);
    public Interest updateInterest(Interest interest);
    public List<Interest> showAllInterests();
    public List<Interest> getCustomAccountDefaultInterests();
    public List<Interest> getCurrentAccountDefaultInterests();
    public List<Interest> getFixedDepositAccountDefaultInterests();
    public List<Interest> getSavingccountDefaultInterests();
    public List<Interest> getLoanAccountDefaultInterests();
    public List<Interest> getMobileAccountDefaultInterests();
    public List<Interest> getDefaultInterestsByAccountName(String accountName);
}
