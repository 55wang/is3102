/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.restful.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author leiyang
 */
@javax.ws.rs.ApplicationPath("rest")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        System.out.println("ApplicationConfig Get Classes");
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(filters.CORSFilter.class);
        resources.add(webservice.restful.creditcard.CreditCardAuthorizationService.class);
        resources.add(webservice.restful.creditcard.CreditCardClearingService.class);
        resources.add(webservice.restful.creditcard.CreditCardSettlementService.class);
        resources.add(webservice.restful.mobile.MobileInitPayLahService.class);
        resources.add(webservice.restful.mobile.MobileOTPService.class);
        resources.add(webservice.restful.mobile.MobileTransferService.class);
        resources.add(webservice.restful.mobile.MobileUserLoginService.class);
        resources.add(webservice.restful.mobile.summary.MobileAccountHistoryService.class);
        resources.add(webservice.restful.mobile.summary.MobileAccountSummaryService.class);
        resources.add(webservice.restful.transfer.NetSettlementService.class);
        resources.add(webservice.restful.transfer.ReceiveCCPayment.class);
        resources.add(webservice.restful.transfer.ReceiveGIROPaymentRequest.class);
        resources.add(webservice.restful.transfer.ReceiveTransferPayment.class);
    }
    
}
