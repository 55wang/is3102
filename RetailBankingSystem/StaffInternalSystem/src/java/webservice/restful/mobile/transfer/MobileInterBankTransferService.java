/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.transfer;

import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.webservice.WebserviceSessionBeanLocal;
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
import util.exception.dams.DepositAccountNotFoundException;
import webservice.restful.mobile.ErrorDTO;

/**
 *
 * @author leiyang
 */
@Path("mobile_inter_bank_transfer")
public class MobileInterBankTransferService {

    @Context
    private UriInfo context;

    @EJB
    private TransferSessionBeanLocal transferBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private WebserviceSessionBeanLocal webserviceBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response interAccountTransfer(
            @FormParam("fromAccountNumber") String fromAccountNumber,
            @FormParam("toPayeeId") String toPayeeId,
            @FormParam("transferAmount") String transferAmount
    ) {
        System.out.println("Received fromAccountNumber:" + fromAccountNumber);
        System.out.println("Received toPayeeId:" + toPayeeId);
        System.out.println("Received transferAmount:" + transferAmount);
        System.out.println("Received POST http with url: /mobile_inter_bank_transfer");

        String jsonString;

        try {

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
            tr.setToBankCode(payee.getBankCode());
            tr.setToBranchCode("010");// dummy
            tr.setName(payee.getName());
            tr.setMyInitial(payee.getMyInitial());
            tr.setFromName(payee.getFromName());
            tr.setPurpose(EnumUtils.TransferPurpose.OTHERS);
            tr.setFromAccount(da);
            tr.setType(EnumUtils.PayeeType.LOCAL);
            tr.setActionType(EnumUtils.TransactionType.TRANSFER);

            System.out.println("FAST transfer clearing");
            webserviceBean.transferClearingFAST(tr);
            depositBean.transferFromAccount(da, amount);

            JSONObject jo = new JSONObject();
            TransactionRecord result = depositBean.latestTransactionFromAccountNumber(fromAccountNumber);
            jo.put("referenceNumber", result.getReferenceNumber());
            jsonString = jo.toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();

        } catch (DepositAccountNotFoundException e) {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-2);
            err.setError("Transfer Failed! Your Account Not Found!");
            jsonString = new JSONObject(err).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        }
    }

    private BigDecimal calculateTransferLimits(MainAccount ma) {
        BigDecimal todayTransferAmount = transferBean.getTodayBankTransferAmount(ma, EnumUtils.PayeeType.LOCAL);
        BigDecimal currentTransferLimit = new BigDecimal(ma.getTransferLimits().getDailyIntraBankLimit().toString());
        return currentTransferLimit.subtract(todayTransferAmount);
    }
}
