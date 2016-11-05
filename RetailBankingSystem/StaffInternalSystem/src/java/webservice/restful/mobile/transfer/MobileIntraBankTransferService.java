/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.transfer;

import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import entity.bill.Payee;
import entity.common.TransactionRecord;
import entity.common.TransferRecord;
import entity.customer.MainAccount;
import entity.dams.account.DepositAccount;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.primefaces.json.JSONObject;
import server.utilities.EnumUtils;
import server.utilities.GenerateAccountAndCCNumber;
import webservice.restful.mobile.ErrorDTO;

/**
 *
 * @author leiyang
 */
@Path("mobile_intra_bank_transfer")
public class MobileIntraBankTransferService {
    
    @Context
    private UriInfo context;

    @EJB
    private TransferSessionBeanLocal transferBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response intraBankTransfer(
            @FormParam("fromAccountNumber") String fromAccountNumber,
            @FormParam("toPayeeId") String toPayeeId,
            @FormParam("transferAmount") String transferAmount
    ) {
        System.out.println("Received fromAccountNumber:" + fromAccountNumber);
        System.out.println("Received toPayeeId:" + toPayeeId);
        System.out.println("Received transferAmount:" + transferAmount);
        System.out.println("Received POST http with url: /mobile_intra_bank_transfer");
        
        String jsonString;
        DepositAccount da = depositBean.getAccountFromId(fromAccountNumber);
        MainAccount ma = da.getMainAccount();
        
        BigDecimal amount = new BigDecimal(transferAmount);
        BigDecimal limit = calculateTransferLimits(ma);
        
        if (amount.compareTo(limit) > 0) {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("Transfer Failed! Check your daily transaction limit");
            jsonString = new JSONObject(err).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        }
        
        Payee payee = transferBean.getPayeeById(Long.parseLong(toPayeeId));
        TransferRecord tr = new TransferRecord();
        
        tr.setAccountNumber(payee.getAccountNumber());
        tr.setReferenceNumber(GenerateAccountAndCCNumber.generateReferenceNumber());
        tr.setAmount(amount);
        tr.setName(payee.getAccountNumber());
        tr.setMyInitial(payee.getMyInitial());
        tr.setFromName(ma.getCustomer().getFullName());
        tr.setFromAccount(da);
        DepositAccount toAccount = depositBean.getAccountFromId(payee.getAccountNumber());
        tr.setToAccount(toAccount);
        tr.setType(EnumUtils.PayeeType.MERLION);
        tr.setActionType(EnumUtils.TransactionType.TRANSFER);
        transferBean.createTransferRecord(tr);
        
        String result = transferBean.transferFromAccountToAccount(fromAccountNumber, payee.getAccountNumber(), amount);
        
        if (result.equals("SUCCESS")) {
            JSONObject jo = new JSONObject();
            TransactionRecord trResult = depositBean.latestTransactionFromAccountNumber(fromAccountNumber);
            jo.put("referenceNumber", trResult.getReferenceNumber());
            jsonString = jo.toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        } else {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("Transfer Failed!");
            jsonString = new JSONObject(err).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        }
    }
    
    private BigDecimal calculateTransferLimits(MainAccount ma) {
        BigDecimal todayTransferAmount = transferBean.getTodayBankTransferAmount(ma, EnumUtils.PayeeType.MERLION);
        BigDecimal currentTransferLimit = new BigDecimal(ma.getTransferLimits().getDailyIntraBankLimit().toString());
        return currentTransferLimit.subtract(todayTransferAmount);
    }
    
}
