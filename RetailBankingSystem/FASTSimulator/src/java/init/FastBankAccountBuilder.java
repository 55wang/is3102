/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.bean.FASTSessionBean;
import entity.FastSettlement;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author qiuxiaqing
 */
@Singleton
@LocalBean
@Startup
public class FastBankAccountBuilder {

    @EJB
    private FASTSessionBean fastBean;

    @PostConstruct
    public void init() {
        System.out.println("FastBankAccountBuilder @PostConstruct");
        if (needInit()) {
            buildEntities();
        } else {
            // test some rules
        }
    }

    private Boolean needInit() {
        FastSettlement result = null;
        try {
            result = fastBean.findSettlement("001");
        } catch (Exception e) {
            return true;
        }
        return result == null;
    }

    private void buildEntities() {
        FastSettlement mbs = new FastSettlement();
        mbs.setBankCode("001");
        mbs.setAmount(BigDecimal.ZERO);
        mbs.setName("MBS Singapore");
        fastBean.persistSettlement(mbs);

        FastSettlement citi = new FastSettlement();
        citi.setBankCode("005");
        citi.setAmount(BigDecimal.ZERO);
        citi.setName("CITIBANK");
        fastBean.persistSettlement(citi);

        FastSettlement ocbc = new FastSettlement();
        ocbc.setBankCode("013");
        ocbc.setAmount(BigDecimal.ZERO);
        ocbc.setName("OVERSEA-CHINESE BANKING CORPN LTD");
        fastBean.persistSettlement(ocbc);

    }

}
