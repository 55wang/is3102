/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.transfer;

import ejb.session.bill.BillSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.webservice.WebserviceSessionBeanLocal;
import entity.bill.GiroArrangement;
import entity.common.BillTransferRecord;
import entity.dams.account.DepositAccount;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.primefaces.json.JSONObject;
import server.utilities.EnumUtils;
import webservice.restful.mobile.ErrorDTO;

/**
 *
 * @author leiyang
 */
@Path("mbs_receive_giro_request")
public class ReceiveGIROPaymentRequest {
    
    @EJB
    private BillSessionBeanLocal billBean;
    @EJB
    private WebserviceSessionBeanLocal serviceBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response netSettlement(
            @FormParam("referenceNumber") String referenceNumber,
            @FormParam("amount") String amount,
            @FormParam("shortCode") String shortCode,
            @FormParam("billReferenceNumber") String billReferenceNumber
    ) {
        
        System.out.println("Received referenceNumber:" + referenceNumber);
        System.out.println("Received amount:" + amount);
        System.out.println("Received billReferenceNumber:" + billReferenceNumber);
        System.out.println("Received shortCode:" + shortCode);
        System.out.println("Received POST http mbs_receive_giro_request");

        GiroArrangement ga = billBean.getGiroArrByReferenceNumberAndOrgCode(billReferenceNumber, shortCode);
        if (ga == null) {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("Account Not Found");
            return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
        } else if (ga.getBillLimit().compareTo(Double.parseDouble(amount)) < 0) {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-2);
            err.setError("Exceed limit");
            return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
        } else {
            System.out.println("Sending back mbs_receive_giro_request response");

            BillTransferRecord btr = new BillTransferRecord();
            btr.setActionType(EnumUtils.TransactionType.BILL);
            btr.setBillReferenceNumber(billReferenceNumber);
            btr.setFromAccount(ga.getDepositAccount());
            btr.setShortCode(shortCode);
            btr.setOrganizationName(ga.getOrganization().getName());
            btr.setPartnerBankCode(ga.getOrganization().getPartnerBankCode());
            btr.setAmount(new BigDecimal(amount));
            btr.setReferenceNumber(referenceNumber);
            
            serviceBean.billingClearingSACH(btr);
            DepositAccount da = ga.getDepositAccount();
            da.removeBalance(new BigDecimal(amount));
            depositBean.updateAccount(da);
            
            ErrorDTO err = new ErrorDTO();
            err.setCode(0);
            err.setError("SUCCESS");
            return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
        }
    }
    
}
