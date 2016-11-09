/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package staff.cms;

import ejb.session.cms.CustomerCaseSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import ejb.session.common.LoginSessionBeanLocal;
import ejb.session.staff.StaffAccountSessionBeanLocal;
import entity.customer.CustomerCase;
import entity.customer.Issue;
import entity.customer.MainAccount;
import entity.staff.StaffAccount;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.IssueField;
import server.utilities.CommonUtils;
import server.utilities.ConstantUtils;
import util.exception.cms.AllCustomerCaseException;
import util.exception.cms.CancelCustomerCaseException;
import util.exception.cms.CustomerCaseNotFoundByTitleException;
import util.exception.cms.CustomerCaseNotFoundException;
import util.exception.cms.DuplicateCaseExistException;
import utils.MessageUtils;
import utils.RedirectUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "customerCaseCounterManagedBean")
@ViewScoped
public class CustomerCaseCounterManagedBean implements Serializable {

    @EJB
    private LoginSessionBeanLocal loginSessionBean;
    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private CustomerCaseSessionBeanLocal customerCaseSessionBean;
    @EJB
    private StaffAccountSessionBeanLocal staffBean;

    private MainAccount mainAccount;
    private String customerIC;
    private CustomerCase customerCase = new CustomerCase();
    private List<Issue> issues = new ArrayList<>();
    private Issue newIssue = new Issue();
    private Boolean issuePage = false;
    private int numOfIssues = 0;
    private String directoryPath;
    private String searchType = "viewAllCases";
    private String viewAllCases = "viewAllCases";
    private String searchByCaseID = "CaseID";
    private String searchByCaseTitle = "CaseTitle";
    private List<CustomerCase> searchResultList;
    private List<CustomerCase> allCaseList;
    private String searchCaseID;
    private String searchCaseTitle;
    private List<String> issueFieldList;

    private final static String NOTIFY_CHANNEL = "/notify";//TODO: notify to specific staff roll

    /**
     * Creates a new instance of CustomerCaseManagedBean
     */
    public CustomerCaseCounterManagedBean() {
    }

    @PostConstruct
    public void setMainAccount() {
        this.issueFieldList = CommonUtils.getEnumList(EnumUtils.IssueField.class);
        this.issueFieldList.remove(IssueField.CHARGEBACK.toString());

        try {
            this.allCaseList = customerCaseSessionBean.getAllCase();
        } catch (AllCustomerCaseException e) {
            this.allCaseList = new ArrayList<>();
        }
    }

    public void retrieveMainAccount() {
        try {
            this.mainAccount = loginSessionBean.getMainAccountByUserIC(customerIC);
        } catch (Exception e) {
            this.mainAccount = null;
        }

        MessageUtils.displayInfo("Customer Main Account Retrieved!");
    }

    public void goToIssuePage() {
        issuePage = true;
    }

    public void addIssue() {
        issues.add(newIssue);
        newIssue.setCustomerCase(customerCase);
        newIssue = new Issue();
        issuePage = false;
        numOfIssues++;
    }

    public void saveCase() {
        try {

            StaffAccount sa = staffBean.getAccountByUsername(ConstantUtils.RELATIONSHIP_MANAGER_USERNAME);

            Date date = new Date();
            customerCase.setCreateDate(date);
            customerCase.setIssues(issues);
            customerCase.setMainAccount(mainAccount);
            customerCase.setStaffAccount(sa);
            mainAccount.addCase(customerCase);

            customerCaseSessionBean.createCase(customerCase);

            try {
                emailServiceSessionBean.sendNewCaseConfirmationToCustomer(mainAccount.getCustomer().getEmail(), customerCase);
            } catch (Exception ex) {
                System.out.println("CustoemrCaseManagedBean.saveCase: " + ex.toString());
            }

            System.out.println("CustoemrCaseManagedBean.saveCase.NewCaseNotification sending");
            EventBus eventBus = EventBusFactory.getDefault().eventBus();
            FacesMessage m = new FacesMessage("New Case", "A new case with ID " + customerCase.getId() + " has been created");
            eventBus.publish(NOTIFY_CHANNEL, m);

            RedirectUtils.redirect("view_case.xhtml");

        } catch (DuplicateCaseExistException e) {
            System.out.println("DuplicateCaseExistException thrown at CustomerCaseCounterManagedBean.java");
        }
    }

    public void removeIssue(String issueID) {
        for (int i = 0; i < issues.size(); i++) {
            if (issues.get(i).toString().equals(issueID)) {
                issues.remove(i);
            }
        }
    }

