/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.wealth;

import entity.customer.WealthManagementSubscriber;
import javax.ejb.Local;

/**
 *
 * @author VIN-S
 */
@Local
public interface WealthManegementSubscriberSessionBeanLocal {
    public WealthManagementSubscriber createWealthManagementSubscriber(WealthManagementSubscriber wms);
    public WealthManagementSubscriber getWealthManagementSubscriberById(Long id);
    public WealthManagementSubscriber updateWealthManagementSubscriber(WealthManagementSubscriber wms);
}
