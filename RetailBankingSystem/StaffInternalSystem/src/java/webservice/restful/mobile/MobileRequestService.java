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
@Path("request")
public class MobileRequestService {
    
    @EJB
    private MobileAccountSessionBeanLocal mobileBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response mobileTransfer(
            @FormParam("fromAccount") String fromAccount,
            @FormParam("toAccount") String toAccount,
            @FormParam("amount") String amount,
            @FormParam("remark") String remark
    ) {
        System.out.println("Received fromAccount:" + fromAccount);
        System.out.println("Received toAccount:" + toAccount);
        System.out.println("Received amount:" + amount);
        System.out.println("Received remark:" + remark);
        System.out.println("Received POST http request");
        String jsonString = null;
        MobileAccount fromMobileAccount = mobileBean.getMobileAccountByMobileNumber(fromAccount);
        MobileAccount toMobileAccount = mobileBean.getMobileAccountByMobileNumber(toAccount);
        // request to set up mobile password
        if (toMobileAccount != null) {
            BigDecimal actualAmount = new BigDecimal(amount);
            // payme request
            PayMeRequest pmr = new PayMeRequest();
            pmr.setAmount(actualAmount);
            pmr.setFromAccount(fromMobileAccount);
            pmr.setToAccount(toMobileAccount);
            pmr.setRemark(remark);
            pmr = mobileBean.createPayMeRequest(pmr);
            // update mobile account
            fromMobileAccount.getSentRequest().add(pmr);
            toMobileAccount.getReceivedRequest().add(pmr);
            mobileBean.updateMobileAccount(fromMobileAccount);
            mobileBean.updateMobileAccount(toMobileAccount);
            
            ErrorDTO err = new ErrorDTO();
            err.setCode(0);
            err.setError("Request Sent!");
            jsonString = new JSONObject(err).toString();
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
