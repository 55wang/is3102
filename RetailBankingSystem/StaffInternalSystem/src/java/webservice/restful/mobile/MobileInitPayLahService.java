/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile;

import ejb.session.dams.MobileAccountSessionBeanLocal;
import entity.common.PayMeRequest;
import entity.common.TransactionRecord;
import entity.dams.account.MobileAccount;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
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

/**
 *
 * @author leiyang
 */
@Path("init_pay_lah")
public class MobileInitPayLahService {

    @EJB
    private MobileAccountSessionBeanLocal mobileBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response clearCC(
            @FormParam("username") String username
    ) {
        System.out.println("Received username:" + username);
        System.out.println("Received POST http init_pay_lah");
        String jsonString = null;
        MobileAccount mobileAccount = mobileBean.getMobileAccountByUserId(username);
        // request to set up mobile password
        if (mobileAccount != null) {
            List<PayMeRequest> requests = mobileBean.getTotalUnpaidRequestReceivedByMobileNumber(mobileAccount.getAccountNumber());
            
            TransactionRecord t = mobileBean.latestTransactionFromMobileAccount(mobileAccount);
            System.out.println(t == null);
            InitPayLahDTO p = new InitPayLahDTO();
            p.setBalance(mobileAccount.getBalance().setScale(2, RoundingMode.UP).toString());
            p.setWalletLimit("999.00");//placeholder
            p.setNoNewReq("" + requests.size());
            BigDecimal limit = mobileBean.dailyTransferLimitLeft(mobileAccount.getAccountNumber());
            p.setTransferLimit(limit.setScale(2, RoundingMode.UP).toString());
            if (t != null) {
                p.setTransferType(t.getActionType().toString());
                p.setTransferAmount(t.getAmount().setScale(2, RoundingMode.UP).toString());
                p.setTransferDate(DateUtils.readableDate(t.getCreationDate()));
                p.setTransferAccount("Wallet - " + mobileAccount.getPhoneNumber());
            } else {
                p.setTransferType("");
                p.setTransferAmount("");
                p.setTransferDate("");
                p.setTransferAccount("");
            }
            jsonString = new JSONObject(p).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        } else {
            // error
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("No such user found!");
            jsonString = new JSONObject(err).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        }
    }
}
