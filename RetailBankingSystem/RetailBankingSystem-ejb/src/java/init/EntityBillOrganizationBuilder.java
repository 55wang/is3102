/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package init;

import ejb.session.bill.BillSessionBeanLocal;
import entity.bill.BankEntity;
import entity.bill.Organization;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import server.utilities.EnumUtils;

/**
 *
 * @author leiyang
 */
@Stateless
@LocalBean
public class EntityBillOrganizationBuilder {

    @EJB
    private BillSessionBeanLocal billBean;

    public void initBillOrganization() {

        String[] clubsName = {
            "Singapore Swimming Club",
            "Singapore Island Country Club",
            "Temasek Club",
            "The Keppel Club",
            "Warren Golf & Country Club"
        };
        String[] clubsCode = {"C01", "C02", "C03", "C04", "C05"};
        for (int i = 0; i < clubsName.length; i++) {
            Organization o = new Organization();
            o.setType(EnumUtils.BillType.CLUBS);
            o.setName(clubsName[i]);
            o.setShortCode(clubsCode[i]);
            o.setPartnerBankCode("005");
            o.setPartnerBankAccount("1000001" + i);
            billBean.createOrganization(o);
        }

        String[] eduName = {
            "Raffles Institution",
            "Singapore American School Limited",
            "Singapore Management University",
            "Singapore Institute of Technology (SIT)",
            "Nanyang Polytechnic"
        };
        String[] eduCode = {"E01", "E02", "E03", "E04", "E05"};
        for (int i = 0; i < eduName.length; i++) {
            Organization o = new Organization();
            o.setType(EnumUtils.BillType.EDU);
            o.setName(eduName[i]);
            o.setShortCode(eduCode[i]);
            o.setPartnerBankCode("005");
            o.setPartnerBankAccount("2000001" + i);
            billBean.createOrganization(o);
        }

        String[] councilsName = {
            "Marine Parade Town Council"
        };
        String[] councilsCode = {"T01"};
        for (int i = 0; i < councilsName.length; i++) {
            Organization o = new Organization();
            o.setType(EnumUtils.BillType.COUNCILS);
            o.setName(councilsName[i]);
            o.setShortCode(councilsCode[i]);
            o.setPartnerBankCode("005");
            o.setPartnerBankAccount("3000001" + i);
            billBean.createOrganization(o);
        }

        String[] cardName = {
            "DBS/POSB Credit Card (Including DBS Amex Card)",
            "American Express Card"
        };
        String[] cardCode = {"CC01", "CC02"};
        for (int i = 0; i < cardName.length; i++) {
            Organization o = new Organization();
            o.setType(EnumUtils.BillType.CARD);
            o.setName(cardName[i]);
            o.setShortCode(cardCode[i]);
            o.setPartnerBankCode("005");
            o.setPartnerBankAccount("4000001" + i);
            billBean.createOrganization(o);
        }

        String[] utilsName = {
            "M1 Limited",
            "MyRepublic Pte Ltd",
            "Singapore Telecommunications Ltd",
            "SP Services",
            "StarHub Ltd"
        };
        String[] utilsCode = {"U01", "U02", "U03", "U04", "U05"};
        for (int i = 0; i < utilsName.length; i++) {
            Organization o = new Organization();
            o.setType(EnumUtils.BillType.UTIL);
            o.setName(utilsName[i]);
            o.setShortCode(utilsCode[i]);
            o.setPartnerBankCode("005");
            o.setPartnerBankAccount("5000001" + i);
            billBean.createOrganization(o);
        }

        String[] gaName = {
            "Agri-Food & Veterinary Authority (AVA)",
            "IRAS - Income Tax",
            "IRAS - Property Tax"
        };
        String[] gaCode = {"G01", "G02", "G03"};
        for (int i = 0; i < gaName.length; i++) {
            Organization o = new Organization();
            o.setType(EnumUtils.BillType.GOV_AGEN);
            o.setName(gaName[i]);
            o.setShortCode(gaCode[i]);
            o.setPartnerBankCode("013");
            o.setPartnerBankAccount("6000001" + i);
            billBean.createOrganization(o);
        }

        String[] ngaName = {
            "NETS - NETS Cheque",
            "NKF",
            "NTUC Membership"
        };
        String[] ngaCode = {"N01", "N02", "N03"};
        for (int i = 0; i < ngaName.length; i++) {
            Organization o = new Organization();
            o.setType(EnumUtils.BillType.NON_GOV_AGEN);
            o.setName(ngaName[i]);
            o.setShortCode(ngaCode[i]);
            o.setPartnerBankCode("013");
            o.setPartnerBankAccount("7000001" + i);
            billBean.createOrganization(o);
        }

        String[] insurName = {
            "AIA",
            "Aviva Health",
            "AVIVA Life 1",
            "AXA Life Insurance (Singapore) Pte Ltd",
            "CHINA LIFE INSURANCE (SINGAPORE) PTE. LTD.",
            "Great Eastern Life",
            "NTUC INCOME INSURANCE CO-OPERATIVE LTD",
            "Prudential Assurance"
        };
        String[] insurCode = {"I01", "I02", "I03", "I04", "I05", "I06", "I07", "I08"};
        for (int i = 0; i < insurName.length; i++) {
            Organization o = new Organization();
            o.setType(EnumUtils.BillType.INSUR);
            o.setName(insurName[i]);
            o.setShortCode(insurCode[i]);
            o.setPartnerBankCode("013");
            o.setPartnerBankAccount("8000001" + i);
            billBean.createOrganization(o);
        }

        String[] securName = {
            "DBS Vickers Securities (Singapore) Pte Ltd"
        };
        String[] securCode = {"S01"};
        for (int i = 0; i < securName.length; i++) {
            Organization o = new Organization();
            o.setType(EnumUtils.BillType.SECUR);
            o.setName(securName[i]);
            o.setShortCode(securCode[i]);
            o.setPartnerBankCode("013");
            o.setPartnerBankAccount("9000001" + i);
            billBean.createOrganization(o);
        }

        initBankEntities();
    }

