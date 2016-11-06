/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.bean.SACHSessionBean;
import entity.SachSettlement;
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
public class SachBankAccountBuilder {

    @EJB
    private SACHSessionBean sachBean;

    @PostConstruct
    public void init() {
        System.out.println("SachBankAccountBuilder @PostConstruct");
        if (needInit()) {
            buildEntities();
        } else {
            // test some rules
        }
    }

    private Boolean needInit() {
        SachSettlement result = null;
        try {
            result = sachBean.findSettlement("001");
        } catch (Exception e) {
            return true;
        }
        return result == null;
    }

    private void buildEntities() {
        SachSettlement mbs = new SachSettlement();
        mbs.setBankCode("001");
        mbs.setAmount(BigDecimal.ZERO);
        mbs.setName("Merlion Bank Singapore");
        sachBean.persistSettlement(mbs);

        SachSettlement citi = new SachSettlement();
        citi.setBankCode("005");
        citi.setAmount(BigDecimal.ZERO);
        citi.setName("CITIBANK NA");
        sachBean.persistSettlement(citi);

        SachSettlement ocbc = new SachSettlement();
        ocbc.setBankCode("013");
        ocbc.setAmount(BigDecimal.ZERO);
        ocbc.setName("OVERSEA-CHINESE BANKING CORPN LTD");
        sachBean.persistSettlement(ocbc);

    }

}
