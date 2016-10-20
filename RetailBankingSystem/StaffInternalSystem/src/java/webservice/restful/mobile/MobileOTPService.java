/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile;

import ejb.session.common.OTPSessionBeanLocal;
import entity.common.OneTimePassword;
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

/**
 *
 * @author leiyang
 */
@Path("mobile_otp")
public class MobileOTPService {

    @Context
    private UriInfo context;

    @EJB
    private OTPSessionBeanLocal otpBean;

    // Works
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response clearCC(
            @FormParam("password") String otpCode,
            @FormParam("mobileNumber") String mobileNumber
    ) {
        System.out.println("Received otpCode:" + otpCode);
        System.out.println("Received mobileNumber:" + mobileNumber);
        System.out.println("Received POST http /mobile_otp");
        String jsonString = null;
        try {
            System.out.println("Receiving otp...");
            OneTimePassword otp = otpBean.getOTPByPhoneNumber(mobileNumber);
            System.out.println("otp received: " + otp != null);
            if (otp != null) {
                if (otp.getPassword().equals(otpCode)) {
                    // TODO: Resend logic
                    ErrorDTO err = new ErrorDTO();
                    err.setCode(0);
                    err.setError("Verified");
                    otpBean.remove(otp);
                    jsonString = new JSONObject(err).toString();
                    return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
                } else {
                    ErrorDTO err = new ErrorDTO();
                    err.setCode(-2);
                    err.setError("Wrong code entered!");
                    jsonString = new JSONObject(err).toString();
                    return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
                }

            } else {
                ErrorDTO err = new ErrorDTO();
                err.setCode(-1);
                err.setError("Password expired!");
                jsonString = new JSONObject(err).toString();
                return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
            }
        } catch (Exception e) {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("Password expired");
            jsonString = new JSONObject(err).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        }
    }
}
