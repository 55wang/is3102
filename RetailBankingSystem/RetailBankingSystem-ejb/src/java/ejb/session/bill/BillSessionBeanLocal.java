/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.bill;

import entity.bill.BankEntity;
import entity.bill.BillingOrg;
import entity.bill.GiroArrangement;
import entity.bill.Organization;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leiyang
 */
@Local
public interface BillSessionBeanLocal {
    // org
    public Organization createOrganization(Organization o);
    public Organization updateOrganization(Organization o);
    public Organization getOrganizationById(Long id);
    public List<Organization> getActiveListOrganization();
    public List<Organization> getCreditCardOrganization();
    // bank entity
    public BankEntity createBankEntity(BankEntity b);
    public BankEntity updateBankEntity(BankEntity b);
    public BankEntity getBankEntityByCode(String code);
    public BankEntity getBankEntityById(Long id);
    public List<BankEntity> getActiveListBankEntities();
    // bill org
    public BillingOrg createBillingOrganization(BillingOrg o);
    public BillingOrg getBillingOrganizationById(Long id);
    public String deleteBillingOrganizationById(Long id);
    public List<BillingOrg> getBillingOrgMainAccountId(Long id);
    public List<BillingOrg> getCreditCardBillingMainAccountId(Long id);
    // giro
    public GiroArrangement createGiroArr(GiroArrangement o);
    public GiroArrangement updateGiroArr(GiroArrangement o);
    public GiroArrangement getGiroArrById(Long id);
    public GiroArrangement getGiroArrByReferenceNumberAndOrgCode(String referenceNumber, String shortCode);
    public String deleteGiroArrById(Long id);
    public List<GiroArrangement> getGiroArrsByMainAccountId(Long id);
}
