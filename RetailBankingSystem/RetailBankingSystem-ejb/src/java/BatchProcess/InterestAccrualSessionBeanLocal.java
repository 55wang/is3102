/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BatchProcess;

import entity.dams.account.DepositAccount;
import entity.dams.rules.ConditionInterest;
import javax.ejb.Local;

/**
 *
 * @author litong
 */
@Local
public interface InterestAccrualSessionBeanLocal {
    public Boolean isAccountMeetCondition(DepositAccount a, ConditionInterest i);
}
