/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.crm;

import javax.ejb.Local;

/**
 *
 * @author wang
 */
@Local
public interface RFMSessionBeanLocal {

    public Long getDepositRecencyByCustomerId(String Id);
    public Long getDepositFrequencyByCustomerId(String Id);
    public Long getDepositMonetaryByCustomerId(String Id);
    
    public Long getCardRecencyByCustomerId(String Id);
    public Long getCardFrequencyByCustomerId(String Id);
    public Long getCardMonetaryByCustomerId(String Id);
    
}
