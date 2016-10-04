/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile;

import ejb.session.common.LoginSessionBeanLocal;
import entity.customer.MainAccount;
import entity.dams.account.MobileAccount;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.primefaces.json.JSONObject;
import server.utilities.HashPwdUtils;

/**
 *
 * @author leiyang
 */
@Path("mobile_login")
public class MobileUserLoginService {

    @Context
    private UriInfo context;

    @EJB
    private LoginSessionBeanLocal loginBean;

    // Works
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@QueryParam("username") String username, @QueryParam("password") String password) {
        System.out.println("Received username:" + username);
        System.out.println("Received password:" + password);
        System.out.println("Received GET http");
        // TODO: Some authentication
        UserLoginDTO user = new UserLoginDTO();
        user.setUsername("Test");
        String jsonString = new JSONObject(user).toString();
        return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
    }

    // Works
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response clearCC(
            @FormParam("username") String username,
            @FormParam("password") String password
    ) {
        System.out.println("Received username:" + username);
        System.out.println("Received password:" + password);
        System.out.println("Received POST http");
        String jsonString = null;
        try {
            MainAccount ma = loginBean.loginAccount(username, HashPwdUtils.hashPwd(password));
            if (ma != null) {
                MobileAccount mobileAccount = ma.getMobileAccount();
                // request to set up mobile password
                UserLoginDTO user = new UserLoginDTO();
                user.setId(ma.getId().toString());
                user.setUsername(ma.getUserID());
                user.setMobileNumber(ma.getCustomer().getPhone());
                user.setFirstName(ma.getCustomer().getFirstname());
                user.setLastName(ma.getCustomer().getLastname());
                if (mobileAccount == null) {
                    user.setMobilePassword("");
                } else {
                    user.setMobilePassword(mobileAccount.getPassword());
                }
                jsonString = new JSONObject(user).toString();
                return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
            } else {
                ErrorDTO err = new ErrorDTO();
                err.setCode(-1);
                err.setError("No such user found!");
                jsonString = new JSONObject(err).toString();
                return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
            }
        } catch (Exception e) {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("No user");
            jsonString = new JSONObject(err).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        }
    }
}
