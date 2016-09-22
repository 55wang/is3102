/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.cms;

import ejb.session.cms.CustomerCaseSessionBeanLocal;
import entity.customer.CustomerCase;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import server.utilities.EnumUtils;
import utils.CommonUtils;
import utils.MessageUtils;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "staffCustomerCaseManagedBean")
@ViewScoped
public class StaffCustomerCaseManagedBean implements Serializable{
    @EJB
    private CustomerCaseSessionBeanLocal customerCaseSessionBean;
    
    private List<CustomerCase> cases;
    private String searchText;
    
    private List<String> caseStatusList = CommonUtils.getEnumList(EnumUtils.CaseStatus.class);
    private List<String> issueFieldList = CommonUtils.getEnumList(EnumUtils.IssueField.class);
    
    /**
     * Creates a new instance of StaffCutomerCaseManagedBean
     */
    public StaffCustomerCaseManagedBean() {
    }
    
    public void search() {
        if (searchText.isEmpty()){
            cases = customerCaseSessionBean.getAllCaseUnderCertainStaff(SessionUtils.getStaff());
        }else{
            CustomerCase tempCase = customerCaseSessionBean.searchCaseByID(searchText);
            cases = new ArrayList<CustomerCase>();
            cases.add(tempCase);
        }
    }
    
    public void update(CustomerCase cc) {
        Boolean result = customerCaseSessionBean.updateCase(cc);
        if (result == false) {
            MessageUtils.displayError("Case not found!");
        } else {
            MessageUtils.displayInfo("Case information is updated!");
        }
    }
    
    @PostConstruct
    public void setCases() {
        this.cases = customerCaseSessionBean.getAllCaseUnderCertainStaff(SessionUtils.getStaff());
    }
    
    public List<CustomerCase> getCases() {
        return cases;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<String> getCaseStatusList() {
        return caseStatusList;
    }

    public void setCaseStatusList(List<String> caseStatusList) {
        this.caseStatusList = caseStatusList;
    }

    public List<String> getIssueFieldList() {
        return issueFieldList;
    }

    public void setIssueFieldList(List<String> issueFieldList) {
        this.issueFieldList = issueFieldList;
    }
    
}
