/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.setting;

import ejb.session.bill.TransferSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.dams.MobileAccountSessionBeanLocal;
import entity.common.TransactionRecord;
import entity.dams.account.MobileAccount;
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
import webservice.restful.mobile.ErrorDTO;

/**
 *
 * @author leiyang
 */
@Path("mobile_save_setting")
public class MobileSaveSettingService {

    @Context
    private UriInfo context;

    @EJB
    private MobileAccountSessionBeanLocal mobileBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response interAccountTransfer(
            @FormParam("mobileNumber") String mobileNumber,
            @FormParam("notification") String notification,
            @FormParam("deals") String deals,
            @FormParam("sound") String sound,
            @FormParam("textAlert") String textAlert,
            @FormParam("ccReminder") String ccReminder,
            @FormParam("giroAlert") String giroAlert,
            @FormParam("general") String general,
            @FormParam("loanAlert") String loanAlert
    ) {
        System.out.println("Received POST http with url: /mobile_save_setting");
        System.out.println("    Settings for mobileNumber:-----" + mobileNumber);
        System.out.println("                Notifications:-----" + notification);
        System.out.println("    General & Security Alerts:-----" + general);
        System.out.println("              MBD Deals Alert:-----" + deals);
        System.out.println("                Sound Enabled:-----" + sound);
        System.out.println("          Text Message Alerts:-----" + textAlert);
        System.out.println(" Credit Card Payment Reminder:-----" + ccReminder);
        System.out.println("                   GIRO Alert:-----" + giroAlert);
        System.out.println("         Loan Repayment Alert:-----" + loanAlert);

        String jsonString;

        ErrorDTO err = new ErrorDTO();
        err.setCode(0);
        err.setError("Settings Saved!");
        jsonString = new JSONObject(err).toString();
        return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
    }

}
