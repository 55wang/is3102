/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import org.primefaces.json.JSONObject;

/**
 *
 * @author leiyang
 */
@Path("credit_card")
public class CreditCardService {

    @Context
    private UriInfo context;

//    @EJB
//    StudentSessionBeanLocal studentSessionBeanLocal;
    public CreditCardService() {
        System.out.println("CreditCardService");
    }

//    @PUT
//    @Consumes(MediaType.APPLICATION_XML)
//    @Produces(MediaType.APPLICATION_XML)
//    public Student createStudent(JAXBElement<Student> student)
//    {
//        return studentSessionBeanLocal.createStudent(student.getValue());
//    }
    @GET
    @Produces(MediaType.APPLICATION_JSON) 
    public JsonArray getStringList(@QueryParam("accountNumber") String accountNumber) {
        System.out.println("Getting String list with account number:" + accountNumber);
        JsonArrayBuilder arrayBld = Json.createArrayBuilder();
        List<String> strList = new ArrayList<>();
        strList.add("test 1");
        strList.add("test 2");
        strList.add("test 3");
        strList.add("test 4");
        strList.add("test 5");
        for (String str : strList) {
            arrayBld.add(str);
        }
        
        return arrayBld.build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateStudent(@FormParam("accountNumber") String accountNumber) {
        // get value from form
        System.out.println("Getting String list with account number:" + accountNumber);
        // return value
        CreditCardDTO c = new CreditCardDTO();
        c.setAmount("123355333.00");
        c.setName("Lei Yang");
        c.setCreditCardNumber("4545454545454545");
        String jsonString = new JSONObject(c).toString();
        
        return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
    }
}
