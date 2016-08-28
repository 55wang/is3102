/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.dams;

import entity.Interest;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface InterestSessionBeanLocal {
    // create new or update interest
    public void addInterest(Interest interest);
    
    public List<Interest> showAllInterests();
}
