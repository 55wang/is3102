/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.dams.rules.Interest;
import entity.dams.rules.TimeRangeInterest;
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
    public List<Interest> showAllPresentInterests();
    public List<TimeRangeInterest> getFixedDepositAccountDefaultInterests();
}
