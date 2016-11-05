/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.transfer;

import ejb.session.bill.BillSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.webservice.WebserviceSessionBeanLocal;
import entity.bill.BillingOrg;
import entity.common.BillTransferRecord;
import entity.common.TransactionRecord;
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
import server.utilities.DateUtils;
import server.utilities.EnumUtils;
import server.utilities.GenerateAccountAndCCNumber;
import webservice.restful.mobile.ErrorDTO;
import webservice.restful.mobile.TransferDTO;

/**
 *
 * @author leiyang
 */
@Path("mobile_pay_bill")
public class MobilePayBillService {

    @EJB
    private BillSessionBeanLocal billBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private WebserviceSessionBeanLocal webserviceBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response payBill(
            @FormParam("fromAccountNumber") String fromAccountNumber,
            @FormParam("billId") String billId,
            @FormParam("amount") String amount
    ) {
        System.out.println("Received fromAccountNumber:" + fromAccountNumber);
        System.out.println("Received billId:" + billId);
        System.out.println("Received amount:" + amount);
        System.out.println("Received POST http mobile_pay_own_cc_normal");
        String jsonString = null;

        DepositAccount da = depositBean.getAccountFromId(fromAccountNumber);
        BigDecimal transferAmount = new BigDecimal(amount);
        if (da.getBalance().compareTo(transferAmount) < 0) {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("Not Enough Balance");
            jsonString = new JSONObject(err).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        }

        BillingOrg bo = billBean.getBillingOrganizationById(Long.parseLong(billId));

        BillTransferRecord btr = new BillTransferRecord();
        btr.setBillReferenceNumber(bo.getBillReference());// it will be credit card number
        btr.setOrganizationName(bo.getOrganization().getName());
        btr.setPartnerBankCode(bo.getOrganization().getPartnerBankCode());
        btr.setSettled(false);
        btr.setAmount(transferAmount);
        btr.setShortCode(bo.getOrganization().getShortCode());
        btr.setReferenceNumber(GenerateAccountAndCCNumber.generateReferenceNumber());
        btr.setActionType(EnumUtils.TransactionType.BILL);
        System.out.println("Bill Payment clearing");
        webserviceBean.billingClearingSACH(btr);
        da.removeBalance(transferAmount);
        depositBean.updateAccount(da);

        TransactionRecord record = depositBean.latestTransactionFromAccountNumber(fromAccountNumber);
        TransferDTO t = new TransferDTO();
        t.setTransferAmount(record.getAmount().setScale(2).toString());
        t.setReferenceNumber(record.getReferenceNumber());
        t.setTransferType(record.getActionType().toString());
        t.setTransferDate(DateUtils.readableDate(record.getCreationDate()));
        t.setTransferAccount(fromAccountNumber);
        jsonString = new JSONObject(t).toString();
        return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
    }
}
