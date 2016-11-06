/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.counter.TellerCounterSessionBeanLocal;
import entity.counter.TellerCounter;
import entity.staff.ServiceCharge;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author leiyang
 */
@Stateless
@LocalBean
public class EntityTellerCounterBuilder {
    
    @EJB
    private TellerCounterSessionBeanLocal counterBean;
    
    public void init() {
        TellerCounter tc = new TellerCounter();
        tc.setCurrentCash(new BigDecimal(10000));
        tc.setId(9L);
        counterBean.createTellerCounter(tc);
        
        ServiceCharge sc = new ServiceCharge();
        sc.setName("Non Cash Service");
        sc.setCharges(new BigDecimal(2));
        counterBean.createServiceCharge(sc);
        
        ServiceCharge sc1 = new ServiceCharge();
        sc1.setName("Billing Service");
        sc1.setCharges(new BigDecimal(5));
        counterBean.createServiceCharge(sc1);
        
        ServiceCharge sc2 = new ServiceCharge();
        sc2.setName("Transfer Service");
        sc2.setCharges(new BigDecimal(10));
        counterBean.createServiceCharge(sc2);
    }
}
