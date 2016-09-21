/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.cms;

import ejb.session.audit.AuditSessionBeanLocal;
import ejb.session.cms.CustomerCaseSessionBeanLocal;
import ejb.session.common.EmailServiceSessionBeanLocal;
import entity.common.AuditLog;
import entity.customer.CustomerCase;
import entity.customer.Issue;
import entity.customer.MainAccount;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import utils.SessionUtils;
import utils.MessageUtils;
import utils.RedirectUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "customerCaseManagedBean")
@ViewScoped
public class CustomerCaseManagedBean implements Serializable {
    @EJB
    private EmailServiceSessionBeanLocal emailServiceSessionBean;
    @EJB
    private CustomerCaseSessionBeanLocal customerCaseSessionBean;
    @EJB
    private AuditSessionBeanLocal auditSessionBean;
    
    
    private MainAccount mainAccount; 
    private List<AuditLog> auditLogs;
    private CustomerCase customerCase = new CustomerCase();
    private List<Issue> issues = new ArrayList<Issue> ();
    private Issue newIssue = new Issue();
    private Boolean issuePage = false;
    private int numOfIssues = 0;
    private String directoryPath;
    private String searchType="viewAllCases";
    private String viewAllCases = "viewAllCases";
    private String searchByCaseID = "CaseID";
    private String searchByCaseTitle = "CaseTitle";
    private List<CustomerCase> searchResultList;
    private List<CustomerCase> allCaseList;
    private String searchCaseID;
    private String searchCaseTitle;
    

    /**
     * Creates a new instance of CustomerCaseManagedBean
     */
    public CustomerCaseManagedBean() {
    }
    
    public void goToIssuePage (){
        issuePage = true;
    }
    
    public void addIssue (){
        issues.add(newIssue);
        newIssue.setCustomerCase(customerCase);
        newIssue = new Issue();
        issuePage = false;
        numOfIssues++;
    }
    
    public void saveCase(){
        customerCase.setIssues(issues);
        customerCase.setMainAccount(mainAccount);
        mainAccount.addCase(customerCase);
        customerCaseSessionBean.saveCase(customerCase);
        RedirectUtils.redirect("view_case.xhtml");
        emailServiceSessionBean.sendNewCaseConfirmationToCustomer(mainAccount.getCustomer().getEmail(), customerCase);
    }
    
    public void removeIssue(String issueID){
        for(int i = 0; i < issues.size(); i++){
            if(issues.get(i).toString().equals(issueID))
                issues.remove(i);
        }
    }
    
    public void uploadPhoto(FileUploadEvent e) throws IOException{
        System.out.println("CustomerCaseManagedBean.uploadPhoto: start uploading");
        UploadedFile uploadedPhoto=e.getFile();
        String filename = FilenameUtils.getName(uploadedPhoto.getFileName());
 
        byte[] bytes=null;
        
            if (uploadedPhoto!=null) {
                ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext(); 
                directoryPath = ec.getRealPath("//WEB-INF//files//");
                bytes = uploadedPhoto.getContents();
                String filepath = directoryPath+"/"+filename;
                System.out.println("CustomerCaseManagedBean.uploadPhoto.filenAME: " + filepath);
            try (FileOutputStream stream = new FileOutputStream((new File(directoryPath+"/"+filename)))) {
                System.out.println("CustomerCaseManagedBean.uploadPhoto: start writing");
                stream.write(bytes);
                stream.flush();
                newIssue.setAttachmentFileName(filename);
            }catch(Exception ex){
                System.out.println("CustomerCaseManagedBean.uploadPhoto: " + ex.toString());
                }
            }
    }
    
    public void searchByIdFunction(){
        searchResultList = null;
        CustomerCase resultCase = customerCaseSessionBean.searchCaseByID(searchCaseID);
        
        if(resultCase == null){        
            String msg = "Result not found!";
            MessageUtils.displayInfo(msg);
        }else{
            searchResultList = new ArrayList<CustomerCase>();
            searchResultList.add(resultCase);
        }                
    }
    
    public void searchByTitleFunction(){
        searchResultList = null;
        searchResultList = customerCaseSessionBean.searchCaseByTitle(searchCaseTitle);
        
        if(searchResultList.size() == 0){
            String msg = "Result not found!";
            MessageUtils.displayInfo(msg);
        }            
    }
    
    public void cancelCase(Long caseID){
        if(customerCaseSessionBean.cancelCase(caseID)){
            if(!searchType.equals("viewAllCases")){
                for(int i=0; i<searchResultList.size();i++){
                    if(Objects.equals(searchResultList.get(i).getId(), caseID)){
                        searchResultList.remove(i);
                        String msg = "Case cancel successfully!";
                        MessageUtils.displayInfo(msg);
                    }
                }
            }
            
                for(int i=0; i<allCaseList.size();i++){
                    if(Objects.equals(allCaseList.get(i).getId(), caseID)){
                        allCaseList.remove(i);
                        String msg = "Case cancel successfully!";
                        MessageUtils.displayInfo(msg);
                    }
                }
            
        }else{
            String msg = "Error!";
            MessageUtils.displayError(msg);
        }       
        
        emailServiceSessionBean.sendCancelCaseConfirmationToCustomer(mainAccount.getCustomer().getEmail(), customerCase);
    }

    @PostConstruct
    public void setMainAccount() {
        this.mainAccount = customerCaseSessionBean.getMainAccountByUserID(SessionUtils.getUserName());
        this.allCaseList = customerCaseSessionBean.getAllCase();
        this.auditLogs = auditSessionBean.getAuditLogByCustomerID(SessionUtils.getUserName());
    }

    public List<AuditLog> getAuditLogs() {
        return auditLogs;
    }

    public void setAuditLogs(List<AuditLog> auditLogs) {
        this.auditLogs = auditLogs;
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
    
}
