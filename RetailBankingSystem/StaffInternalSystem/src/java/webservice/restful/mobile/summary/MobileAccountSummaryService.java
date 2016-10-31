/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.mobile.summary;

import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.dams.CustomerDepositSessionBeanLocal;
import ejb.session.dams.MobileAccountSessionBeanLocal;
import ejb.session.loan.LoanAccountSessionBeanLocal;
import entity.customer.MainAccount;
import entity.dams.account.CustomerDepositAccount;
import entity.dams.account.CustomerFixedDepositAccount;
import entity.dams.account.MobileAccount;
import entity.loan.LoanAccount;
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

/**
 *
 * @author leiyang
 */
@Path("mobile_account_summary")
public class MobileAccountSummaryService {

    @Context
    private UriInfo context;

    @EJB
    private LoginSessionBeanLocal loginBean;
    @EJB
    private MobileAccountSessionBeanLocal mobileBean;
    @EJB
    private CustomerDepositSessionBeanLocal depositBean;
    @EJB
    private LoanAccountSessionBeanLocal loanAccountBean;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getAccountSummary(
            @FormParam("username") String username
    ) {
        System.out.println("Received username:" + username);
        System.out.println("Received POST http with url: /mobile_account_summary");
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
            
            MobileAccount mobileAccount = mobileBean.getMobileAccountByUserId(ma.getUserID());
            // request to set up mobile password
            AccountDTO mobileAccountDTO = new AccountDTO();
            mobileAccountDTO.setAccountBalance(mobileAccount.getBalance().setScale(2).toString());
            mobileAccountDTO.setAccountName("Mobile Account");
            mobileAccountDTO.setAccountNumber(mobileAccount.getAccountNumber());
            mobileAccountDTO.setAccountType("MOBILE");
            
            accountSummaryDTO.setMobileAccount(mobileAccountDTO);
            
            List<CustomerDepositAccount> savingsAccounts = depositBean.getAllNonFixedCustomerAccounts(ma.getId());
            for (CustomerDepositAccount a : savingsAccounts) {
                AccountDTO dto = new AccountDTO();
                dto.setAccountBalance(a.getBalance().setScale(2).toString());
                dto.setAccountName(a.getProduct().getName());
                dto.setAccountNumber(a.getAccountNumber());
                dto.setAccountType("SAVING");
                accountSummaryDTO.getDepositAccounts().add(dto);
            }
            
            List<CustomerFixedDepositAccount> fixedAccounts = depositBean.getAllFixedCustomerAccounts(ma.getId());
            for (CustomerFixedDepositAccount a : fixedAccounts) {
                AccountDTO dto = new AccountDTO();
                dto.setAccountBalance(a.getBalance().setScale(2).toString());
                dto.setAccountName(a.getProduct().getName());
                dto.setAccountNumber(a.getAccountNumber());
                dto.setAccountType("FIXED");
                accountSummaryDTO.getFixedDepositAccounts().add(dto);
            }
            
            List<LoanAccount> loanAccounts = loanAccountBean.getActiveLoanAccountListByMainAccountId(ma.getId());
            for (LoanAccount a : loanAccounts) {
                AccountDTO dto = new AccountDTO();
                dto.setAccountBalance(a.getOutstandingPrincipal().toString());
                dto.setAccountName(a.getLoanProduct().getProductName());
                dto.setAccountNumber(a.getAccountNumber());
                dto.setAccountType("LOAN");
                accountSummaryDTO.getLoanAccounts().add(dto);
            }
            
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
