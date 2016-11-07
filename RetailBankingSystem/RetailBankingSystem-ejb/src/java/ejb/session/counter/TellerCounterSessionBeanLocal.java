/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.counter;

import entity.counter.ServiceChargeTransaction;
import entity.counter.TellerCounter;
import entity.staff.ServiceCharge;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface TellerCounterSessionBeanLocal {
    // teller counter
    public TellerCounter createTellerCounter(TellerCounter object);
    public TellerCounter updateTellerCounter(TellerCounter object);
    public TellerCounter getTellerCounterById(Long id);
    // service charge transaction
    public ServiceChargeTransaction createServiceChargeTransaction(ServiceChargeTransaction object);
    public ServiceChargeTransaction updateServiceChargeTransaction(ServiceChargeTransaction object);
    // service charge
    public ServiceCharge createServiceCharge(ServiceCharge object);
    public ServiceCharge updateServiceCharge(ServiceCharge object);
    public ServiceCharge getServiceChargeByName(String name);
    public List<ServiceCharge> getAllServiceCharges();
}
