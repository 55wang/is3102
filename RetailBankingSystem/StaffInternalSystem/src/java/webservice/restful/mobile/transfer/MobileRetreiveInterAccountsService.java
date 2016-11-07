/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.transfer;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.dams.MobileAccountSessionBeanLocal;
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
import webservice.restful.mobile.ErrorDTO;
import webservice.restful.mobile.summary.AccountDTO;
import webservice.restful.mobile.summary.AccountSummaryDTO;

/**
 *
 * @author leiyang
 */
@Path("mobile_deposit_accounts")
public class MobileRetreiveInterAccountsService {

    @Context
    private UriInfo context;

    @EJB
    private LoginSessionBeanLocal loginBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private MobileAccountSessionBeanLocal mobileBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getDepositAccount(
            @FormParam("username") String username
    ) {
        System.out.println("Received username:" + username);
        System.out.println("Received POST http with url: /mobile_deposit_accounts");
        String jsonString;
        MainAccount ma;
        try {
            ma = loginBean.getMainAccountByUserID(username);
        } catch (Exception e) {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("No user");
            jsonString = new JSONObject(err).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        }

        if (ma != null) {
            AccountSummaryDTO accountSummaryDTO = new AccountSummaryDTO();

            List<CustomerDepositAccount> savingsAccounts = depositBean.getAllNonFixedCustomerAccounts(ma.getId());
            for (CustomerDepositAccount a : savingsAccounts) {
                AccountDTO dto = new AccountDTO();
                dto.setAccountBalance(a.getBalance().setScale(2, RoundingMode.UP).toString());
                dto.setAccountName(a.getProduct().getName());
                dto.setAccountNumber(a.getAccountNumber());
                dto.setAccountType("SAVING");
                accountSummaryDTO.getDepositAccounts().add(dto);
            }

            MobileAccount mobileAccount = mobileBean.getMobileAccountByUserId(username);
            AccountDTO dto = new AccountDTO();
            dto.setAccountBalance(mobileAccount.getBalance().setScale(2, RoundingMode.UP).toString());
            dto.setAccountName("Mobile Account");
            dto.setAccountNumber(mobileAccount.getAccountNumber());
            dto.setAccountType("MOBILE");
            accountSummaryDTO.getDepositAccounts().add(dto);

            jsonString = new JSONObject(accountSummaryDTO).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        } else {
            ErrorDTO err = new ErrorDTO();
            err.setCode(-1);
            err.setError("No such user found!");
            jsonString = new JSONObject(err).toString();
            return Response.ok(jsonString, MediaType.APPLICATION_JSON).build();
        }
    }

}
