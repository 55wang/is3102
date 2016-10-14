/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.webservice;

import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface WebserviceSessionBeanLocal {
    public void paySACHSettlement(String netSettlementAmount);
    public void payFASTSettlement(String netSettlementAmount);
}
