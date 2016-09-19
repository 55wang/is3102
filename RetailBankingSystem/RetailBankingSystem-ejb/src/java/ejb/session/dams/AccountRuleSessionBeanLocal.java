/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.dams.rules.DepositRule;
import entity.dams.rules.Interest;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface AccountRuleSessionBeanLocal {
    // create new or update interest
    public Boolean addInterest(Interest interest);
    public Boolean updateInterest(Interest interest);
    public List<Interest> showAllInterests();
    public List<Interest> getCurrentAccountDefaultInterests();
    public List<Interest> getFixedDepositAccountDefaultInterests();
    public List<Interest> getSavingccountDefaultInterests();
    public List<Interest> getLoanAccountDefaultInterests();
    public List<Interest> getMobileAccountDefaultInterests();
    public List<Interest> getDefaultInterestsByAccountName(String accountName);
    // deposit Rule
    public Boolean addDepositRule(DepositRule depositRule);
    public Boolean updateDepositRule(DepositRule depositRule);
    public DepositRule getDepositRuleByAccountName(String accountName);
}
