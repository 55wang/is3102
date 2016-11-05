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

    public Long getDepositRecencyByCustomerId(Long Id);
    public Long getDepositFrequencyByCustomerId(Long Id);
    public Long getDepositMonetaryByCustomerId(Long Id);
    public Long updateDepositRecencyByCustomerId(Long Id);
    public Long updateDepositFrequencyByCustomerId(Long Id);
    public Long updateDepositMonetaryByCustomerId(Long Id);
    
    public Long getCardRecencyByCustomerId(Long Id);
    public Long getCardFrequencyByCustomerId(Long Id);
    public Long getCardMonetaryByCustomerId(Long Id);
    public Long updateCardRecencyByCustomerId(Long Id);
    public Long updateCardFrequencyByCustomerId(Long Id);
    public Long updateCardMonetaryByCustomerId(Long Id);
    
}
