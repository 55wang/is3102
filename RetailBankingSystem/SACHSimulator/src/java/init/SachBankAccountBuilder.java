/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.bean.SACHSessionBean;
import entity.SachSettlement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;

/**
 *
 * @author qiuxiaqing
 */
@Stateless
public class SachBankAccountBuilder implements Serializable {

    @EJB
    private SACHSessionBean sachBean;

    
    public void init() {
        System.out.println("SachBankAccountBuilder @PostConstruct");
        if (needInit()) {
            buildEntities();
        } else {
            // test some rules
        }
    }

    private Boolean needInit() {
        List<SachSettlement> result = sachBean.getAllSachSettlement();
        return result == null || result.isEmpty();
    }

    private void buildEntities() {

        SachSettlement citi = new SachSettlement();
        citi.setFromBankCode("001");
        citi.setToBankCode("005");
        citi.setAmount(BigDecimal.ZERO);
        citi.setFromBankName("Merlion Bank Singapore");
        citi.setToBankName("CITIBANK NA");
        sachBean.persistSettlement(citi);

        SachSettlement ocbc = new SachSettlement();
        ocbc.setFromBankCode("001");
        ocbc.setToBankCode("013");
        ocbc.setAmount(BigDecimal.ZERO);
        ocbc.setFromBankName("Merlion Bank Singapore");
        ocbc.setToBankName("OVERSEA-CHINESE BANKING CORPN LTD");
        sachBean.persistSettlement(ocbc);

    }

}
