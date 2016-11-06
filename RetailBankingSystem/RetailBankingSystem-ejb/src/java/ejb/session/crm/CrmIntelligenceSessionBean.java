/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.crm;

import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import server.utilities.EnumUtils;

/**
 *
 * @author wang
 */
@Stateless
public class CrmIntelligenceSessionBean implements CrmIntelligenceSessionBeanLocal {

    @PersistenceContext(unitName = "RetailBankingSystem-ejbPU")
    private EntityManager em;

    @Override
    public Double getCustomerChurnRate(Date startDate, Date endDate) {
        return 0.0;
    }

    @Override
    public Double getCustomerAvgCLV(Date startDate, Date endDate) {
        return 0.0;
    }

    @Override
    public Double getCustomerAvgAge(Date startDate, Date endDate) {
        return 0.0;
    }

    @Override
    public Double getCustomerAvgDepositSavingAmount(Date startDate, Date endDate) {
        return 0.0;
    }

    @Override
    public Double getCustomerAvgLoanAmount(Date startDate, Date endDate) {
        return 0.0;
    }
}
