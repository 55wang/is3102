/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.request;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.dams.MobileAccountSessionBeanLocal;
import ejb.session.loan.LoanAccountSessionBeanLocal;
import entity.common.PayMeRequest;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.CustomerFixedDepositAccount;
import entity.dams.account.MobileAccount;
import entity.loan.LoanAccount;
import java.math.RoundingMode;
import java.util.List;
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
import server.utilities.DateUtils;

/**
 *
 * @author leiyang
 */
@Path("mobile_payme_request_summary")
public class MobilePaymeRequestSummaryService {

    @Context
    private UriInfo context;
    @EJB
    private MobileAccountSessionBeanLocal mobileBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getPaymeSummary(
            @FormParam("mobileNumber") String mobileNumber
    ) {
        System.out.println("Received mobileNumber:" + mobileNumber);
        System.out.println("Received POST http with url: /mobile_payme_request_summary");
        
        String jsonString;

        PaymeRequestSummaryDTO requestSummaryDTO = new PaymeRequestSummaryDTO();

        List<PayMeRequest> receivedRequests = mobileBean.getTotalRequestReceivedByMobileNumber(mobileNumber);
        System.out.println("Number of receivedRequests:" + receivedRequests.size());
        List<PayMeRequest> sentRequests = mobileBean.getTotalRequestSentByMobileNumber(mobileNumber);
        System.out.println("Number of sentRequests:" + sentRequests.size());
        
        for (PayMeRequest p : receivedRequests) {
            PaymeRequestDTO dto = new PaymeRequestDTO();
            dto.setRequestId(p.getId().toString());
            dto.setFromAccountNumber(p.getFromAccount().getAccountNumber());
            dto.setToAccountNumber(p.getToAccount().getAccountNumber());
            dto.setFromName(p.getFromAccount().getMainAccount().getCustomer().getFullName());
            dto.setToName(p.getToAccount().getMainAccount().getCustomer().getFullName());
            dto.setRequestAmount(p.getAmount().toString());
            dto.setRequestDate(DateUtils.readableDate(p.getCreationDate()));
            dto.setRequestPaid(p.getPaid().toString());
            dto.setRequestRemark(p.getRemark());
            requestSummaryDTO.getReceivedRequests().add(dto);
        }
        
        for (PayMeRequest p : sentRequests) {
            PaymeRequestDTO dto = new PaymeRequestDTO();
            dto.setRequestId(p.getId().toString());
            dto.setFromAccountNumber(p.getFromAccount().getAccountNumber());
            dto.setToAccountNumber(p.getToAccount().getAccountNumber());
            dto.setFromName(p.getFromAccount().getMainAccount().getCustomer().getFullName());
            dto.setToName(p.getToAccount().getMainAccount().getCustomer().getFullName());
            dto.setRequestAmount(p.getAmount().toString());
            dto.setRequestDate(DateUtils.readableDate(p.getCreationDate()));
            dto.setRequestPaid(p.getPaid().toString());
            dto.setRequestRemark(p.getRemark());
            requestSummaryDTO.getSentRequests().add(dto);
        }

        jsonString = new JSONObject(requestSummaryDTO).toString();
        return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();

    }
}
