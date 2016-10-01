/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile;

import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
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
import webservice.restful.creditcard.CreditCardDTO;

/**
 *
 * @author leiyang
 */
@Path("mobile_login")
public class MobileUserLoginService {
    
    @Context
    private UriInfo context;
    
    // Works
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStringList(@QueryParam("username") String username) {
        System.out.println("Received username:" + username);

        System.out.println("Received GET http");
        // TODO: Some authentication
        UserDTO user = new UserDTO();
        user.setUsername("Test");
        String jsonString = new JSONObject(user).toString();
        return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
    }
    
    // Works
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response clearCC (
            @FormParam("username") String username
            ) {
        System.out.println("Received POST http");
        System.out.println(username);
        // TODO: Some authentication
        UserDTO user = new UserDTO();
        user.setUsername("Test");
        String jsonString = new JSONObject(user).toString();
        return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
    }
}