    public void uploadPhoto(FileUploadEvent e) throws IOException {
        System.out.println("CustomerCaseManagedBean.uploadPhoto: start uploading");
        UploadedFile uploadedPhoto = e.getFile();
        String filename = FilenameUtils.getName(uploadedPhoto.getFileName());

        byte[] bytes = null;

        if (uploadedPhoto != null) {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            directoryPath = ec.getRealPath("//WEB-INF//files//");
            bytes = uploadedPhoto.getContents();
            String filepath = directoryPath + "/" + filename;
            System.out.println("CustomerCaseManagedBean.uploadPhoto.filenAME: " + filepath);
            try (FileOutputStream stream = new FileOutputStream((new File(directoryPath + "/" + filename)))) {
                System.out.println("CustomerCaseManagedBean.uploadPhoto: start writing");
                stream.write(bytes);
                stream.flush();
                newIssue.setAttachmentFileName(filename);
            } catch (Exception ex) {
                System.out.println("CustomerCaseManagedBean.uploadPhoto: " + ex.toString());
            }
        }
    }

    public void searchByIdFunction() {
        searchResultList = null;

        try {
            CustomerCase resultCase = customerCaseSessionBean.getCaseById(searchCaseID);
            searchResultList = new ArrayList<>();
            searchResultList.add(resultCase);

        } catch (CustomerCaseNotFoundException e) {
            String msg = "Result not found!";
            MessageUtils.displayInfo(msg);
        }

    }

    public void searchByTitleFunction() {
        searchResultList = null;

        try {
            searchResultList = customerCaseSessionBean.getCaseByTitle(searchCaseTitle);
        } catch (CustomerCaseNotFoundByTitleException e) {
            String msg = "Result not found!";
            MessageUtils.displayInfo(msg);
        }

    }

    public void cancelCase(String caseID) {

        try {

            customerCaseSessionBean.removeCase(caseID);
            if (!searchType.equals("viewAllCases")) {
                for (int i = 0; i < searchResultList.size(); i++) {
                    if (Objects.equals(searchResultList.get(i).getId(), caseID)) {
                        searchResultList.remove(i);
                        String msg = "Case cancel successfully!";
                        MessageUtils.displayInfo(msg);
                    }
                }
            }

            for (int i = 0; i < allCaseList.size(); i++) {
                if (Objects.equals(allCaseList.get(i).getId(), caseID)) {
                    allCaseList.remove(i);
                    String msg = "Case cancel successfully!";
                    MessageUtils.displayInfo(msg);
                }
            }

        } catch (CancelCustomerCaseException e) {
            String msg = "Error!";
            MessageUtils.displayError(msg);
        }

        emailServiceSessionBean.sendCancelCaseConfirmationToCustomer(mainAccount.getCustomer().getEmail(), customerCase);
    }

    public Boolean isChargeBackSelected(String issueFieldSelected) {
        return issueFieldSelected.equals("CHARGEBACK");
    }

    public CustomerCase getCustomerCase() {
        return customerCase;
    }

    public void setCustomerCase(CustomerCase customerCase) {
        this.customerCase = customerCase;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public Boolean getIssuePage() {
        return issuePage;
    }

    public void setIssuePage(Boolean issuePage) {
        this.issuePage = issuePage;
    }

    public Issue getNewIssue() {
        return newIssue;
    }

    public void setNewIssue(Issue newIssue) {
        this.newIssue = newIssue;
    }

    public int getNumOfIssues() {
        return numOfIssues;
    }

    public void setNumOfIssues(int numOfIssues) {
        this.numOfIssues = numOfIssues;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchByCaseID() {
        return searchByCaseID;
    }

    public void setSearchByCaseID(String searchByCaseID) {
        this.searchByCaseID = searchByCaseID;
    }

    public String getSearchByCaseTitle() {
        return searchByCaseTitle;
    }

    public void setSearchByCaseTitle(String searchByCaseTitle) {
        this.searchByCaseTitle = searchByCaseTitle;
    }

    public List<CustomerCase> getSearchResultList() {
        return searchResultList;
    }

    public void setSearchResultList(List<CustomerCase> searchResultList) {
        this.searchResultList = searchResultList;
    }

    public String getSearchCaseID() {
        return searchCaseID;
    }

    public void setSearchCaseID(String searchCaseID) {
        this.searchCaseID = searchCaseID;
    }

    public String getSearchCaseTitle() {
        return searchCaseTitle;
    }

    public void setSearchCaseTitle(String searchCaseTitle) {
        this.searchCaseTitle = searchCaseTitle;
    }

    public String getViewAllCases() {
        return viewAllCases;
    }

    public void setViewAllCases(String viewAllCases) {
        this.viewAllCases = viewAllCases;
    }

    public List<CustomerCase> getAllCaseList() {
        return allCaseList;
    }

    public void setAllCaseList(List<CustomerCase> allCaseList) {
        this.allCaseList = allCaseList;
    }

    public List<String> getIssueFieldList() {
        return issueFieldList;
    }

    public void setIssueFieldList(List<String> issueFieldList) {
        this.issueFieldList = issueFieldList;
    }

    /**
     * @return the customerIC
     */
    public String getCustomerIC() {
        return customerIC;
    }

    /**
     * @param customerIC the customerIC to set
     */
    public void setCustomerIC(String customerIC) {
        this.customerIC = customerIC;
    }

}
