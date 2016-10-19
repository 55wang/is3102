/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.fact;

import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.ejb.Schedule;
import javax.ejb.Startup;

/**
 *
 * @author wang
 */
@Startup
@Singleton
public class SchedulerFactSessionBean {

    private Logger logger = Logger.getLogger(
            "SchedulerFactSessionBean");

    @Schedule(second = "0", minute = "*/1", hour = "0",
            info = "MinScheduler",
            persistent = false)
    public void automaticTimeout() {
        logger.info("Automatic timeout occured");
        System.out.print("aa");

        //create a scheduler to populate the fact table every night.
        // 1. portfolio
        // 2. debt
        // 3. deposit
        // 4. customer
    }

}
