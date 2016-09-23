/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BatchProcess;

import entity.dams.account.DepositAccount;
import entity.dams.rules.ConditionInterest;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author litong
 */
@Local
public interface InterestAccrualSessionBeanLocal {
    public List<DepositAccount> calculateMonthlyInterestsForDepositAccount(List<DepositAccount> a);
    public List<DepositAccount> calculateDailyInterestsForDepositAccount(List<DepositAccount> a);
    public DepositAccount calculateMonthlyInterestForDepositAccount(DepositAccount a);
    public DepositAccount calculateDailyInterestForDepositAccount(DepositAccount a);
    public Boolean isAccountMeetCondition(DepositAccount a, ConditionInterest i);
}
