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
@javax.ws.rs.ApplicationPath("meps")
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
        resources.add(filter.CORSFilter.class);
        resources.add(webservice.restful.settlement.MEPSAgencySettlementService.class);
        resources.add(webservice.restful.settlement.MEPSFastSettlementService.class);
        resources.add(webservice.restful.settlement.MEPSSettlementService.class);
    }
    
}

