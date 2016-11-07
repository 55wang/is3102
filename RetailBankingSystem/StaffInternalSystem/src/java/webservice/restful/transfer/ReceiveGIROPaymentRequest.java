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
// REMARK: Change receiving url @Path("This is receiving url, need to change for new service")
@Path("mbs_receive_giro_request")
public class ReceiveGIROPaymentRequest {

    // REMARK: Import your ejb sessions bean
    @EJB
    private BillSessionBeanLocal billBean;
    @EJB
    private WebserviceSessionBeanLocal serviceBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;

    // REMARK: receiving in POST method
    // REMARK: DO NOT CHANGE, just copy
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response netSettlement(
            // REMARK: match with sender Form, key name should be exactly the same
            @FormParam("referenceNumber") String referenceNumber,
            @FormParam("amount") String amount,
            @FormParam("shortCode") String shortCode,
            @FormParam("billReferenceNumber") String billReferenceNumber
    ) {
        // REMARK: All the print lines, 
        // TODO: Need to improve the readablity
        System.out.println(".");
        System.out.println("[MBS]");
        System.out.println(".      Received GIRO request from SACH...");
        System.out.println(".      Received Reference Number:" + referenceNumber);
        System.out.println(".      Received Amount:" + amount);
        System.out.println(".      Received Bill Reference Number:" + billReferenceNumber);
        System.out.println(".      Received Short Code:" + shortCode);
        System.out.println(".      Received POST http mbs_receive_giro_request");

        // REMARK: EJB do the business logics
        GiroArrangement ga = billBean.getGiroArrByReferenceNumberAndOrgCode(billReferenceNumber, shortCode);
        if (ga == null) {
            // REMARK: Return failed message
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("Account Not Found");
            return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
        } else if (ga.getBillLimit().compareTo(Double.parseDouble(amount)) < 0) {
            // REMARK: Return failed message
            ErrorDTO err = new ErrorDTO();
            err.setCode(-2);
            err.setError("Exceed limit");
            return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
        } else {
            // REMARK: Return SUCCESS MESSAGE and update any entity
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
            depositBean.payBillFromAccount(da, new BigDecimal(amount));

            ErrorDTO err = new ErrorDTO();
            err.setCode(0);
            err.setError("SUCCESS");
            return Response.ok(new JSONObject(err).toString(), MediaType.APPLICATION_JSON).build();
        }
    }

}
