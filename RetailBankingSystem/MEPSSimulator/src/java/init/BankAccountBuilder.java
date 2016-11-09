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
        SettlementAccount result = null;
        try {
            result = mepsBean.find("000");
        } catch (Exception e) {
            return true;
        }
        return result == null;
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

        // MBS
        SettlementAccount mbs = new SettlementAccount();
        mbs.setBankCode("001");
        mbs.setAmount(new BigDecimal(50000000));//initial 50m
        mbs.setName("Merlion Bank Singapore");//001
        mepsBean.persist(mbs);


        initBankEntities();
    }

    private void initBankEntities() {
        String[] bankNames = {
            "AUSTRALIA & NEW ZEALAND BANKING GROUP",
            "BANK OF CHINA LIMITED",
            "BNP PARIBAS",
            "CIMB BANK BERHAD",
            "CITIBANK NA",//005
            "DEUTSCHE BANK AG",
            "FAR EASTERN BANK LTD",
            "HL BANK",
            "HSBC (Corporate)",
            "HSBC (Personal)",
            "MALAYAN BANKING BHD",
            "MIZUHO BANK LIMITED",
            "OVERSEA-CHINESE BANKING CORPN LTD",//013
            "RHB BANK BERHAD",
            "STANDARD CHARTERED BANK",
            "SUMITOMO MITSUI BANKING CORPORATION",
            "THE BANK OF TOKYO-MITSUBISHI UFJ, LTD",
            "THE ROTAL BANK OF SCOTLAND N.V.",
            "UNITED OVERSEAS BANK LTD"
        };
        String[] bankCodes = {"020", "002", "003", "004", "005", "006", "007", "008", "009", "010", "011", "012", "013", "014", "015", "016", "017", "018", "019"};

        for (int i = 0; i < bankNames.length; i++) {
            SettlementAccount other = new SettlementAccount();
            other.setBankCode(bankCodes[i]);
            other.setAmount(new BigDecimal(50000000));//initial 50m
            other.setName(bankNames[i]);
            mepsBean.persist(other);
        }
    }
}
