/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.wealth;

import ejb.session.mainaccount.MainAccountSessionBeanLocal;
import ejb.session.wealth.FinancialInstrumentSessionBeanLocal;
import ejb.session.wealth.InvestmentPlanSessionBeanLocal;
import entity.customer.MainAccount;
import entity.customer.WealthManagementSubscriber;
import entity.wealth.FinancialInstrument;
import entity.wealth.InvestmentPlan;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.model.DualListModel;
import server.utilities.EnumUtils;
import server.utilities.EnumUtils.FinancialInstrumentClass;
import utils.SessionUtils;

/**
 *
 * @author VIN-S
 */
@Named(value = "requestInvestmentPlanManagedBean")
@ViewScoped
public class RequestInvestmentPlanManagedBean implements Serializable {
    @EJB
    private InvestmentPlanSessionBeanLocal investmentPlanSessionBean;
    @EJB
    private FinancialInstrumentSessionBeanLocal financialInstrumentSessionBean;
    @EJB
    private MainAccountSessionBeanLocal mainAccountSessionBean;
    
    
    private WealthManagementSubscriber wms;
    private MainAccount mainAccount;
    private InvestmentPlan newInvestmenPlan = new InvestmentPlan();
    private List<FinancialInstrument> allFinancialInstruments;
    private DualListModel<FinancialInstrument> financialInstruments;

    /**
     * Creates a new instance of RequestInvestmentPlanManagedBean
     */
    public RequestInvestmentPlanManagedBean() {
    }
    
    // Followed by @PostConstruct
    @PostConstruct
    public void init() {
        setMainAccount(mainAccountSessionBean.getMainAccountByUserId(SessionUtils.getUserName()));
        setWms(mainAccount.getWealthManagementSubscriber());
        setAllFinancialInstruments(financialInstrumentSessionBean.getAllFinancialInstruments());
        
        List<FinancialInstrument> selectedFinancialInstruments = new ArrayList<FinancialInstrument>();
        financialInstruments = new DualListModel<FinancialInstrument>(allFinancialInstruments, selectedFinancialInstruments);       
    }
    
    public void submit(){
        List<FinancialInstrument> targetFI = new ArrayList<FinancialInstrument>();
        for(int i = 0; i < financialInstruments.getTarget().size(); i++)
            for(int j=0;j<allFinancialInstruments.size();j++)
                if(allFinancialInstruments.get(j).getName().getValue().equals(financialInstruments.getTarget().get(i)))
                    targetFI.add(allFinancialInstruments.get(j));
        
        newInvestmenPlan.setPreferedFinancialInstrument(targetFI);
        newInvestmenPlan.setWealthManagementSubscriber(wms);
        newInvestmenPlan.setStatus(EnumUtils.InvestmentPlanStatus.PENDING);
        
        investmentPlanSessionBean.createInvestmentPlan(newInvestmenPlan);
    }

    public WealthManagementSubscriber getWms() {
        return wms;
    }

    public void setWms(WealthManagementSubscriber wms) {
        this.wms = wms;
    }

    public MainAccount getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccount mainAccount) {
        this.mainAccount = mainAccount;
    }

    public InvestmentPlan getNewInvestmenPlan() {
        return newInvestmenPlan;
    }

    public void setNewInvestmenPlan(InvestmentPlan newInvestmenPlan) {
        this.newInvestmenPlan = newInvestmenPlan;
    }

    public List<FinancialInstrument> getAllFinancialInstruments() {
        return allFinancialInstruments;
    }

    public void setAllFinancialInstruments(List<FinancialInstrument> allFinancialInstruments) {
        this.allFinancialInstruments = allFinancialInstruments;
    }

    public DualListModel<FinancialInstrument> getFinancialInstruments() {
        return financialInstruments;
    }

    public void setFinancialInstruments(DualListModel<FinancialInstrument> financialInstruments) {
        this.financialInstruments = financialInstruments;
    }
}