    private void initBankEntities() {
        String[] bankNames = {
            "AUSTRALIA & NEW ZEALAND BANKING GROUP",
            "BANK OF CHINA LIMITED",
            "BNP PARIBAS",
            "CIMB BANK BERHAD",
            "CITIBANK NA",
            "DEUTSCHE BANK AG",
            "FAR EASTERN BANK LTD",
            "HL BANK",
            "HSBC (Corporate)",
            "HSBC (Personal)",
            "MALAYAN BANKING BHD",
            "MIZUHO BANK LIMITED",
            "OVERSEA-CHINESE BANKING CORPN LTD",
            "RHB BANK BERHAD",
            "STANDARD CHARTERED BANK",
            "SUMITOMO MITSUI BANKING CORPORATION",
            "THE BANK OF TOKYO-MITSUBISHI UFJ, LTD",
            "THE ROTAL BANK OF SCOTLAND N.V.",
            "UNITED OVERSEAS BANK LTD"
        };
        String[] bankCodes = {
            "020", "002", "003", "004", "005", "006", "007", "008", "009", "010", "011", "012", "013", "014", "015", "016", "017", "018", "019",};

        for (int i = 0; i < bankNames.length; i++) {
            BankEntity b = new BankEntity();
            b.setBankCode(bankCodes[i]);
            b.setInFast(Boolean.TRUE);
            b.setName(bankNames[i]);
            b.setStatus(EnumUtils.StatusType.ACTIVE);
            billBean.createBankEntity(b);

            Organization o = new Organization();
            o.setType(EnumUtils.BillType.CARD);
            o.setShortCode("O" + bankCodes[i]);
            o.setName(bankNames[i]);
            o.setPartnerBankCode(bankCodes[i]);
            billBean.createOrganization(o);
        }
    }
}
