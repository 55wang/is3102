/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.bean.MEPSSessionBean;
import entity.SettlementAccount;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author leiyang
 */
@Singleton
@LocalBean
@Startup
public class BankAccountBuilder {
    
    @EJB
    private MEPSSessionBean mepsBean;
    
    @PostConstruct
    public void init() {
        System.out.println("BankAccountBuilder @PostConstruct");
        if (needInit()) {
            buildEntities();
        } else {
            // test some rules
        }
    }
    
    private Boolean needInit() {
        return mepsBean.find("000") == null;
    }
    
    private void buildEntities() {
        SettlementAccount sach = new SettlementAccount();
        sach.setBankCode("000");
        sach.setAmount(BigDecimal.ZERO);
        sach.setName("Singapore Automated Clearing House");
        mepsBean.persist(sach);
        
        SettlementAccount fast = new SettlementAccount();
        fast.setBankCode("111");
        fast.setAmount(BigDecimal.ZERO);
        fast.setName("FAST Agent");
        mepsBean.persist(fast);
        
        SettlementAccount mbs = new SettlementAccount();
        mbs.setBankCode("001");
        mbs.setAmount(new BigDecimal(50000000));//initial 50m
        mbs.setName("Merlion Bank Singapore");
        mepsBean.persist(mbs);
        
        SettlementAccount other = new SettlementAccount();
        other.setBankCode("002");
        other.setAmount(new BigDecimal(50000000));//initial 50m
        other.setName("Other bank");
        mepsBean.persist(other);
    }
}
